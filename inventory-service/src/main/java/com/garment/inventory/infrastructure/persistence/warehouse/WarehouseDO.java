package com.garment.inventory.infrastructure.persistence.warehouse;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 仓库持久化对象
 */
@Data
@TableName("inv_warehouse")
public class WarehouseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private String warehouseCode;

    private String warehouseName;

    private Integer warehouseType;

    private String contactPerson;

    private String contactPhone;

    private String address;

    private Integer status;

    private String remark;

    private Long createBy;

    private Long updateBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}