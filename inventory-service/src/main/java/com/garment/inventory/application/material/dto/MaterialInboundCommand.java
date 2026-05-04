package com.garment.inventory.application.material.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 原材料入库命令
 */
@Data
public class MaterialInboundCommand {

    /**
     * 仓库ID
     */
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    /**
     * 原材料ID
     */
    @NotNull(message = "原材料ID不能为空")
    private Long materialId;

    /**
     * 原材料编码
     */
    private String materialCode;

    /**
     * 原材料名称
     */
    private String materialName;

    /**
     * 原材料类型: 1-面料 2-辅料 3-包装材料
     */
    private Integer materialType;

    /**
     * 入库数量
     */
    @NotNull(message = "入库数量不能为空")
    @Min(value = 1, message = "入库数量必须大于0")
    private BigDecimal quantity;

    /**
     * 单位
     */
    @NotBlank(message = "单位不能为空")
    private String unit;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 备注
     */
    private String remark;
}