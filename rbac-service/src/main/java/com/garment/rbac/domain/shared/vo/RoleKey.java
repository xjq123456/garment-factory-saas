package com.garment.rbac.domain.shared.vo;

import lombok.Getter;

/**
 * 角色标识值对象
 */
@Getter
public class RoleKey {

    private final String value;

    private RoleKey(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("角色标识不能为空");
        }
        if (value.length() > 128) {
            throw new IllegalArgumentException("角色标识长度不能超过128个字符");
        }
        this.value = value;
    }

    public static RoleKey of(String value) {
        return new RoleKey(value);
    }

    @Override
    public String toString() {
        return value;
    }
}