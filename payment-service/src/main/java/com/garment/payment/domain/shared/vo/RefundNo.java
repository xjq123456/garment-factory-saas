package com.garment.payment.domain.shared.vo;

import com.garment.common.domain.ValueObject;
import lombok.Getter;

import java.util.UUID;

/**
 * 退款单号值对象。
 */
@Getter
public class RefundNo extends ValueObject {

    private final String value;

    private RefundNo(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("退款单号不能为空");
        }
        this.value = value;
    }

    public static RefundNo of(String value) {
        return new RefundNo(value);
    }

    public static RefundNo generate() {
        String no = "REF" + System.currentTimeMillis()
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return new RefundNo(no);
    }

    @Override
    public String toString() {
        return value;
    }
}