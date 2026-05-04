package com.garment.inventory.infrastructure.persistence.stocklog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 成品库存变动日志Mapper
 */
@Mapper
public interface StockLogMapper extends BaseMapper<StockLogDO> {
}