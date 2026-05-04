package com.garment.inventory.application.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 调拨命令
 */
@Data
public class TransferCommand {

    /**
     * 源仓库ID
     */
    @NotNull(message = "源仓库ID不能为空")
    private Long fromWarehouseId;

    /**
     * 目标仓库ID
     */
    @NotNull(message = "目标仓库ID不能为空")
    private Long toWarehouseId;

    /**
     * SKU ID
     */
    @NotNull(message = "SKU ID不能为空")
    private Long skuId;

    /**
     * 调拨数量
     */
    @NotNull(message = "调拨数量不能为空")
    @Min(value = 1, message = "调拨数量必须大于0")
    private Integer quantity;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 备注
     */
    private String remark;
}