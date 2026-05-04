package com.garment.inventory.application.stock.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存变动日志DTO
 */
@Data
public class StockLogDTO {

    private Long id;

    private Long warehouseId;

    private Long skuId;

    /**
     * 变动类型
     */
    private Integer changeType;

    private String changeTypeDesc;

    /**
     * 变动数量
     */
    private Integer changeQty;

    /**
     * 变动前数量
     */
    private Integer beforeQty;

    /**
     * 变动后数量
     */
    private Integer afterQty;

    private String bizType;

    private String bizNo;

    private Long operatorId;

    private String operatorName;

    private String remark;

    private LocalDateTime createTime;
}