package com.garment.rbac.domain.shared.vo;

import lombok.Getter;

/**
 * 菜单ID值对象
 */
@Getter
public class MenuId {

    private final Long value;

    private MenuId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("菜单ID无效");
        }
        this.value = value;
    }

    public static MenuId of(Long value) {
        return new MenuId(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}