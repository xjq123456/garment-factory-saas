package com.garment.payment.infrastructure.persistence.refund;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款单 MyBatis-Plus Mapper。
 */
@Mapper
public interface RefundMapper extends BaseMapper<RefundDO> {
}