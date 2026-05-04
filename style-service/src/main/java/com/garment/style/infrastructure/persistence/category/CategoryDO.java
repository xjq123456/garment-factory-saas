package com.garment.style.infrastructure.persistence.category;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.common.infrastructure.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_style_category")
public class CategoryDO extends BaseEntity {

    private Long parentId;
    private String name;
    private Integer sortOrder;
    private String icon;
    private Integer status;
}
