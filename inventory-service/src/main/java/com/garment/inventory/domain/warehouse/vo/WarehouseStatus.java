package com.garment.inventory.domain.warehouse.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 仓库状态
 */
@Getter
public enum WarehouseStatus {

    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    @EnumValue
    @JsonValue
    private final int code;
    private final String desc;

    WarehouseStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WarehouseStatus of(int code) {
        for (WarehouseStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid warehouse status code: " + code);
    }
}