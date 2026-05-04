package com.garment.production.application.quality.dto;

import com.garment.production.domain.quality.vo.InspectionResult;
import com.garment.production.domain.quality.vo.InspectionType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质检记录视图对象
 */
@Data
public class QualityInspectionVO {

    private Long id;
    private String inspectionNo;
    private Long orderId;
    private Long taskId;
    private InspectionType inspectionType;
    private Integer inspectQty;
    private Integer passQty;
    private Integer rejectQty;
    private String defectTypes;
    private String defectDesc;
    private InspectionResult result;
    private Long inspectorId;
    private String inspectorName;
    private LocalDateTime inspectionTime;
    private Double passRate;
    private boolean qualified;
    private String remark;
    private LocalDateTime createTime;
}