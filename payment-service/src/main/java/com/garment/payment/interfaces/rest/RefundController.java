package com.garment.payment.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.payment.application.refund.RefundAppService;
import com.garment.payment.application.refund.dto.CreateRefundCommand;
import com.garment.payment.application.refund.dto.RefundDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 退款 REST 控制器。
 */
@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundAppService refundAppService;

    /**
     * 创建退款单。
     */
    @PostMapping
    public R<RefundDTO> createRefund(@Valid @RequestBody CreateRefundCommand cmd) {
        return R.ok(refundAppService.createRefund(cmd));
    }

    /**
     * 根据 ID 查询退款单。
     */
    @GetMapping("/{id}")
    public R<RefundDTO> getRefund(@PathVariable Long id) {
        return R.ok(refundAppService.getRefund(id));
    }

    /**
     * 根据退款单号查询。
     */
    @GetMapping("/by-no/{refundNo}")
    public R<RefundDTO> getRefundByNo(@PathVariable String refundNo) {
        return R.ok(refundAppService.getRefundByNo(refundNo));
    }

    /**
     * 根据支付单 ID 查询退款单。
     */
    @GetMapping("/by-payment/{paymentId}")
    public R<RefundDTO> getRefundByPaymentId(@PathVariable Long paymentId) {
        return R.ok(refundAppService.getRefundByPaymentId(paymentId));
    }

    /**
     * 退款成功回调。
     */
    @PostMapping("/{refundNo}/success")
    public R<RefundDTO> refundSuccess(@PathVariable String refundNo,
                                      @RequestParam String channelRefundNo) {
        return R.ok(refundAppService.refundSuccess(refundNo, channelRefundNo));
    }

    /**
     * 退款失败回调。
     */
    @PostMapping("/{refundNo}/fail")
    public R<RefundDTO> refundFailed(@PathVariable String refundNo,
                                     @RequestParam String reason) {
        return R.ok(refundAppService.refundFailed(refundNo, reason));
    }

    /**
     * 标记退款处理中。
     */
    @PostMapping("/{refundNo}/processing")
    public R<RefundDTO> markProcessing(@PathVariable String refundNo) {
        return R.ok(refundAppService.markProcessing(refundNo));
    }
}