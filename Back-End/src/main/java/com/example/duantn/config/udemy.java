package com.example.duantn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

public class udemy {
    @Configuration
    public class AppConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

}
