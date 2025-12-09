package com.CoopCredit.CoopCredit.infrastructure.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterRegistry getMeterRegistry(MeterRegistry meterRegistry) {
        return meterRegistry;
    }
}