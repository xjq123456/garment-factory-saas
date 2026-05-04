package com.garment.inventory.infrastructure.persistence.stock;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 成品库存Mapper
 */
@Mapper
public interface StockMapper extends BaseMapper<StockDO> {

    /**
     * 查询低于安全库存的记录
     */
    @Select("SELECT * FROM inv_stock WHERE deleted = 0 AND safety_stock > 0 AND total_qty < safety_stock AND tenant_id = #{tenantId}")
    List<StockDO> selectBelowSafetyStock(@Param("tenantId") Long tenantId);
}