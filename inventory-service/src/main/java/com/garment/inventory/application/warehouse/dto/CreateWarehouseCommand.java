package com.garment.inventory.application.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建仓库命令
 */
@Data
public class CreateWarehouseCommand {

    /**
     * 仓库编码
     */
    @NotBlank(message = "仓库编码不能为空")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @NotBlank(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 仓库类型: 1-成品仓 2-原材料仓 3-半成品仓
     */
    @NotNull(message = "仓库类型不能为空")
    private Integer warehouseType;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 仓库地址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;
}