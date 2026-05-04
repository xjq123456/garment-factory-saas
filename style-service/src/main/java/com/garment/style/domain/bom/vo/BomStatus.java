package com.garment.style.domain.bom.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum BomStatus {
    DRAFT(0, "草稿"), CONFIRMED(1, "已确认"), DEPRECATED(2, "已废弃");

    @EnumValue @JsonValue
    private final int code;
    private final String desc;

    BomStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BomStatus fromCode(int code) {
        for (BomStatus s : values()) {
            if (s.code == code) return s;
        }
        throw new IllegalArgumentException("未知的BOM状态: " + code);
    }
}
