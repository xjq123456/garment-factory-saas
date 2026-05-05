package com.garment.marketing.domain.customer.entity;

import com.garment.common.domain.BaseDomainEntity;
import com.garment.common.domain.BizException;
import com.garment.marketing.domain.customer.vo.CustomerLevel;
import com.garment.marketing.domain.customer.vo.CustomerType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户领域实体。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseDomainEntity {

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

    /** 客户类型 */
    private CustomerType type;

    /** 客户等级 */
    private CustomerLevel level;

    /** 客户来源 */
    private String source;

    /** 备注 */
    private String remark;

    /** 累计消费金额 */
    private BigDecimal totalConsumption;

    /** 最近下单时间 */
    private LocalDateTime lastOrderAt;

    /**
     * 升级客户等级（只能升级，不能降级）。
     *
     * @param newLevel 新等级
     */
    public void upgradeLevel(CustomerLevel newLevel) {
        if (newLevel == null) {
            throw new BizException("客户等级不能为空");
        }
        if (this.level != null && newLevel.getCode() <= this.level.getCode()) {
            throw new BizException("客户等级只能升级，不能降级");
        }
        this.level = newLevel;
    }

    /**
     * 增加消费金额并更新最近下单时间。
     *
     * @param amount 消费金额
     */
    public void addConsumption(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("消费金额必须大于零");
        }
        this.totalConsumption = this.totalConsumption == null
                ? amount
                : this.totalConsumption.add(amount);
        this.lastOrderAt = LocalDateTime.now();
    }

    /**
     * 更新联系信息。
     *
     * @param name  联系人姓名
     * @param phone 联系电话
     * @param email 电子邮箱
     * @param address 地址
     */
    public void updateContact(String name, String phone, String email, String address) {
        this.contactName = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
}
