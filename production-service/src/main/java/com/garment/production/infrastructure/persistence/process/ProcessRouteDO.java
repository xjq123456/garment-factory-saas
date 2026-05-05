package com.garment.production.infrastructure.persistence.process;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工艺路线 - 持久化对象
 */
@Data
@TableName("prod_process_route")
public class ProcessRouteDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
    private String routeCode;
    private String routeName;
    private Long styleId;
    private String description;
    private String status;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
    @Version
    private Integer version;
}