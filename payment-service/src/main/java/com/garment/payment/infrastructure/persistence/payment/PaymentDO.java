package com.garment.payment.infrastructure.persistence.payment;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.common.infrastructure.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付单持久化对象。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pay_payment")
public class PaymentDO extends BaseEntity {

    private String paymentNo;
    private Long orderId;
    private String orderNo;
    private Long payerId;
    private Long payeeId;
    private BigDecimal amount;
    private String currency;
    private String payMethod;
    private String status;
    private String channel;
    private String channelTradeNo;
    private LocalDateTime paidAt;
    private LocalDateTime expireAt;
    private String remark;
    private String extraInfo;
    private Integer version;
}