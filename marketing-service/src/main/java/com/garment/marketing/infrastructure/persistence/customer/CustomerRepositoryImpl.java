package com.garment.marketing.infrastructure.persistence.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.marketing.domain.customer.entity.Customer;
import com.garment.marketing.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户仓储实现。
 */
@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerMapper customerMapper;

    @Override
    public void save(Customer customer) {
        CustomerDO customerDO = CustomerConverter.toDO(customer);
        if (customer.getId() == null) {
            customerMapper.insert(customerDO);
            customer.setId(customerDO.getId());
        } else {
            customerMapper.updateById(customerDO);
        }
    }

    @Override
    public void updateById(Customer customer) {
        CustomerDO customerDO = CustomerConverter.toDO(customer);
        customerMapper.updateById(customerDO);
    }

    @Override
    public Customer findById(Long id) {
        CustomerDO customerDO = customerMapper.selectById(id);
        return customerDO != null ? CustomerConverter.toEntity(customerDO) : null;
    }

    @Override
    public List<Customer> findByTenantId(Long tenantId) {
        LambdaQueryWrapper<CustomerDO> qw = new LambdaQueryWrapper<>();
        qw.eq(CustomerDO::getTenantId, tenantId);
        List<CustomerDO> list = customerMapper.selectList(qw);
        return list.stream()
                .map(CustomerConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Customer findByPhone(Long tenantId, String phone) {
        LambdaQueryWrapper<CustomerDO> qw = new LambdaQueryWrapper<>();
        qw.eq(CustomerDO::getTenantId, tenantId)
          .eq(CustomerDO::getPhone, phone);
        CustomerDO customerDO = customerMapper.selectOne(qw);
        return customerDO != null ? CustomerConverter.toEntity(customerDO) : null;
    }
}
