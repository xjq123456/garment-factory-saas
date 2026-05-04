package com.garment.payment.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.payment.application.payment.PaymentAppService;
import com.garment.payment.application.payment.dto.CreatePaymentCommand;
import com.garment.payment.application.payment.dto.PaymentDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 支付 REST 控制器。
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentAppService paymentAppService;

    /**
     * 创建支付单。
     */
    @PostMapping
    public R<PaymentDTO> createPayment(@Valid @RequestBody CreatePaymentCommand cmd) {
        return R.ok(paymentAppService.createPayment(cmd));
    }

    /**
     * 根据 ID 查询支付单。
     */
    @GetMapping("/{id}")
    public R<PaymentDTO> getPayment(@PathVariable Long id) {
        return R.ok(paymentAppService.getPayment(id));
    }

    /**
     * 根据支付单号查询。
     */
    @GetMapping("/by-no/{paymentNo}")
    public R<PaymentDTO> getPaymentByNo(@PathVariable String paymentNo) {
        return R.ok(paymentAppService.getPaymentByNo(paymentNo));
    }

    /**
     * 根据订单 ID 查询支付单。
     */
    @GetMapping("/by-order/{orderId}")
    public R<PaymentDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        return R.ok(paymentAppService.getPaymentByOrderId(orderId));
    }

    /**
     * 支付成功回调。
     */
    @PostMapping("/{paymentNo}/success")
    public R<PaymentDTO> paySuccess(@PathVariable String paymentNo,
                                    @RequestParam String channelTradeNo) {
        return R.ok(paymentAppService.paySuccess(paymentNo, channelTradeNo));
    }

    /**
     * 支付失败回调。
     */
    @PostMapping("/{paymentNo}/fail")
    public R<PaymentDTO> payFailed(@PathVariable String paymentNo) {
        return R.ok(paymentAppService.payFailed(paymentNo));
    }

    /**
     * 取消支付。
     */
    @PostMapping("/{paymentNo}/cancel")
    public R<PaymentDTO> cancelPayment(@PathVariable String paymentNo) {
        return R.ok(paymentAppService.cancelPayment(paymentNo));
    }
}