package com.garment.production.application.quality.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建质检记录命令
 */
@Data
public class CreateInspectionCommand {

    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    private Long taskId;

    @NotBlank(message = "质检类型不能为空")
    private String inspectionType;

    @NotNull(message = "检验数量不能为空")
    @Min(value = 1, message = "检验数量必须大于0")
    private Integer inspectQty;

    private String remark;
}