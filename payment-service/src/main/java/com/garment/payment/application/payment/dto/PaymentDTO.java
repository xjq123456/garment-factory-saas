package com.garment.payment.application.payment.dto;

import com.garment.payment.domain.payment.entity.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付单 DTO。
 */
@Data
public class PaymentDTO {

    private Long id;
    private Long tenantId;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentDTO from(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(p.getId());
        dto.setTenantId(p.getTenantId());
        dto.setPaymentNo(p.getPaymentNo() != null ? p.getPaymentNo().getValue() : null);
        dto.setOrderId(p.getOrderId());
        dto.setOrderNo(p.getOrderNo());
        dto.setPayerId(p.getPayerId());
        dto.setPayeeId(p.getPayeeId());
        dto.setAmount(p.getAmount());
        dto.setCurrency(p.getCurrency());
        dto.setPayMethod(p.getPayMethod() != null ? p.getPayMethod().name() : null);
        dto.setStatus(p.getStatus() != null ? p.getStatus().name() : null);
        dto.setChannel(p.getChannel());
        dto.setChannelTradeNo(p.getChannelTradeNo());
        dto.setPaidAt(p.getPaidAt());
        dto.setExpireAt(p.getExpireAt());
        dto.setRemark(p.getRemark());
        return dto;
    }
}