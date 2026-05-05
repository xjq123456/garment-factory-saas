package com.garment.common.domain;

/**
 * 认证用户上下文（基于 ThreadLocal）。
 * <p>
 * 在请求进入时由 {@link com.garment.common.interfaces.TenantWebFilter} 设置，
 * 请求结束时自动清理。
 * <p>
 * 业务代码可直接通过 {@link #getAuthInfo()} 获取当前请求的完整认证信息，
 * 无需在方法签名中逐层传递 tenantId / userId。
 * <p>
 * 参考 zanmall-local 的 AuthUserContext 设计，分离"认证上下文"关注点。
 */
public final class AuthUserContext {

    private static final ThreadLocal<AuthInfo> AUTH_INFO = new ThreadLocal<>();

    private AuthUserContext() {}

    public static void set(AuthInfo authInfo) {
        AUTH_INFO.set(authInfo);
    }

    public static AuthInfo getAuthInfo() {
        return AUTH_INFO.get();
    }

    /**
     * 获取当前租户 ID（便捷方法）。
     * <p>
     * 等价于 {@code getAuthInfo().getTenantId()}，若未认证则返回 null。
     */
    public static Long getTenantId() {
        AuthInfo info = AUTH_INFO.get();
        return info != null ? info.getTenantId() : null;
    }

    /**
     * 获取当前用户 ID（便捷方法）。
     */
    public static Long getUserId() {
        AuthInfo info = AUTH_INFO.get();
        return info != null ? info.getUserId() : null;
    }

    /**
     * 获取当前用户名（便捷方法）。
     */
    public static String getUsername() {
        AuthInfo info = AUTH_INFO.get();
        return info != null ? info.getUsername() : null;
    }

    /**
     * 获取当前租户 ID，若为空则抛出业务异常。
     */
    public static Long requireTenantId() {
        Long id = getTenantId();
        if (id == null) {
            throw new BizException("当前请求未携带租户信息");
        }
        return id;
    }

    public static void clear() {
        AUTH_INFO.remove();
    }
}