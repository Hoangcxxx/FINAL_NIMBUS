package com.example.duantn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // Timeout kết nối (tăng lên 10 giây)
        factory.setReadTimeout(10000);    // Timeout đọc (tăng lên 10 giây)
        RestTemplate restTemplate = new RestTemplate(factory);

        // Logging request và response
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}
