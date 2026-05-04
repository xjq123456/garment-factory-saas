package com.garment.inventory.application.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 入库命令
 */
@Data
public class InboundCommand {

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
     * 款号ID
     */
    private Long styleId;

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
     * 入库数量
     */
    @NotNull(message = "入库数量不能为空")
    @Min(value = 1, message = "入库数量必须大于0")
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