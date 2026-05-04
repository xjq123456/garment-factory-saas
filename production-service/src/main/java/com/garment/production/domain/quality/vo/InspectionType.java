package com.garment.production.domain.quality.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 质检类型
 */
@Getter
@AllArgsConstructor
public enum InspectionType {

    INLINE("INLINE", "巡检"),
    FINAL("FINAL", "终检"),
    INCOMING("INCOMING", "来料检");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;
}