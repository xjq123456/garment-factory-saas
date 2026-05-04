package com.garment.style.infrastructure.persistence.bom;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.common.infrastructure.BaseEntity;
import com.garment.style.domain.bom.vo.BomStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_style_bom")
public class BomDO extends BaseEntity {

    private Long styleId;
    private String bomCode;
    private String bomName;
    private String versionNo;
    private BomStatus status;
    private String remark;
}
