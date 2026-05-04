package com.garment.search.domain.index.model;

import lombok.Getter;

/**
 * 索引类型枚举：定义服装厂SaaS系统中的搜索索引分类。
 */
@Getter
public enum IndexType {

    STYLE("style", "款式"),
    SKU("sku", "SKU"),
    ORDER("order", "订单"),
    INVENTORY("inventory", "库存"),
    CUSTOMER("customer", "客户"),
    PRODUCTION("production", "生产单"),
    MATERIAL("material", "面辅料");

    private final String code;
    private final String description;

    IndexType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static IndexType fromCode(String code) {
        for (IndexType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的索引类型: " + code);
    }
}