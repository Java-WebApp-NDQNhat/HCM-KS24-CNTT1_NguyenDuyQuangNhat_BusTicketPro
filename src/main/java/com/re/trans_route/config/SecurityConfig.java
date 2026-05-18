package com.re.trans_route.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt cơ chế chống Cross-Site Request Forgery (Cần thiết khi làm việc với API)
                .csrf(csrf -> csrf.disable())

                // 2. Cấu hình phân quyền đường dẫn
                .authorizeHttpRequests(auth -> auth
                        // Cho phép TẤT CẢ mọi người truy cập vào các API auth (đăng ký, đăng nhập) mà không cần chặn
                        .requestMatchers("/auth/**").permitAll()
                        // Tất cả các request khác thì vẫn bắt buộc phải xác thực (tùy bạn cấu hình sau)
                        .requestMatchers("/login", "/register", "/").permitAll()
                        .requestMatchers("/css/**", "/assets/**").permitAll()
                        .requestMatchers("/error", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )

                // 3. Đăng nhập bằng form tùy chỉnh
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/page/tmp", true)
                        .permitAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
