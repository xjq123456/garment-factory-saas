package com.garment.payment.infrastructure.persistence.refund;

import com.garment.payment.domain.refund.entity.Refund;
import com.garment.payment.domain.refund.entity.Refund.RefundStatus;
import com.garment.payment.domain.shared.vo.PaymentNo;
import com.garment.payment.domain.shared.vo.RefundNo;

/**
 * 退款单 DO ↔ Domain 转换器。
 */
public final class RefundConverter {

    private RefundConverter() {}

    public static RefundDO toDO(Refund domain) {
        if (domain == null) {
            return null;
        }
        RefundDO d = new RefundDO();
        d.setId(domain.getId());
        d.setTenantId(domain.getTenantId());
        d.setRefundNo(domain.getRefundNo() != null ? domain.getRefundNo().getValue() : null);
        d.setPaymentId(domain.getPaymentId());
        d.setPaymentNo(domain.getPaymentNo() != null ? domain.getPaymentNo().getValue() : null);
        d.setOrderId(domain.getOrderId());
        d.setOrderNo(domain.getOrderNo());
        d.setRefundAmount(domain.getRefundAmount());
        d.setReason(domain.getReason());
        d.setStatus(domain.getStatus() != null ? domain.getStatus().name() : null);
        d.setChannelRefundNo(domain.getChannelRefundNo());
        d.setRefundedAt(domain.getRefundedAt());
        d.setOperatorId(domain.getOperatorId());
        d.setRemark(domain.getRemark());
        d.setVersion(domain.getVersion());
        return d;
    }

    public static Refund toDomain(RefundDO d) {
        if (d == null) {
            return null;
        }
        Refund domain = new Refund();
        domain.setId(d.getId());
        domain.setTenantId(d.getTenantId());
        domain.setRefundNo(d.getRefundNo() != null ? RefundNo.of(d.getRefundNo()) : null);
        domain.setPaymentId(d.getPaymentId());
        domain.setPaymentNo(d.getPaymentNo() != null ? PaymentNo.of(d.getPaymentNo()) : null);
        domain.setOrderId(d.getOrderId());
        domain.setOrderNo(d.getOrderNo());
        domain.setRefundAmount(d.getRefundAmount());
        domain.setReason(d.getReason());
        domain.setStatus(d.getStatus() != null ? RefundStatus.valueOf(d.getStatus()) : null);
        domain.setChannelRefundNo(d.getChannelRefundNo());
        domain.setRefundedAt(d.getRefundedAt());
        domain.setOperatorId(d.getOperatorId());
        domain.setRemark(d.getRemark());
        domain.setVersion(d.getVersion());
        return domain;
    }
}