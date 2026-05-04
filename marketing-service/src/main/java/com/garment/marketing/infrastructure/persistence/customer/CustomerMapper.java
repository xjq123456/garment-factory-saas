package com.garment.marketing.infrastructure.persistence.customer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户 MyBatis Plus Mapper。
 */
@Mapper
public interface CustomerMapper extends BaseMapper<CustomerDO> {
}
