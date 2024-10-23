package com.example.duantn.Secutiry;

import com.example.duantn.service.OurUserDetailsService;
import com.example.duantn.TokenUser.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration {

    @Autowired
    private OurUserDetailsService ourUserDetailsService; // Dịch vụ chi tiết người dùng

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter; // Bộ lọc xác thực JWT

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                // Vô hiệu hóa bảo vệ CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Cấu hình CORS nếu cần
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Cấu hình quy tắc truy cập
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**",
                                "/api/san_pham_chi_tiet/**",
                                "/api/giohang/**",
                                "/api/san_pham/findDanhMuc/**",
                                "/api/san_pham_chi_tiet/findSanPhamCT/**",
                                "/api/ad_mau_sac/**",
                                "/api/ad_chat_lieu/**",
                                "/api/ad_kich_thuoc/**",
                                "/api/ad_san_pham/**",
                                "/api/ad_danh_muc",
                                "/api/hinh_anh/**",
                                "/api/vai-tro/**",
                                "/api/xac-thuc/**",
                                "/api/san_pham/**",
                                "/api/ad_san_pham/**").permitAll() // Các đường dẫn này được cho phép truy cập mà không cần xác thực
                        // Bỏ chú thích các dòng bên dưới để hạn chế quyền truy cập dựa trên vai trò
                        //.requestMatchers("/api/admin/login/**").hasRole("admin")
                        //.requestMatchers("/api/auth/login/**").hasRole("Khach_Hang")
                        .anyRequest().authenticated() // Tất cả các yêu cầu khác đều yêu cầu xác thực
                )
                .httpBasic(Customizer.withDefaults()) // Kích hoạt xác thực cơ bản

                // Thêm bộ lọc JWT và cấu hình quản lý phiên
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Quản lý phiên không trạng thái
                .authenticationProvider(authenticationProvider()) // Thiết lập nhà cung cấp xác thực
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Thêm bộ lọc JWT trước bộ lọc xác thực tên người dùng/mật khẩu

        return http.build(); // Trả về chuỗi bộ lọc bảo mật
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Định nghĩa bộ mã hóa mật khẩu
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Trả về nhà cung cấp xác thực
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(); // Tạo nhà cung cấp xác thực tùy chỉnh
        daoAuthenticationProvider.setUserDetailsService(ourUserDetailsService); // Thiết lập dịch vụ chi tiết người dùng
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); // Thiết lập bộ mã hóa mật khẩu
        return daoAuthenticationProvider; // Trả về nhà cung cấp xác thực
    }
}
