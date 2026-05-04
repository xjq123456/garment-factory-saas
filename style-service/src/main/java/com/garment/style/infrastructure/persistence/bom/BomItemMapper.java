package com.garment.style.infrastructure.persistence.bom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BomItemMapper extends BaseMapper<BomItemDO> {
}
