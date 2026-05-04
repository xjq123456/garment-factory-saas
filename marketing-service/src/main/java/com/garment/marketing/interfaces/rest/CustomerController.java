package com.garment.marketing.interfaces.rest;

import com.garment.common.interfaces.PageResult;
import com.garment.common.interfaces.R;
import com.garment.marketing.application.customer.CustomerAppService;
import com.garment.marketing.application.customer.dto.CreateCustomerCommand;
import com.garment.marketing.application.customer.dto.CustomerQuery;
import com.garment.marketing.domain.customer.entity.Customer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 客户管理接口。
 */
@RestController
@RequestMapping("/api/marketing/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerAppService customerAppService;

    /**
     * 创建客户。
     */
    @PostMapping
    public R<Customer> createCustomer(@Valid @RequestBody CreateCustomerCommand cmd) {
        return R.ok(customerAppService.createCustomer(cmd));
    }

    /**
     * 升级客户等级。
     */
    @PutMapping("/{id}/level")
    public R<Customer> upgradeLevel(@PathVariable Long id, @RequestParam Integer newLevel) {
        return R.ok(customerAppService.upgradeLevel(id, newLevel));
    }

    /**
     * 增加消费金额。
     */
    @PutMapping("/{id}/consumption")
    public R<Customer> addConsumption(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return R.ok(customerAppService.addConsumption(id, amount));
    }

    /**
     * 查询客户详情。
     */
    @GetMapping("/{id}")
    public R<Customer> getCustomer(@PathVariable Long id) {
        return R.ok(customerAppService.getCustomer(id));
    }

    /**
     * 分页查询客户。
     */
    @GetMapping("/page")
    public R<PageResult<Customer>> pageCustomers(CustomerQuery query) {
        return R.ok(customerAppService.pageCustomers(query));
    }
}
