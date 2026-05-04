package com.garment.user.application.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册命令 DTO
 */
@Data
public class RegisterCommand {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 32, message = "用户名长度必须在 4-32 位之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 64, message = "密码长度必须在 6-64 位之间")
    private String password;

    private String nickname;

    private String phone;

    private String email;
}