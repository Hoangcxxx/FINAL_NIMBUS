package com.example.duantn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfig {
    @Bean
    public PayOS payOS() {
        return new PayOS("f1dc62a5-bd5c-4a10-b527-2ec86ca3f81d",
                "2b7d933b-d381-405f-ad7d-c1754323e55a",
                "c690326daec38e1df3def185453f1131e8ff1cb137d5d9ac71688587b896f0ec");
    }
}