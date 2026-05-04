package com.garment.marketing.domain.customer.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户等级枚举。
 */
@Getter
@AllArgsConstructor
public enum CustomerLevel {

    NORMAL(0, "普通"),
    SILVER(1, "银牌"),
    GOLD(2, "金牌"),
    DIAMOND(3, "钻石");

    private final int code;
    private final String desc;
}
