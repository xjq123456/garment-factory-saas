package com.garment.payment.domain.payment.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.common.domain.BizException;
import com.garment.common.domain.DomainEvent;
import com.garment.payment.domain.shared.vo.PayMethod;
import com.garment.payment.domain.shared.vo.PaymentNo;
import com.garment.payment.domain.shared.vo.PaymentStatus;
import com.garment.payment.domain.payment.event.PaymentCreatedEvent;
import com.garment.payment.domain.payment.event.PaymentSuccessEvent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付单聚合根。
 */
@Getter
@Setter
public class Payment extends AggregateRoot {

    private PaymentNo paymentNo;
    private Long orderId;
    private String orderNo;
    private Long payerId;
    private Long payeeId;
    private BigDecimal amount;
    private String currency;
    private PayMethod payMethod;
    private PaymentStatus status;
    private String channel;
    private String channelTradeNo;
    private LocalDateTime paidAt;
    private LocalDateTime expireAt;
    private String remark;

    public Payment() {}

    /**
     * 创建支付单（工厂方法）。
     * <p>
     * 返回 {@link PaymentCreatedEvent}，由应用层负责发布。
     */
    public static Payment create(Long tenantId, Long orderId, String orderNo,
                                 Long payerId, Long payeeId, BigDecimal amount,
                                 PayMethod payMethod, String channel, String remark) {
        Payment p = new Payment();
        p.tenantId = tenantId;
        p.paymentNo = PaymentNo.generate();
        p.orderId = orderId;
        p.orderNo = orderNo;
        p.payerId = payerId;
        p.payeeId = payeeId;
        p.amount = amount;
        p.currency = "CNY";
        p.payMethod = payMethod;
        p.status = PaymentStatus.PENDING;
        p.channel = channel;
        p.expireAt = LocalDateTime.now().plusHours(2);
        p.remark = remark;
        p.version = 0;

        return p;
    }

    /**
     * 标记支付成功，返回领域事件。
     */
    public DomainEvent markSuccess(String channelTradeNo) {
        if (this.status != PaymentStatus.PENDING && this.status != PaymentStatus.PROCESSING) {
            throw new BizException("当前状态不允许标记为成功");
        }
        this.status = PaymentStatus.SUCCESS;
        this.channelTradeNo = channelTradeNo;
        this.paidAt = LocalDateTime.now();
        return new PaymentSuccessEvent(this.id, this.paymentNo.getValue(),
                this.orderId, this.orderNo, this.payerId, this.amount);
    }

    /**
     * 标记支付失败。
     */
    public void markFailed(String reason) {
        if (this.status != PaymentStatus.PENDING && this.status != PaymentStatus.PROCESSING) {
            throw new BizException("当前状态不允许标记为失败");
        }
        this.status = PaymentStatus.FAILED;
        this.remark = reason;
    }

    /**
     * 取消支付。
     */
    public void cancel() {
        if (this.status != PaymentStatus.PENDING) {
            throw new BizException("只有待支付状态可以取消");
        }
        this.status = PaymentStatus.CANCELLED;
    }

    /**
     * 标记为处理中。
     */
    public void markProcessing() {
        if (this.status != PaymentStatus.PENDING) {
            throw new BizException("只有待支付状态可以转为处理中");
        }
        this.status = PaymentStatus.PROCESSING;
    }

    /**
     * 标记已退款。
     */
    public void markRefunded() {
        this.status = PaymentStatus.REFUNDED;
    }

    /**
     * 判断是否已过期。
     */
    public boolean isExpired() {
        return this.expireAt != null && LocalDateTime.now().isAfter(this.expireAt);
    }
}
