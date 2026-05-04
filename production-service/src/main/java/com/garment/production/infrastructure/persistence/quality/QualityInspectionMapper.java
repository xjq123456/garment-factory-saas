package com.garment.production.infrastructure.persistence.quality;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 质检记录 Mapper
 */
@Mapper
public interface QualityInspectionMapper extends BaseMapper<QualityInspectionDO> {
}