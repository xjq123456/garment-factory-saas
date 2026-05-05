package com.garment.production.infrastructure.persistence.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 生产工单 - 持久化对象
 */
@Data
@TableName("prod_production_order")
public class ProductionOrderDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
    private String orderNo;
    private Long routeId;
    private Long styleId;
    private String styleCode;
    private String styleName;
    private Long skuId;
    private String skuCode;
    private String customerName;
    private Integer totalQty;
    private Integer completedQty;
    private Integer defectiveQty;
    private String unit;
    private LocalDate deliveryDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer priority;
    private String status;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
    @Version
    private Integer version;
}