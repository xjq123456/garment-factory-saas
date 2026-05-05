package com.garment.payment.application.payment;

import com.garment.common.domain.BizException;
import com.garment.common.domain.AuthUserContext;
import com.garment.common.domain.DomainEvent;
import com.garment.payment.application.payment.dto.CreatePaymentCommand;
import com.garment.payment.application.payment.dto.PaymentDTO;
import com.garment.payment.domain.payment.entity.Payment;
import com.garment.payment.domain.payment.event.PaymentCreatedEvent;
import com.garment.payment.domain.payment.repository.PaymentRepository;
import com.garment.payment.domain.shared.vo.PayMethod;
import com.garment.payment.infrastructure.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付应用服务。
 */
@Service
@RequiredArgsConstructor
public class PaymentAppService {

    private final PaymentRepository paymentRepository;
    private final DomainEventPublisher eventPublisher;

    /**
     * 创建支付单。
     */
    @Transactional
    public PaymentDTO createPayment(CreatePaymentCommand cmd) {
        Payment payment = Payment.create(
                AuthUserContext.requireTenantId(),
                cmd.getOrderId(),
                cmd.getOrderNo(),
                cmd.getPayerId(),
                cmd.getPayeeId(),
                cmd.getAmount(),
                PayMethod.valueOf(cmd.getPayMethod()),
                cmd.getChannel(),
                cmd.getRemark()
        );

        Payment saved = paymentRepository.save(payment);
        eventPublisher.publish(new PaymentCreatedEvent(saved.getId(), saved.getPaymentNo().getValue(),
                saved.getOrderId(), saved.getOrderNo(), saved.getPayerId(), saved.getAmount()));
        return PaymentDTO.from(saved);
    }

    /**
     * 根据 ID 查询支付单。
     */
    @Transactional(readOnly = true)
    public PaymentDTO getPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new BizException("支付单不存在: " + id));
        return PaymentDTO.from(payment);
    }

    /**
     * 根据支付单号查询。
     */
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentByNo(String paymentNo) {
        Payment payment = paymentRepository.findByPaymentNo(paymentNo)
                .orElseThrow(() -> new BizException("支付单不存在: " + paymentNo));
        return PaymentDTO.from(payment);
    }

    /**
     * 根据订单 ID 查询支付单。
     */
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BizException("订单关联的支付单不存在: " + orderId));
        return PaymentDTO.from(payment);
    }

    /**
     * 支付成功回调。
     */
    @Transactional
    public PaymentDTO paySuccess(String paymentNo, String channelTradeNo) {
        Payment payment = paymentRepository.findByPaymentNo(paymentNo)
                .orElseThrow(() -> new BizException("支付单不存在: " + paymentNo));
        DomainEvent event = payment.markSuccess(channelTradeNo);
        Payment saved = paymentRepository.save(payment);
        eventPublisher.publish(event);
        return PaymentDTO.from(saved);
    }

    /**
     * 支付失败回调。
     */
    @Transactional
    public PaymentDTO payFailed(String paymentNo) {
        Payment payment = paymentRepository.findByPaymentNo(paymentNo)
                .orElseThrow(() -> new BizException("支付单不存在: " + paymentNo));
        payment.markFailed("支付回调失败");
        Payment saved = paymentRepository.save(payment);
        return PaymentDTO.from(saved);
    }

    /**
     * 取消支付。
     */
    @Transactional
    public PaymentDTO cancelPayment(String paymentNo) {
        Payment payment = paymentRepository.findByPaymentNo(paymentNo)
                .orElseThrow(() -> new BizException("支付单不存在: " + paymentNo));
        payment.cancel();
        Payment saved = paymentRepository.save(payment);
        return PaymentDTO.from(saved);
    }
}
