package com.garment.inventory.application.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 出库命令
 */
@Data
public class OutboundCommand {

    /**
     * 仓库ID
     */
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    /**
     * SKU ID
     */
    @NotNull(message = "SKU ID不能为空")
    private Long skuId;

    /**
     * 出库数量
     */
    @NotNull(message = "出库数量不能为空")
    @Min(value = 1, message = "出库数量必须大于0")
    private Integer quantity;

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