package com.garment.production.infrastructure.persistence.quality;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质检记录 - 持久化对象
 */
@Data
@TableName("prod_quality_inspection")
public class QualityInspectionDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
    private String inspectionNo;
    private Long orderId;
    private Long taskId;
    private String inspectionType;
    private Integer inspectQty;
    private Integer passQty;
    private Integer rejectQty;
    private String defectTypes;
    private String defectDesc;
    private String result;
    private Long inspectorId;
    private String inspectorName;
    private LocalDateTime inspectionTime;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
}