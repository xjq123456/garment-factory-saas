package com.garment.order.infrastructure.persistence.sales;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.garment.common.infrastructure.BaseEntity;
import com.garment.order.domain.sales.vo.SalesOrderStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@TableName("sales_order")
public class SalesOrderDO extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long tenantId;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private LocalDate deliveryDate;
    private SalesOrderStatus status;
    private String shippingAddress;
    private String remark;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
}