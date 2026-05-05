package com.garment.marketing.application.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garment.common.domain.BizException;
import com.garment.common.domain.AuthUserContext;
import com.garment.common.interfaces.PageResult;
import com.garment.marketing.application.customer.dto.CreateCustomerCommand;
import com.garment.marketing.application.customer.dto.CustomerQuery;
import com.garment.marketing.domain.customer.entity.Customer;
import com.garment.marketing.domain.customer.repository.CustomerRepository;
import com.garment.marketing.domain.customer.vo.CustomerLevel;
import com.garment.marketing.domain.customer.vo.CustomerType;
import com.garment.marketing.infrastructure.persistence.customer.CustomerConverter;
import com.garment.marketing.infrastructure.persistence.customer.CustomerDO;
import com.garment.marketing.infrastructure.persistence.customer.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 客户应用服务。
 */
@Service
@RequiredArgsConstructor
public class CustomerAppService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * 创建客户。
     */
    @Transactional
    public Customer createCustomer(CreateCustomerCommand cmd) {
        CustomerType customerType = Arrays.stream(CustomerType.values())
                .filter(t -> t.getCode() == cmd.getType())
                .findFirst()
                .orElseThrow(() -> new BizException("无效的客户类型"));

        Customer customer = new Customer();
        customer.setName(cmd.getName());
        customer.setContactName(cmd.getContactName());
        customer.setPhone(cmd.getPhone());
        customer.setEmail(cmd.getEmail());
        customer.setAddress(cmd.getAddress());
        customer.setType(customerType);
        customer.setLevel(CustomerLevel.NORMAL);
        customer.setSource(cmd.getSource());
        customer.setRemark(cmd.getRemark());

        customerRepository.save(customer);
        return customer;
    }

    /**
     * 升级客户等级。
     */
    @Transactional
    public Customer upgradeLevel(Long id, Integer newLevel) {
        Customer customer = customerRepository.findById(id);
        if (customer == null) {
            throw new BizException("客户不存在");
        }

        CustomerLevel level = Arrays.stream(CustomerLevel.values())
                .filter(l -> l.getCode() == newLevel)
                .findFirst()
                .orElseThrow(() -> new BizException("无效的客户等级"));

        customer.upgradeLevel(level);
        customerRepository.updateById(customer);
        return customer;
    }

    /**
     * 增加消费金额。
     */
    @Transactional
    public Customer addConsumption(Long id, BigDecimal amount) {
        Customer customer = customerRepository.findById(id);
        if (customer == null) {
            throw new BizException("客户不存在");
        }

        customer.addConsumption(amount);
        customerRepository.updateById(customer);
        return customer;
    }

    /**
     * 查询客户详情。
     */
    public Customer getCustomer(Long id) {
        Customer customer = customerRepository.findById(id);
        if (customer == null) {
            throw new BizException("客户不存在");
        }
        return customer;
    }

    /**
     * 分页查询客户。
     */
    public PageResult<Customer> pageCustomers(CustomerQuery query) {
        LambdaQueryWrapper<CustomerDO> qw = new LambdaQueryWrapper<>();

        if (query.getType() != null) {
            qw.eq(CustomerDO::getType, query.getType());
        }
        if (query.getLevel() != null) {
            qw.eq(CustomerDO::getLevel, query.getLevel());
        }
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            qw.and(w -> w.like(CustomerDO::getName, query.getKeyword())
                    .or()
                    .like(CustomerDO::getPhone, query.getKeyword()));
        }
        qw.orderByDesc(CustomerDO::getCreatedAt);

        Page<CustomerDO> page = customerMapper.selectPage(
                new Page<>(query.getPageNum(), query.getPageSize()), qw);

        Page<Customer> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(page.getRecords().stream()
                .map(CustomerConverter::toEntity)
                .toList());
        return PageResult.of(result);
    }
}
