package com.garment.inventory.application.material.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 原材料库存DTO
 */
@Data
public class MaterialStockDTO {

    private Long id;

    private Long warehouseId;

    private String warehouseName;

    private Long materialId;

    private String materialCode;

    private String materialName;

    private Integer materialType;

    private String materialTypeDesc;

    private BigDecimal totalQty;

    private BigDecimal availableQty;

    private BigDecimal lockedQty;

    private BigDecimal safetyStock;

    private String unit;

    private String batchNo;

    /**
     * 是否低于安全库存
     */
    private Boolean belowSafetyStock;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}