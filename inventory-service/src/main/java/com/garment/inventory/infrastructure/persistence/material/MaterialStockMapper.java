package com.garment.inventory.infrastructure.persistence.material;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 原材料库存Mapper
 */
@Mapper
public interface MaterialStockMapper extends BaseMapper<MaterialStockDO> {

    /**
     * 查询低于安全库存的记录
     */
    @Select("SELECT * FROM inv_material_stock WHERE deleted = 0 AND safety_stock > 0 AND total_qty < safety_stock AND tenant_id = #{tenantId}")
    List<MaterialStockDO> selectBelowSafetyStock(@Param("tenantId") Long tenantId);
}