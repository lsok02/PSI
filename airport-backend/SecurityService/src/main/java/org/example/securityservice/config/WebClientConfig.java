package org.example.securityservice.config;

import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient authServiceWebClient() {
        String authServiceBaseUrl = "http://localhost:9090";
        return WebClient.builder()
                .baseUrl(authServiceBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}