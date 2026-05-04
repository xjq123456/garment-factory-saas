package com.garment.inventory.domain.warehouse.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 仓库类型
 */
@Getter
public enum WarehouseType {

    FINISHED(1, "成品仓"),
    MATERIAL(2, "原材料仓"),
    SEMI_FINISHED(3, "半成品仓");

    @EnumValue
    @JsonValue
    private final int code;
    private final String desc;

    WarehouseType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WarehouseType of(int code) {
        for (WarehouseType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid warehouse type code: " + code);
    }
}