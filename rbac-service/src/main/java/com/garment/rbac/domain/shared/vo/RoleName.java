package com.garment.rbac.domain.shared.vo;

import lombok.Getter;

/**
 * 角色名称值对象
 */
@Getter
public class RoleName {

    private final String value;

    private RoleName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("角色名称不能为空");
        }
        if (value.length() > 64) {
            throw new IllegalArgumentException("角色名称长度不能超过64个字符");
        }
        this.value = value;
    }

    public static RoleName of(String value) {
        return new RoleName(value);
    }

    @Override
    public String toString() {
        return value;
    }
}