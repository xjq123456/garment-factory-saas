package com.garment.marketing.application.customer.dto;

import com.garment.common.interfaces.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 客户分页查询条件。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerQuery extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 客户类型（CustomerType.code） */
    private Integer type;

    /** 客户等级（CustomerLevel.code） */
    private Integer level;

    /** 关键字（模糊搜索客户名称或电话） */
    private String keyword;
}
