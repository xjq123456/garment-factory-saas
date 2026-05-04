package com.garment.marketing.domain.customer.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户类型枚举。
 */
@Getter
@AllArgsConstructor
public enum CustomerType {

    PERSONAL(0, "个人"),
    ENTERPRISE(1, "企业"),
    FACTORY_PARTNER(2, "工厂加盟商");

    private final int code;
    private final String desc;
}
