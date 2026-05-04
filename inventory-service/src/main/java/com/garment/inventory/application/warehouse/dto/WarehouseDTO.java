package com.garment.inventory.application.warehouse.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 仓库DTO
 */
@Data
public class WarehouseDTO {

    private Long id;

    private String warehouseCode;

    private String warehouseName;

    private Integer warehouseType;

    private String warehouseTypeDesc;

    private String contactPerson;

    private String contactPhone;

    private String address;

    private Integer status;

    private String statusDesc;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}