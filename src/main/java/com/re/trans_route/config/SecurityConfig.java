package com.re.trans_route.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final RoleBasedLoginSuccessHandler roleBasedLoginSuccessHandler;

    public SecurityConfig(RoleBasedLoginSuccessHandler roleBasedLoginSuccessHandler) {
        this.roleBasedLoginSuccessHandler = roleBasedLoginSuccessHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
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
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/passenger/**").hasRole("PASSENGER")
//                        .requestMatchers("/staff/**").hasRole("STAFF")
                        .anyRequest().authenticated()
                )

                // 3. Controller xu? li' form dang nhap., tat' form default
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login") // logout xong se~ qua day
                        .invalidateHttpSession(true)    // xoa' session treen server
                        .clearAuthentication(true)  // xoa' thong tin dang nhap. tren SecurityContextHolder
                        .deleteCookies("JSESSIONID") // xoa' cookie session tren client
                )
                .httpBasic(AbstractHttpConfigurer::disable)

                // cau' hinh` khi reload server mat' session ma` client con` khi do' tu. load ve` dang nhap.
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/auth/login");
                        })
                );

        return http.build();
    }
}
