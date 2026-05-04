package com.garment.inventory.domain.material.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 原材料类型
 */
@Getter
public enum MaterialType {

    FABRIC(1, "面料"),
    ACCESSORY(2, "辅料"),
    PACKAGING(3, "包装材料");

    @EnumValue
    @JsonValue
    private final int code;
    private final String desc;

    MaterialType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MaterialType of(int code) {
        for (MaterialType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid material type code: " + code);
    }
}