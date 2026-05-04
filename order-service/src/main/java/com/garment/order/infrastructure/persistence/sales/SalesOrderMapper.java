package com.garment.order.infrastructure.persistence.sales;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrderDO> {
}