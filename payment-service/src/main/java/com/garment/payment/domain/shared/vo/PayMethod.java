package com.garment.payment.domain.shared.vo;

import com.garment.common.domain.ValueObject;

/**
 * 支付方式枚举。
 */
public enum PayMethod {

    WECHAT("微信支付"),
    ALIPAY("支付宝"),
    BANK_TRANSFER("银行转账"),
    OFFLINE("线下支付"),
    CREDIT("信用支付");

    private final String description;

    PayMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}