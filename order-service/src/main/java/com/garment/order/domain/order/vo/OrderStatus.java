package com.garment.order.domain.order.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    DRAFT("DRAFT", "草稿"),
    CONFIRMED("CONFIRMED", "已确认"),
    IN_PRODUCTION("IN_PRODUCTION", "生产中"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELLED("CANCELLED", "已取消");

    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;
}
