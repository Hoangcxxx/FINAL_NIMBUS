package com.example.duantn.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/api/nguoi_dung/**", "/api/**").permitAll()  // Cho phép truy cập không cần xác thực
                .anyRequest().authenticated()  // Các endpoint khác yêu cầu xác thực
                .and()
                .httpBasic()
                .and()
                .logout()
                .logoutUrl("/api/nguoi_dung/dang_xuat")  // Định nghĩa URL logout
                .logoutSuccessHandler((request, response, authentication) -> {
                    // Hủy session hoặc xóa thông tin đăng nhập
                    SecurityContextHolder.clearContext(); // Xóa Spring Security context
                    response.setStatus(HttpServletResponse.SC_OK);  // Trả về trạng thái 200 OK
                    response.getWriter().write("Đăng xuất thành công");  // Trả về thông báo đăng xuất thành công
                })
                .invalidateHttpSession(true)  // Hủy session khi đăng xuất
                .clearAuthentication(true);  // Xóa thông tin xác thực của người dùng

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


