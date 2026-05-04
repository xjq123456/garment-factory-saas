package com.garment.inventory.infrastructure.persistence.warehouse;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库Mapper
 */
@Mapper
public interface WarehouseMapper extends BaseMapper<WarehouseDO> {
}