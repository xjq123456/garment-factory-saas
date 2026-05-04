package com.garment.payment.domain.refund.repository;

import com.garment.payment.domain.refund.entity.Refund;

import java.util.Optional;

/**
 * 退款单仓储接口（领域层）。
 */
public interface RefundRepository {

    Refund save(Refund refund);

    Optional<Refund> findById(Long id);

    Optional<Refund> findByRefundNo(String refundNo);

    Optional<Refund> findByPaymentId(Long paymentId);
}