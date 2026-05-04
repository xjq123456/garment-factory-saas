package com.garment.production.domain.workstation.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工位状态
 */
@Getter
@AllArgsConstructor
public enum WorkstationStatus {

    IDLE("IDLE", "空闲"),
    BUSY("BUSY", "忙碌"),
    MAINTENANCE("MAINTENANCE", "维护中"),
    DISABLED("DISABLED", "停用");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;
}