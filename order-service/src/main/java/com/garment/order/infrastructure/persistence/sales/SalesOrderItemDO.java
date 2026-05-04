package com.garment.order.infrastructure.persistence.sales;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.garment.common.infrastructure.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@TableName("sales_order_item")
public class SalesOrderItemDO extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long tenantId;
    private Long orderId;
    private Long skuId;
    private String skuCode;
    private String color;
    private String size;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private String remark;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
}