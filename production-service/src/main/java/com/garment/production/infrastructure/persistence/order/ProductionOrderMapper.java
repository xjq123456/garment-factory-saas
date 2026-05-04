package com.garment.production.infrastructure.persistence.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 生产工单 Mapper
 */
@Mapper
public interface ProductionOrderMapper extends BaseMapper<ProductionOrderDO> {
}