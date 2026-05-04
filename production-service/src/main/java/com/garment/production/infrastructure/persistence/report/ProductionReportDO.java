package com.garment.production.infrastructure.persistence.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报工记录 - 持久化对象
 */
@Data
@TableName("prod_production_report")
public class ProductionReportDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
    private String reportNo;
    private Long orderId;
    private Long taskId;
    private Long stationId;
    private Long workerId;
    private String workerName;
    private Integer reportQty;
    private Integer qualifiedQty;
    private Integer defectiveQty;
    private BigDecimal workHours;
    private LocalDateTime reportTime;
    private String remark;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
}