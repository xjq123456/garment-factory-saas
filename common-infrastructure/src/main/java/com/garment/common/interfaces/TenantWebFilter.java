package com.garment.common.interfaces;

import com.garment.common.domain.AuthInfo;
import com.garment.common.domain.AuthUserContext;
import com.garment.common.domain.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 认证 & 租户解析过滤器。
 * <p>
 * 从网关转发的请求头中提取用户认证信息，写入 {@link AuthUserContext} 和
 * {@link TenantContext}（兼容旧调用），请求结束后自动清理。
 * <p>
 * 请求头约定（由 Gateway {@code JwtAuthGlobalFilter} 注入）：
 * <ul>
 *   <li>{@code X-User-Id}    — 用户 ID</li>
 *   <li>{@code X-Username}   — 用户名</li>
 *   <li>{@code X-Tenant-Id}  — 租户 ID</li>
 *   <li>{@code X-Roles}      — 角色列表（逗号分隔）</li>
 * </ul>
 */
public class TenantWebFilter extends OncePerRequestFilter {

    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USERNAME = "X-Username";
    public static final String HEADER_TENANT_ID = "X-Tenant-Id";
    public static final String HEADER_ROLES = "X-Roles";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 解析 Gateway 注入的认证信息
            Long userId = parseLong(request.getHeader(HEADER_USER_ID));
            String username = request.getHeader(HEADER_USERNAME);
            Long tenantId = parseLong(request.getHeader(HEADER_TENANT_ID));
            String rolesStr = request.getHeader(HEADER_ROLES);

            // 只要有任一认证头就构建 AuthInfo
            if (userId != null || tenantId != null) {
                AuthInfo authInfo = AuthInfo.of(userId, username, tenantId);
                if (rolesStr != null && !rolesStr.isBlank()) {
                    authInfo.setRoles(java.util.Arrays.asList(rolesStr.split(",")));
                }
                AuthUserContext.set(authInfo);
                // 兼容：同步设置 TenantContext
                if (tenantId != null) {
                    TenantContext.setTenantId(tenantId);
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            AuthUserContext.clear();
            TenantContext.clear();
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}