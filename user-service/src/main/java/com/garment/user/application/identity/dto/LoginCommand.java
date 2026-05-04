package com.garment.user.application.identity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录命令 DTO
 */
@Data
public class LoginCommand {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}