package com.garment.production.infrastructure.persistence.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 生产任务 - 持久化对象
 */
@Data
@TableName("prod_production_task")
public class ProductionTaskDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
    private String taskNo;
    private Long orderId;
    private Long routeId;
    private Long stepId;
    private String stepName;
    private Long stationId;
    private String stationName;
    private Long workerId;
    private String workerName;
    private Integer planQty;
    private Integer completedQty;
    private Integer defectiveQty;
    private String unit;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private String status;
    private Integer priority;
    private String remark;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
    @Version
    private Integer version;
}