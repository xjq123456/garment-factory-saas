package com.garment.inventory.infrastructure.persistence.material;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 原材料库存持久化对象
 */
@Data
@TableName("inv_material_stock")
public class MaterialStockDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long warehouseId;

    private Long materialId;

    private String materialCode;

    private String materialName;

    private Integer materialType;

    private BigDecimal totalQty;

    private BigDecimal availableQty;

    private BigDecimal lockedQty;

    private BigDecimal safetyStock;

    private String unit;

    private String batchNo;

    private String remark;

    private Long createBy;

    private Long updateBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}