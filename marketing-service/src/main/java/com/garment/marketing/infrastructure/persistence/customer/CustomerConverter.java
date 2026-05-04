package com.garment.marketing.infrastructure.persistence.customer;

import com.garment.marketing.domain.customer.entity.Customer;
import com.garment.marketing.domain.customer.vo.CustomerLevel;
import com.garment.marketing.domain.customer.vo.CustomerType;

/**
 * 客户领域实体与持久化对象之间的转换器。
 */
public final class CustomerConverter {

    private CustomerConverter() {}

    /**
     * 领域实体 -> 持久化对象。
     */
    public static CustomerDO toDO(Customer entity) {
        CustomerDO d = new CustomerDO();
        d.setId(entity.getId());
        d.setTenantId(entity.getTenantId());
        d.setName(entity.getName());
        d.setContactName(entity.getContactName());
        d.setPhone(entity.getPhone());
        d.setEmail(entity.getEmail());
        d.setAddress(entity.getAddress());
        d.setType(entity.getType() != null ? entity.getType().getCode() : null);
        d.setLevel(entity.getLevel() != null ? entity.getLevel().getCode() : null);
        d.setSource(entity.getSource());
        d.setRemark(entity.getRemark());
        d.setTotalConsumption(entity.getTotalConsumption());
        d.setLastOrderAt(entity.getLastOrderAt());
        d.setCreatedBy(entity.getCreatedBy());
        d.setUpdatedBy(entity.getUpdatedBy());
        return d;
    }

    /**
     * 持久化对象 -> 领域实体。
     */
    public static Customer toEntity(CustomerDO d) {
        Customer entity = new Customer();
        entity.setId(d.getId());
        entity.setTenantId(d.getTenantId());
        entity.setName(d.getName());
        entity.setContactName(d.getContactName());
        entity.setPhone(d.getPhone());
        entity.setEmail(d.getEmail());
        entity.setAddress(d.getAddress());
        entity.setType(resolveType(d.getType()));
        entity.setLevel(resolveLevel(d.getLevel()));
        entity.setSource(d.getSource());
        entity.setRemark(d.getRemark());
        entity.setTotalConsumption(d.getTotalConsumption());
        entity.setLastOrderAt(d.getLastOrderAt());
        entity.setCreatedBy(d.getCreatedBy());
        entity.setCreatedAt(d.getCreatedAt());
        entity.setUpdatedBy(d.getUpdatedBy());
        entity.setUpdatedAt(d.getUpdatedAt());
        return entity;
    }

    private static CustomerType resolveType(Integer code) {
        if (code == null) return null;
        for (CustomerType t : CustomerType.values()) {
            if (t.getCode() == code) return t;
        }
        return null;
    }

    private static CustomerLevel resolveLevel(Integer code) {
        if (code == null) return null;
        for (CustomerLevel l : CustomerLevel.values()) {
            if (l.getCode() == code) return l;
        }
        return null;
    }
}
