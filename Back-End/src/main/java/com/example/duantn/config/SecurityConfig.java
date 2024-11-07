package com.example.duantn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Cấu hình SecurityFilterChain thay vì WebSecurityConfigurerAdapter
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/api/nguoi_dung/**").permitAll()  // Cho phép truy cập không cần xác thực
                .anyRequest().authenticated()  // Các endpoint khác yêu cầu xác thực
                .and()
                .httpBasic(); // Sử dụng Basic Authentication (hoặc JWT tùy theo yêu cầu)
        return http.build();
    }

    // Bean PasswordEncoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
