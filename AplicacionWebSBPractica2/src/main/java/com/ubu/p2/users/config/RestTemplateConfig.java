package com.ubu.p2.users.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * declaramos un RestTemplate con time-out de 5sec para hacer llamadas a la API Python.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3))  // 3 s para conectar
                .setReadTimeout(Duration.ofSeconds(5))     // 5 s para leer
                .build();
    }
}
