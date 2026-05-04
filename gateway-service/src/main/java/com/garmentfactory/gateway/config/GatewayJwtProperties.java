package com.garmentfactory.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关 JWT 配置属性。
 */
@Data
@ConfigurationProperties(prefix = "gateway.jwt")
public class GatewayJwtProperties {

    /**
     * 是否启用 JWT 认证。
     */
    private boolean enabled = true;

    /**
     * JWT 签名密钥（至少 32 字节）。
     */
    private String secret = "garment-factory-default-secret-key-min32";

    /**
     * 白名单路径（Ant 风格，无需 Token 即可访问）。
     */
    private List<String> whitelist = new ArrayList<>();
}