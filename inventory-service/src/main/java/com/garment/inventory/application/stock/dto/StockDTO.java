package com.garment.inventory.application.stock.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存DTO
 */
@Data
public class StockDTO {

    private Long id;

    private Long warehouseId;

    private String warehouseName;

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

    /**
     * 是否低于安全库存
     */
    private Boolean belowSafetyStock;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}