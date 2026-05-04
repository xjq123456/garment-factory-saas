package com.garment.inventory.infrastructure.persistence.stocklog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成品库存变动日志持久化对象
 */
@Data
@TableName("inv_stock_log")
public class StockLogDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long warehouseId;

    private Long skuId;

    private Integer changeType;

    private Integer changeQty;

    private Integer beforeQty;

    private Integer afterQty;

    private String bizType;

    private String bizNo;

    private Long operatorId;

    private String operatorName;

    private String remark;

    private LocalDateTime createTime;
}