package com.garment.production.domain.workstation.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工位类型
 */
@Getter
@AllArgsConstructor
public enum WorkstationType {

    CUTTING("CUTTING", "裁剪"),
    IRONING("IRONING", "熨烫"),
    SEWING("SEWING", "缝制"),
    PACKING("PACKING", "包装"),
    QC("QC", "质检"),
    OTHER("OTHER", "其他");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;
}