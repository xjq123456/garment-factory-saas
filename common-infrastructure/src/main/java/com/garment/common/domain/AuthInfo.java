package com.garment.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 认证信息载体。
 * <p>
 * 由网关层解析 JWT 后通过请求头传递给下游微服务，
 * 各服务的 {@code AuthWebFilter} 负责将其写入 {@link AuthUserContext}。
 */
@Data
public class AuthInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户 ID */
    private Long userId;

    /** 用户名 */
    private String username;

    /** 租户 ID */
    private Long tenantId;

    /** 用户角色列表 */
    private List<String> roles = Collections.emptyList();

    public static AuthInfo of(Long userId, String username, Long tenantId) {
        AuthInfo info = new AuthInfo();
        info.userId = userId;
        info.username = username;
        info.tenantId = tenantId;
        return info;
    }
}