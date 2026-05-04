package com.garment.marketing.domain.customer.repository;

import com.garment.marketing.domain.customer.entity.Customer;

import java.util.List;

/**
 * 客户仓储接口。
 */
public interface CustomerRepository {

    /**
     * 保存客户（新增或更新）。
     *
     * @param customer 客户实体
     */
    void save(Customer customer);

    /**
     * 根据 ID 更新客户。
     *
     * @param customer 客户实体
     */
    void updateById(Customer customer);

    /**
     * 根据 ID 查找客户。
     *
     * @param id 客户 ID
     * @return 客户实体
     */
    Customer findById(Long id);

    /**
     * 根据租户 ID 查找所有客户。
     *
     * @param tenantId 租户 ID
     * @return 客户列表
     */
    List<Customer> findByTenantId(Long tenantId);

    /**
     * 根据电话查找客户。
     *
     * @param tenantId 租户 ID
     * @param phone    电话
     * @return 客户实体
     */
    Customer findByPhone(Long tenantId, String phone);
}
