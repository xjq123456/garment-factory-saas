package com.garment.payment.domain.shared.vo;

import com.garment.common.domain.ValueObject;
import lombok.Getter;

import java.util.UUID;

/**
 * 支付单号值对象。
 */
@Getter
public class PaymentNo implements ValueObject {

    private final String value;

    private PaymentNo(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("支付单号不能为空");
        }
        this.value = value;
    }

    public static PaymentNo of(String value) {
        return new PaymentNo(value);
    }

    /**
     * 生成支付单号：PAY + 时间戳 + 随机串
     */
    public static PaymentNo generate() {
        String no = "PAY" + System.currentTimeMillis()
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return new PaymentNo(no);
    }

    @Override
    public String toString() {
        return value;
    }
}