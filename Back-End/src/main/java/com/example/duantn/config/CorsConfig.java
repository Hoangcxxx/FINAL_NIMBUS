package com.example.duantn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Cho phép tất cả các nguồn (origins) hoặc chỉ định cụ thể các nguồn
        registry.addMapping("/api/**")  // Áp dụng cho tất cả các API
                .allowedOrigins("http://localhost:5501", "http://127.0.0.1:5502")  // Cho phép yêu cầu từ localhost:5500
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Các phương thức HTTP được phép
                .allowedHeaders("*")  // Cho phép tất cả các header
                .allowCredentials(true);  // Nếu cần gửi cookie trong yêu cầu
    }
}
