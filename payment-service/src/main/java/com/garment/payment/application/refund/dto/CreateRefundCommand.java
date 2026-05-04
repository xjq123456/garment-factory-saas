package com.garment.payment.application.refund.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建退款单命令。
 */
@Data
public class CreateRefundCommand {

    @NotNull(message = "支付单 ID 不能为空")
    private Long paymentId;

    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0.01", message = "退款金额必须大于 0")
    private BigDecimal refundAmount;

    @NotBlank(message = "退款原因不能为空")
    private String reason;

    private Long operatorId;
    private String remark;
}