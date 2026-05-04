package com.garment.inventory.infrastructure.persistence.stocklog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 原材料库存变动日志持久化对象
 */
@Data
@TableName("inv_material_stock_log")
public class MaterialStockLogDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long warehouseId;

    private Long materialId;

    private Integer changeType;

    private BigDecimal changeQty;

    private BigDecimal beforeQty;

    private BigDecimal afterQty;

    private String bizType;

    private String bizNo;

    private String batchNo;

    private Long operatorId;

    private String operatorName;

    private String remark;

    private LocalDateTime createTime;
}