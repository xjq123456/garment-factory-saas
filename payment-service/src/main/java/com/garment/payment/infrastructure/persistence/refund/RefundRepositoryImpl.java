package com.garment.payment.infrastructure.persistence.refund;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.payment.domain.refund.entity.Refund;
import com.garment.payment.domain.refund.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 退款单仓储实现。
 */
@Repository
@RequiredArgsConstructor
public class RefundRepositoryImpl implements RefundRepository {

    private final RefundMapper refundMapper;

    @Override
    public Refund save(Refund refund) {
        RefundDO d = RefundConverter.toDO(refund);
        if (d.getId() == null) {
            refundMapper.insert(d);
        } else {
            refundMapper.updateById(d);
        }
        return RefundConverter.toDomain(d);
    }

    @Override
    public Optional<Refund> findById(Long id) {
        RefundDO d = refundMapper.selectById(id);
        return Optional.ofNullable(d).map(RefundConverter::toDomain);
    }

    @Override
    public Optional<Refund> findByRefundNo(String refundNo) {
        RefundDO d = refundMapper.selectOne(
                new LambdaQueryWrapper<RefundDO>().eq(RefundDO::getRefundNo, refundNo));
        return Optional.ofNullable(d).map(RefundConverter::toDomain);
    }

    @Override
    public Optional<Refund> findByPaymentId(Long paymentId) {
        RefundDO d = refundMapper.selectOne(
                new LambdaQueryWrapper<RefundDO>().eq(RefundDO::getPaymentId, paymentId));
        return Optional.ofNullable(d).map(RefundConverter::toDomain);
    }
}