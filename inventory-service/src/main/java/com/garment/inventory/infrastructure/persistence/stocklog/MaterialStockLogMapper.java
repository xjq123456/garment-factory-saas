package com.garment.inventory.infrastructure.persistence.stocklog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 原材料库存变动日志Mapper
 */
@Mapper
public interface MaterialStockLogMapper extends BaseMapper<MaterialStockLogDO> {
}