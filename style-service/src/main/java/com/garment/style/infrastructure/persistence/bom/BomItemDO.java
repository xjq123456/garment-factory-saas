package com.garment.style.infrastructure.persistence.bom;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.common.infrastructure.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_style_bom_item")
public class BomItemDO extends BaseEntity {

    private Long bomId;
    private String materialName;
    private String materialCode;
    private String materialType;
    private String specification;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String supplier;
    private String color;
    private String remark;
    private Integer sortOrder;
}
