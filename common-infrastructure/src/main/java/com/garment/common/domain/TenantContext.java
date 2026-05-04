package com.garment.common.domain;

/**
 * 租户上下文（基于 ThreadLocal）。
 * <p>
 * 在请求进入时由 WebFilter 设置，请求结束时清理。
 * 业务代码可通过 {@link #getTenantId()} 获取当前租户标识。
 */
public final class TenantContext {

    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();

    private TenantContext() {}

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    /**
     * 获取当前租户 ID，若为空则抛出业务异常。
     */
    public static Long requireTenantId() {
        Long id = TENANT_ID.get();
        if (id == null) {
            throw new com.garment.common.domain.BizException("当前请求未携带租户信息");
        }
        return id;
    }

    public static void clear() {
        TENANT_ID.remove();
    }
}