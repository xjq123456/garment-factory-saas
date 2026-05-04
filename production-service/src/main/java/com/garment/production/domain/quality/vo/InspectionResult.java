package com.garment.production.domain.quality.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 质检结果
 */
@Getter
@AllArgsConstructor
public enum InspectionResult {

    PENDING("PENDING", "待检"),
    PASS("PASS", "合格"),
    REJECT("REJECT", "不合格"),
    CONDITIONAL("CONDITIONAL", "让步接收");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;
}