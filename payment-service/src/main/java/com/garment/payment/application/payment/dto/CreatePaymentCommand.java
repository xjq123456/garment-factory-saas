package com.garment.payment.application.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建支付单命令。
 */
@Data
public class CreatePaymentCommand {

    @NotNull(message = "订单 ID 不能为空")
    private Long orderId;

    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @NotNull(message = "付款方 ID 不能为空")
    private Long payerId;

    private Long payeeId;

    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于 0")
    private BigDecimal amount;

    private String currency = "CNY";

    @NotBlank(message = "支付方式不能为空")
    private String payMethod;

    private String channel;

    private String remark;
}