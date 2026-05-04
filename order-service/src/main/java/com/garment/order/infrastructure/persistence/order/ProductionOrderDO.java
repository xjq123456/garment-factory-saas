package com.garment.order.infrastructure.persistence.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.garment.common.infrastructure.BaseEntity;
import com.garment.order.domain.order.vo.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@TableName("production_order")
public class ProductionOrderDO extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long tenantId;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private Long styleId;
    private String styleName;
    private BigDecimal totalQuantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private LocalDate deliveryDate;
    private OrderStatus status;
    private String remark;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
}
