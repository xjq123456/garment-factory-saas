package com.garment.inventory.application.material.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 原材料出库命令
 */
@Data
public class MaterialOutboundCommand {

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
     * 出库数量
     */
    @NotNull(message = "出库数量不能为空")
    @Min(value = 1, message = "出库数量必须大于0")
    private BigDecimal quantity;

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