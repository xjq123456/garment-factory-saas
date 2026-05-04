package com.garment.user.domain.identity.vo;

/**
 * 用户状态枚举
 * <p>
 * 定义用户账号在其生命周期中可能处于的状态。
 * 状态流转：ACTIVE ↔ FROZEN → DELETED（不可逆）
 * </p>
 *
 * @author garment-factory-saas
 */
public enum UserStatus {

    /** 正常：用户可以正常登录和使用系统 */
    ACTIVE("ACTIVE", "正常"),

    /** 冻结：用户账号被管理员冻结，无法登录 */
    FROZEN("FROZEN", "冻结"),

    /** 已删除：逻辑删除状态，账号数据保留但不可访问 */
    DELETED("DELETED", "已删除");

    /** 状态编码 */
    private final String code;

    /** 状态描述 */
    private final String description;

    UserStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 判断当前状态是否允许登录
     *
     * @return true 表示可以登录，false 表示不可以
     */
    public boolean canLogin() {
        return this == ACTIVE;
    }
}