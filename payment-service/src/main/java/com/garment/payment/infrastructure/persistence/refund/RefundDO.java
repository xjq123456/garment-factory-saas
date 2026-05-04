package com.garment.payment.infrastructure.persistence.refund;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.common.infrastructure.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款单持久化对象。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pay_refund")
public class RefundDO extends BaseEntity {

    private String refundNo;
    private Long paymentId;
    private String paymentNo;
    private Long orderId;
    private String orderNo;
    private BigDecimal refundAmount;
    private String reason;
    private String status;
    private String channelRefundNo;
    private LocalDateTime refundedAt;
    private Long operatorId;
    private String remark;
    private String extraInfo;
    private Integer version;
}