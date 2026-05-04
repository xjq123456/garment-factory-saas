package com.garment.payment.infrastructure.persistence.payment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.payment.domain.payment.entity.Payment;
import com.garment.payment.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付单仓储实现。
 */
@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentMapper paymentMapper;

    @Override
    public Payment save(Payment payment) {
        PaymentDO d = PaymentConverter.toDO(payment);
        if (d.getId() == null) {
            paymentMapper.insert(d);
        } else {
            paymentMapper.updateById(d);
        }
        return PaymentConverter.toDomain(d);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        PaymentDO d = paymentMapper.selectById(id);
        return Optional.ofNullable(d).map(PaymentConverter::toDomain);
    }

    @Override
    public Optional<Payment> findByPaymentNo(String paymentNo) {
        PaymentDO d = paymentMapper.selectOne(
                new LambdaQueryWrapper<PaymentDO>().eq(PaymentDO::getPaymentNo, paymentNo));
        return Optional.ofNullable(d).map(PaymentConverter::toDomain);
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        PaymentDO d = paymentMapper.selectOne(
                new LambdaQueryWrapper<PaymentDO>().eq(PaymentDO::getOrderId, orderId));
        return Optional.ofNullable(d).map(PaymentConverter::toDomain);
    }
}