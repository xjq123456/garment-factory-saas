package com.garment.user.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置
 * <p>
 * 提供密码编码器等安全相关的 Bean 定义。
 * BCryptPasswordEncoder 用于用户密码的加密和校验。
 * </p>
 *
 * @author garment-factory-saas
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 安全过滤链配置
     * <p>
     * 注册和登录接口允许匿名访问，其余接口需要认证。
     * 使用无状态 Session 策略（JWT 认证）。
     * </p>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/api/v1/users/register",
                            "/api/v1/users/login",
                            "/actuator/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            );
        return http.build();
    }


    /**
     * BCrypt 密码编码器
     * <p>
     * 使用 BCrypt 强哈希算法对密码进行加密，
     * 每次加密相同密码都会生成不同的哈希值（内置随机盐）。
     * </p>
     *
     * @return PasswordEncoder 实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}