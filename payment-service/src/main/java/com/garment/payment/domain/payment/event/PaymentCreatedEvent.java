package com.garment.payment.domain.payment.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentCreatedEvent implements DomainEvent {
    private final Long paymentId;
    private final String paymentNo;
    private final Long orderId;
    private final String orderNo;
    private final Long payerId;
    private final BigDecimal amount;

    public PaymentCreatedEvent(Long paymentId, String paymentNo, Long orderId,
                               String orderNo, Long payerId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.paymentNo = paymentNo;
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.payerId = payerId;
        this.amount = amount;
    }
}