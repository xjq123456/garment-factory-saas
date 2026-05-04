package com.garment.production.infrastructure.persistence.workstation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工位 - 持久化对象
 */
@Data
@TableName("prod_workstation")
public class WorkstationDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
    private String stationCode;
    private String stationName;
    private String workshop;
    private String productionLine;
    private String stationType;
    private Long workerId;
    private String workerName;
    private String equipmentCode;
    private String status;
    private String remark;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
    @Version
    private Integer version;
}