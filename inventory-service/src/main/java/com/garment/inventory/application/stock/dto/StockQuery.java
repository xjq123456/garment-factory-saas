package com.garment.inventory.application.stock.dto;

import lombok.Data;

/**
 * 库存查询参数
 */
@Data
public class StockQuery {

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 款号编码
     */
    private String styleCode;

    /**
     * 颜色
     */
    private String color;

    /**
     * 尺码
     */
    private String size;

    /**
     * 是否只查询低于安全库存的
     */
    private Boolean belowSafetyStock;
}