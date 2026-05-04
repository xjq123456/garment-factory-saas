package com.garment.style.infrastructure.persistence.style;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.common.infrastructure.BaseEntity;
import com.garment.style.domain.style.vo.StyleStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_style")
public class StyleDO extends BaseEntity {

    private String styleCode;
    private String styleName;
    private Long categoryId;
    private String season;
    private String year;
    private String patternType;
    private String craftDesc;
    private String designSketch;
    private String mainImage;
    private String images;
    private String tags;
    private StyleStatus status;
    private String remark;
}
