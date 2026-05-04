package com.garment.marketing.infrastructure.persistence.customer;

import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.common.infrastructure.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户数据对象（持久化层）。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mk_customer")
public class CustomerDO extends BaseEntity {

    /** 客户名称 */
    private String name;

    /** 联系人姓名 */
    private String contactName;

    /** 联系电话 */
    private String phone;

    /** 电子邮箱 */
    private String email;

    /** 地址 */
    private String address;

    /** 客户类型（对应 CustomerType.code） */
    private Integer type;

    /** 客户等级（对应 CustomerLevel.code） */
    private Integer level;

    /** 客户来源 */
    private String source;

    /** 备注 */
    private String remark;

    /** 累计消费金额 */
    private BigDecimal totalConsumption;

    /** 最近下单时间 */
    private LocalDateTime lastOrderAt;
}
