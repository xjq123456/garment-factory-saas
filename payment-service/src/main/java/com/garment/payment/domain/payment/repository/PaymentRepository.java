package com.garment.payment.domain.payment.repository;

import com.garment.payment.domain.payment.entity.Payment;

import java.util.Optional;

/**
 * 支付单仓储接口（领域层）。
 */
public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(Long id);

    Optional<Payment> findByPaymentNo(String paymentNo);

    Optional<Payment> findByOrderId(Long orderId);
}