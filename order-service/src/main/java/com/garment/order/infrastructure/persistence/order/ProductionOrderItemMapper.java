package com.garment.order.infrastructure.persistence.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductionOrderItemMapper extends BaseMapper<ProductionOrderItemDO> {
}
