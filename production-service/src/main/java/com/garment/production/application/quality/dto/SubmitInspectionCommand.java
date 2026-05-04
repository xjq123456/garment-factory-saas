package com.garment.production.application.quality.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 提交质检结果命令
 */
@Data
public class SubmitInspectionCommand {

    @NotNull(message = "质检ID不能为空")
    private Long inspectionId;

    @NotNull(message = "合格数量不能为空")
    @Min(value = 0, message = "合格数量不能为负")
    private Integer passQty;

    @NotNull(message = "不合格数量不能为空")
    @Min(value = 0, message = "不合格数量不能为负")
    private Integer rejectQty;

    @NotNull(message = "质检结果不能为空")
    private String result;

    private String defectTypes;

    private String defectDesc;

    private Long inspectorId;

    private String inspectorName;

    private String remark;
}