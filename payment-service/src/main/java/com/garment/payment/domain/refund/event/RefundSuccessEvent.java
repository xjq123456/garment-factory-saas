package com.garment.payment.domain.refund.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 退款成功事件。
 */
@Getter
public class RefundSuccessEvent extends DomainEvent {

    private final Long refundId;
    private final String refundNo;
    private final Long paymentId;
    private final String paymentNo;
    private final Long orderId;
    private final BigDecimal refundAmount;

    public RefundSuccessEvent(Long refundId, String refundNo, Long paymentId,
                              String paymentNo, Long orderId, BigDecimal refundAmount) {
        super("payment-service");
        this.refundId = refundId;
        this.refundNo = refundNo;
        this.paymentId = paymentId;
        this.paymentNo = paymentNo;
        this.orderId = orderId;
        this.refundAmount = refundAmount;
    }
}