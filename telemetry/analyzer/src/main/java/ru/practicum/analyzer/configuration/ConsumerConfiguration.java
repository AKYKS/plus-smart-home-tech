package ru.practicum.analyzer.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@AllArgsConstructor
@ConfigurationProperties("kafka.consumer")
@SuppressWarnings("unused")
public class ConsumerConfiguration {
    private long attemptTimeoutMillis;

    public Duration getAttemptTimeout() {
        return Duration.ofMillis(attemptTimeoutMillis);
    }
}
