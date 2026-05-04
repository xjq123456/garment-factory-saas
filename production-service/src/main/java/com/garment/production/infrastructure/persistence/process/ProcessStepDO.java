package com.garment.production.infrastructure.persistence.process;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工序步骤 - 持久化对象
 */
@Data
@TableName("prod_process_step")
public class ProcessStepDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
    private Long routeId;
    private Integer stepNo;
    private String stepName;
    private String stepType;
    private BigDecimal standardTime;
    private Integer standardOutput;
    private String description;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
}