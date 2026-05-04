package com.garment.order.domain.sales.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum SalesOrderStatus {
    DRAFT("DRAFT", "草稿"),
    CONFIRMED("CONFIRMED", "已确认"),
    SHIPPED("SHIPPED", "已发货"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELLED("CANCELLED", "已取消");

    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;
}