package com.garment.production.application.process.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 创建工艺路线命令
 */
@Data
public class CreateProcessRouteCommand {

    @NotBlank(message = "路线编号不能为空")
    private String routeCode;

    @NotBlank(message = "路线名称不能为空")
    private String routeName;

    private Long styleId;

    private String description;

    /** 工序步骤列表 */
    private List<ProcessStepDTO> steps;

    @Data
    public static class ProcessStepDTO {
        @NotBlank(message = "工序名称不能为空")
        private String stepName;

        @NotBlank(message = "工序类型不能为空")
        private String stepType;

        private java.math.BigDecimal standardTime;

        private Integer standardOutput;

        private String description;
    }
}