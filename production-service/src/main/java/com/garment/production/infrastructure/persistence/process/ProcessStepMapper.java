package com.garment.production.infrastructure.persistence.process;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工序步骤 Mapper
 */
@Mapper
public interface ProcessStepMapper extends BaseMapper<ProcessStepDO> {
}