package com.garment.common.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.garment.common.domain.JwtUtils;
import com.garment.common.infrastructure.AuditMetaObjectHandler;
import com.garment.common.infrastructure.MybatisPlusTenantConfig;
import com.garment.common.interfaces.GlobalExceptionHandler;
import com.garment.common.interfaces.TenantWebFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 公共基础设施自动装配。
 * <p>
 * 业务服务引入 common-infrastructure 依赖后，以下组件将自动注册：
 * <ul>
 *   <li>全局异常处理器</li>
 *   <li>多租户解析过滤器</li>
 *   <li>MyBatis Plus 多租户插件</li>
 *   <li>审计字段自动填充</li>
 *   <li>JWT 工具类（需配置 garment.jwt.secret）</li>
 * </ul>
 */
@Configuration
public class CommonAutoConfiguration {

    /* ==================== 全局异常处理 ==================== */

    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /* ==================== 多租户过滤器 ==================== */

    @Bean
    public FilterRegistrationBean<TenantWebFilter> tenantWebFilter() {
        FilterRegistrationBean<TenantWebFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TenantWebFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1); // 最高优先级
        return registration;
    }

    /* ==================== MyBatis Plus 多租户插件 ==================== */

    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        return MybatisPlusTenantConfig.createInterceptor();
    }

    /* ==================== 审计字段自动填充 ==================== */

    @Bean
    @ConditionalOnMissingBean(AuditMetaObjectHandler.class)
    public AuditMetaObjectHandler auditMetaObjectHandler() {
        return new AuditMetaObjectHandler();
    }

    /* ==================== JWT 工具类 ==================== */

    @Bean
    @ConditionalOnProperty(prefix = "garment.jwt", name = "secret")
    public JwtUtils jwtUtils(
            @org.springframework.beans.factory.annotation.Value("${garment.jwt.secret}") String secret,
            @org.springframework.beans.factory.annotation.Value("${garment.jwt.expiration:86400000}") long expiration) {
        return new JwtUtils(secret, expiration);
    }
}