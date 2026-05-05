package com.garment.common.domain;

/**
 * 租户上下文（基于 ThreadLocal）。
 * <p>
 * <b>兼容桥接类</b>——推荐直接使用 {@link AuthUserContext} 获取完整的认证信息。
 * <p>
 * 新代码应使用：
 * <pre>
 *   Long tenantId = AuthUserContext.requireTenantId();
 *   AuthInfo info  = AuthUserContext.getAuthInfo();
 * </pre>
 * <p>
 * 旧代码中直接调用 {@link #getTenantId()} / {@link #requireTenantId()} 的地方
 * 不会立即报错，内部已委托给 {@link AuthUserContext}。
 */
public final class TenantContext {

    private TenantContext() {}

    /**
     * 设置租户 ID。
     * <p>
     * <b>注意</b>：正常流程中不需要手动调用，由 {@link com.garment.common.interfaces.TenantWebFilter}
     * 统一通过 {@link AuthUserContext#set(AuthInfo)} 设置。
     * 保留此方法仅为极端场景（如定时任务、MQ 消费）下手动注入。
     */
    public static void setTenantId(Long tenantId) {
        // 如果 AuthUserContext 已有数据则补充 tenantId，否则创建一个仅含 tenantId 的 AuthInfo
        AuthInfo existing = AuthUserContext.getAuthInfo();
        if (existing != null) {
            existing.setTenantId(tenantId);
        } else {
            AuthUserContext.set(AuthInfo.of(null, null, tenantId));
        }
    }

    /**
     * 获取当前租户 ID。
     */
    public static Long getTenantId() {
        return AuthUserContext.getTenantId();
    }

    /**
     * 获取当前租户 ID，若为空则抛出业务异常。
     */
    public static Long requireTenantId() {
        return AuthUserContext.requireTenantId();
    }

    /**
     * 清理上下文。
     * <p>
     * 委托给 {@link AuthUserContext#clear()}，同时清空自身的遗留数据。
     */
    public static void clear() {
        AuthUserContext.clear();
    }
}