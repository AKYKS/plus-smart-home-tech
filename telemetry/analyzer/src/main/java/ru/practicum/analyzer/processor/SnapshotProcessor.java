package ru.practicum.analyzer.processor;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.configuration.ConsumerConfiguration;
import ru.practicum.analyzer.configuration.SnapshotConsumerConfiguration;
import ru.practicum.analyzer.entity.*;
import ru.practicum.analyzer.repository.ScenarioRepository;
import ru.practicum.analyzer.service.snapshot.condition.ConditionProcessor;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {
    private final ConsumerConfiguration consumerConfiguration;
    private final SnapshotConsumerConfiguration snapshotConsumerConfiguration;
    private final KafkaConsumer<String, SpecificRecordBase> snapshotConsumer;
    private final ScenarioRepository scenarioRepository;
    private final ConditionProcessor conditionProcessor;

    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(snapshotConsumer::wakeup));
        try {
            snapshotConsumer.subscribe(snapshotConsumerConfiguration.getTopics());
            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = snapshotConsumer.poll(consumerConfiguration.getAttemptTimeout());
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    SensorsSnapshotAvro snapshot = (SensorsSnapshotAvro) record.value();
                    processSnapshot(snapshot);
                }
                snapshotConsumer.commitSync();
            }
        } catch (Exception e) {
            log.error("SnapshotProcessor error: ", e);
        } finally {
            snapshotConsumer.close();
        }
    }

    private void processSnapshot(SensorsSnapshotAvro snapshot) {
        List<Scenario> scenarios = scenarioRepository.findByHubId(snapshot.getHubId());
        for (Scenario scenario : scenarios) {
            if (checkConditions(scenario, snapshot)) {
                executeActions(scenario);
            }
        }
    }

    private boolean checkConditions(Scenario scenario, SensorsSnapshotAvro snapshot) {
        for (ScenarioCondition scenarioCondition : scenario.getConditions()) {
            Sensor sensor = scenarioCondition.getSensor();
            Condition condition = scenarioCondition.getCondition();
            SensorStateAvro state = snapshot.getSensorsState().get(sensor.getId());
            if (state == null || conditionProcessor.isNotMatch(condition, state)) {
                return false;
            }
        }
        return true;
    }

    private void executeActions(Scenario scenario) {
        for (ScenarioAction scenarioAction : scenario.getActions()) {
            Sensor sensor = scenarioAction.getSensor();
            Action action = scenarioAction.getAction();
            Instant now = Instant.now();
            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(scenario.getHubId())
                    .setScenarioName(scenario.getName())
                    .setAction(
                            DeviceActionProto.newBuilder()
                                    .setSensorId(sensor.getId())
                                    .setType(ActionTypeProto.valueOf(action.getType()))
                                    .setValue(action.getValue() != null ? action.getValue() : 0)
                                    .build()
                    )
                    .setTimestamp(Timestamp.newBuilder()
                            .setSeconds(now.getEpochSecond())
                            .setNanos(now.getNano())
                            .build())
                    .build();
            hubRouterClient.handleDeviceAction(request);
            log.trace("action executed. scenario: '{}', sensor: '{}', action: '{}'", scenario.getName(), sensor.getId(), action.getType());
        }
    }
}
