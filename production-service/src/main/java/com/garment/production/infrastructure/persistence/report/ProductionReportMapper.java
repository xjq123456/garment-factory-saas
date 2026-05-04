package com.garment.production.infrastructure.persistence.report;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报工记录 Mapper
 */
@Mapper
public interface ProductionReportMapper extends BaseMapper<ProductionReportDO> {
}