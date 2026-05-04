package com.garment.payment.infrastructure.persistence.payment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付单 MyBatis-Plus Mapper。
 */
@Mapper
public interface PaymentMapper extends BaseMapper<PaymentDO> {
}