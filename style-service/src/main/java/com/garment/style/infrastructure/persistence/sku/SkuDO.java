package com.garment.style.infrastructure.persistence.sku;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.common.infrastructure.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_style_sku")
public class SkuDO extends BaseEntity {

    private Long styleId;
    private String skuCode;
    private String color;
    private String colorCode;
    private String size;
    private String sizeType;
    private String barcode;
    private BigDecimal weight;
    private BigDecimal extraPrice;
    private Integer status;
    private Integer sortOrder;
}
