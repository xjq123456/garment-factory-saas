package com.garment.common.interfaces;

import com.garment.common.domain.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 租户解析过滤器。
 * <p>
 * 从请求头 {@code X-Tenant-Id} 中提取租户 ID 并写入 {@link TenantContext}，
 * 请求结束后自动清理，防止 ThreadLocal 泄漏。
 */
public class TenantWebFilter extends OncePerRequestFilter {

    public static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String tenantIdStr = request.getHeader(TENANT_HEADER);
            if (tenantIdStr != null && !tenantIdStr.isBlank()) {
                TenantContext.setTenantId(Long.parseLong(tenantIdStr.trim()));
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}