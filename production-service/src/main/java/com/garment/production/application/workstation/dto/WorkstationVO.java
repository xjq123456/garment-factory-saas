package com.garment.production.application.workstation.dto;

import com.garment.production.domain.workstation.vo.WorkstationStatus;
import com.garment.production.domain.workstation.vo.WorkstationType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工位视图对象
 */
@Data
public class WorkstationVO {

    private Long id;
    private String stationCode;
    private String stationName;
    private String workshop;
    private String productionLine;
    private WorkstationType stationType;
    private Long workerId;
    private String workerName;
    private String equipmentCode;
    private WorkstationStatus status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}