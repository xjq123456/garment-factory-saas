package com.garment.marketing.application.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建客户命令。
 */
@Data
public class CreateCustomerCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 客户名称 */
    @NotBlank(message = "客户名称不能为空")
    private String name;

    /** 联系人姓名 */
    private String contactName;

    /** 联系电话 */
    private String phone;

    /** 电子邮箱 */
    private String email;

    /** 地址 */
    private String address;

    /** 客户类型（CustomerType.code） */
    @NotNull(message = "客户类型不能为空")
    private Integer type;

    /** 客户来源 */
    private String source;

    /** 备注 */
    private String remark;
}
