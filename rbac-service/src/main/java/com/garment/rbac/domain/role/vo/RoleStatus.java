package com.garment.rbac.domain.role.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 角色状态枚举
 */
@Getter
public enum RoleStatus {

    ENABLED(1, "启用"),
    DISABLED(0, "禁用");

    @EnumValue
    @JsonValue
    private final int code;
    private final String desc;

    RoleStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}