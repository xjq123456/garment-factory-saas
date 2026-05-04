package com.garment.payment.domain.refund.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.common.domain.BizException;
import com.garment.payment.domain.refund.event.RefundCreatedEvent;
import com.garment.payment.domain.refund.event.RefundSuccessEvent;
import com.garment.payment.domain.shared.vo.RefundNo;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款单聚合根。
 */
@Getter
public class Refund extends AggregateRoot {

    private Long id;
    private Long tenantId;
    private RefundNo refundNo;
    private Long paymentId;
    private String paymentNo;
    private Long orderId;
    private String orderNo;
    private BigDecimal refundAmount;
    private String reason;
    private RefundStatus status;
    private String channelRefundNo;
    private LocalDateTime refundedAt;
    private Long operatorId;
    private String remark;
    private Integer version;

    protected Refund() {}

    /**
     * 创建退款单（工厂方法）。
     */
    public static Refund create(Long tenantId, Long paymentId, String paymentNo,
                                Long orderId, String orderNo, BigDecimal refundAmount,
                                String reason, Long operatorId) {
        Refund r = new Refund();
        r.id = nextId();
        r.tenantId = tenantId;
        r.refundNo = RefundNo.generate();
        r.paymentId = paymentId;
        r.paymentNo = paymentNo;
        r.orderId = orderId;
        r.orderNo = orderNo;
        r.refundAmount = refundAmount;
        r.reason = reason;
        r.status = RefundStatus.PENDING;
        r.operatorId = operatorId;
        r.version = 0;

        r.addEvent(new RefundCreatedEvent(r.id, r.refundNo.getValue(), r.paymentId,
                r.paymentNo, r.orderId, r.refundAmount));
        return r;
    }

    /**
     * 标记退款成功。
     */
    public void markSuccess(String channelRefundNo) {
        if (this.status != RefundStatus.PENDING && this.status != RefundStatus.PROCESSING) {
            throw new BizException("当前状态不允许标记为退款成功");
        }
        this.status = RefundStatus.SUCCESS;
        this.channelRefundNo = channelRefundNo;
        this.refundedAt = LocalDateTime.now();
        this.addEvent(new RefundSuccessEvent(this.id, this.refundNo.getValue(),
                this.paymentId, this.paymentNo, this.orderId, this.refundAmount));
    }

    /**
     * 标记退款失败。
     */
    public void markFailed(String reason) {
        if (this.status != RefundStatus.PENDING && this.status != RefundStatus.PROCESSING) {
            throw new BizException("当前状态不允许标记为退款失败");
        }
        this.status = RefundStatus.FAILED;
        this.remark = reason;
    }

    /**
     * 标记为处理中。
     */
    public void markProcessing() {
        if (this.status != RefundStatus.PENDING) {
            throw new BizException("只有待退款状态可以转为处理中");
        }
        this.status = RefundStatus.PROCESSING;
    }

    /**
     * 退款状态枚举。
     */
    public enum RefundStatus {
        PENDING, PROCESSING, SUCCESS, FAILED
    }
}