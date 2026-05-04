package com.garment.payment.domain.shared.vo;

import com.garment.common.domain.ValueObject;

/**
 * 支付状态枚举。
 */
public enum PaymentStatus implements ValueObject {

    PENDING("待支付"),
    PROCESSING("处理中"),
    SUCCESS("支付成功"),
    FAILED("支付失败"),
    CANCELLED("已取消"),
    REFUNDED("已退款"),
    PARTIAL_REFUNDED("部分退款");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}