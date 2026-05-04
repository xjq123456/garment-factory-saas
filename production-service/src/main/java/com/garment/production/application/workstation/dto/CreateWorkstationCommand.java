package com.garment.production.application.workstation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建工位命令
 */
@Data
public class CreateWorkstationCommand {

    @NotBlank(message = "工位编号不能为空")
    private String stationCode;

    @NotBlank(message = "工位名称不能为空")
    private String stationName;

    private String workshop;

    private String productionLine;

    @NotBlank(message = "工位类型不能为空")
    private String stationType;

    private String equipmentCode;

    private String remark;
}