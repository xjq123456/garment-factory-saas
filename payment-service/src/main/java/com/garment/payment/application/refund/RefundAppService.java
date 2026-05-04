package com.garment.payment.application.refund;

import com.garment.common.domain.BizException;
import com.garment.common.domain.TenantContext;
import com.garment.payment.application.refund.dto.CreateRefundCommand;
import com.garment.payment.application.refund.dto.RefundDTO;
import com.garment.payment.domain.payment.entity.Payment;
import com.garment.payment.domain.payment.repository.PaymentRepository;
import com.garment.payment.domain.refund.entity.Refund;
import com.garment.payment.domain.refund.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 退款应用服务。
 */
@Service
@RequiredArgsConstructor
public class RefundAppService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 创建退款单。
     */
    @Transactional
    public RefundDTO createRefund(CreateRefundCommand cmd) {
        Payment payment = paymentRepository.findById(cmd.getPaymentId())
                .orElseThrow(() -> new BizException("支付单不存在: " + cmd.getPaymentId()));

        Refund refund = Refund.create(
                TenantContext.getTenantId(),
                payment.getId(),
                payment.getPaymentNo().getValue(),
                payment.getOrderId(),
                payment.getOrderNo(),
                cmd.getRefundAmount(),
                cmd.getReason(),
                cmd.getOperatorId()
        );
        refund.setRemark(cmd.getRemark());

        Refund saved = refundRepository.save(refund);
        return RefundDTO.from(saved);
    }

    /**
     * 根据 ID 查询退款单。
     */
    @Transactional(readOnly = true)
    public RefundDTO getRefund(Long id) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new BizException("退款单不存在: " + id));
        return RefundDTO.from(refund);
    }

    /**
     * 根据退款单号查询。
     */
    @Transactional(readOnly = true)
    public RefundDTO getRefundByNo(String refundNo) {
        Refund refund = refundRepository.findByRefundNo(refundNo)
                .orElseThrow(() -> new BizException("退款单不存在: " + refundNo));
        return RefundDTO.from(refund);
    }

    /**
     * 根据支付单 ID 查询退款单。
     */
    @Transactional(readOnly = true)
    public RefundDTO getRefundByPaymentId(Long paymentId) {
        Refund refund = refundRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new BizException("支付单关联的退款单不存在: " + paymentId));
        return RefundDTO.from(refund);
    }

    /**
     * 退款成功回调。
     */
    @Transactional
    public RefundDTO refundSuccess(String refundNo, String channelRefundNo) {
        Refund refund = refundRepository.findByRefundNo(refundNo)
                .orElseThrow(() -> new BizException("退款单不存在: " + refundNo));
        refund.markSuccess(channelRefundNo);
        Refund saved = refundRepository.save(refund);
        return RefundDTO.from(saved);
    }

    /**
     * 退款失败回调。
     */
    @Transactional
    public RefundDTO refundFailed(String refundNo, String reason) {
        Refund refund = refundRepository.findByRefundNo(refundNo)
                .orElseThrow(() -> new BizException("退款单不存在: " + refundNo));
        refund.markFailed(reason);
        Refund saved = refundRepository.save(refund);
        return RefundDTO.from(saved);
    }

    /**
     * 标记退款处理中。
     */
    @Transactional
    public RefundDTO markProcessing(String refundNo) {
        Refund refund = refundRepository.findByRefundNo(refundNo)
                .orElseThrow(() -> new BizException("退款单不存在: " + refundNo));
        refund.markProcessing();
        Refund saved = refundRepository.save(refund);
        return RefundDTO.from(saved);
    }
}