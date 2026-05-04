package com.garment.payment.infrastructure.persistence.payment;

import com.garment.payment.domain.payment.entity.Payment;
import com.garment.payment.domain.shared.vo.PayMethod;
import com.garment.payment.domain.shared.vo.PaymentNo;
import com.garment.payment.domain.shared.vo.PaymentStatus;

/**
 * 支付单 DO ↔ Domain 转换器。
 */
public final class PaymentConverter {

    private PaymentConverter() {}

    public static PaymentDO toDO(Payment domain) {
        if (domain == null) {
            return null;
        }
        PaymentDO d = new PaymentDO();
        d.setId(domain.getId());
        d.setTenantId(domain.getTenantId());
        d.setPaymentNo(domain.getPaymentNo() != null ? domain.getPaymentNo().getValue() : null);
        d.setOrderId(domain.getOrderId());
        d.setOrderNo(domain.getOrderNo());
        d.setPayerId(domain.getPayerId());
        d.setPayeeId(domain.getPayeeId());
        d.setAmount(domain.getAmount());
        d.setCurrency(domain.getCurrency());
        d.setPayMethod(domain.getPayMethod() != null ? domain.getPayMethod().name() : null);
        d.setStatus(domain.getStatus() != null ? domain.getStatus().name() : null);
        d.setChannel(domain.getChannel());
        d.setChannelTradeNo(domain.getChannelTradeNo());
        d.setPaidAt(domain.getPaidAt());
        d.setExpireAt(domain.getExpireAt());
        d.setRemark(domain.getRemark());
        d.setVersion(domain.getVersion());
        return d;
    }

    public static Payment toDomain(PaymentDO d) {
        if (d == null) {
            return null;
        }
        Payment domain = new Payment();
        domain.setId(d.getId());
        domain.setTenantId(d.getTenantId());
        domain.setPaymentNo(d.getPaymentNo() != null ? PaymentNo.of(d.getPaymentNo()) : null);
        domain.setOrderId(d.getOrderId());
        domain.setOrderNo(d.getOrderNo());
        domain.setPayerId(d.getPayerId());
        domain.setPayeeId(d.getPayeeId());
        domain.setAmount(d.getAmount());
        domain.setCurrency(d.getCurrency());
        domain.setPayMethod(d.getPayMethod() != null ? PayMethod.valueOf(d.getPayMethod()) : null);
        domain.setStatus(d.getStatus() != null ? PaymentStatus.valueOf(d.getStatus()) : null);
        domain.setChannel(d.getChannel());
        domain.setChannelTradeNo(d.getChannelTradeNo());
        domain.setPaidAt(d.getPaidAt());
        domain.setExpireAt(d.getExpireAt());
        domain.setRemark(d.getRemark());
        domain.setVersion(d.getVersion());
        return domain;
    }
}