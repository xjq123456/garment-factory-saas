package com.garment.production.infrastructure.persistence.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 生产任务 Mapper
 */
@Mapper
public interface ProductionTaskMapper extends BaseMapper<ProductionTaskDO> {
}