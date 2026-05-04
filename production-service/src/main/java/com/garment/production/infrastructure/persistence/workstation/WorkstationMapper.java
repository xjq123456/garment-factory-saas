package com.garment.production.infrastructure.persistence.workstation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工位 Mapper
 */
@Mapper
public interface WorkstationMapper extends BaseMapper<WorkstationDO> {
}