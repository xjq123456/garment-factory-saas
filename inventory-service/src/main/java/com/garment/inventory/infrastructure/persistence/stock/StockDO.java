package com.garment.inventory.infrastructure.persistence.stock;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成品库存持久化对象
 */
@Data
@TableName("inv_stock")
public class StockDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long warehouseId;

    private Long skuId;

    private Long styleId;

    private String styleCode;

    private String color;

    private String size;

    private Integer totalQty;

    private Integer availableQty;

    private Integer lockedQty;

    private Integer safetyStock;

    private String unit;

    private String remark;

    private Long createBy;

    private Long updateBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}