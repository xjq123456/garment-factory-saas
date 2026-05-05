package com.garment.payment.application.refund.dto;

import com.garment.payment.domain.refund.entity.Refund;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款单 DTO。
 */
@Data
public class RefundDTO {

    private Long id;
    private Long tenantId;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RefundDTO from(Refund r) {
        RefundDTO dto = new RefundDTO();
        dto.setId(r.getId());
        dto.setTenantId(r.getTenantId());
        dto.setRefundNo(r.getRefundNo() != null ? r.getRefundNo().getValue() : null);
        dto.setPaymentId(r.getPaymentId());
        dto.setPaymentNo(r.getPaymentNo());
        dto.setOrderId(r.getOrderId());
        dto.setOrderNo(r.getOrderNo());
        dto.setRefundAmount(r.getRefundAmount());
        dto.setReason(r.getReason());
        dto.setStatus(r.getStatus() != null ? r.getStatus().name() : null);
        dto.setChannelRefundNo(r.getChannelRefundNo());
        dto.setRefundedAt(r.getRefundedAt());
        dto.setOperatorId(r.getOperatorId());
        dto.setRemark(r.getRemark());
        return dto;
    }
}