package com.garment.order.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.garment.common.infrastructure.MybatisPlusTenantConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        return MybatisPlusTenantConfig.createInterceptor();
    }
}
