package com.garment.production.application.process.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工艺路线视图对象
 */
@Data
public class ProcessRouteVO {

    private Long id;
    private String routeCode;
    private String routeName;
    private Long styleId;
    private String description;
    private String status;
    private List<ProcessStepVO> steps;
    private String createBy;
    private LocalDateTime createTime;

    @Data
    public static class ProcessStepVO {
        private Long id;
        private Integer stepNo;
        private String stepName;
        private String stepType;
        private BigDecimal standardTime;
        private Integer standardOutput;
        private String description;
    }
}