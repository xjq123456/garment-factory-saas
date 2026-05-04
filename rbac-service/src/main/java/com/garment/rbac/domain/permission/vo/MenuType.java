package com.garment.rbac.domain.permission.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 菜单类型枚举
 */
@Getter
public enum MenuType {

    CATALOG(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮/权限");

    @EnumValue
    @JsonValue
    private final int code;
    private final String desc;

    MenuType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据 code 查找枚举
     */
    public static MenuType fromCode(int code) {
        for (MenuType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的菜单类型: " + code);
    }
}
