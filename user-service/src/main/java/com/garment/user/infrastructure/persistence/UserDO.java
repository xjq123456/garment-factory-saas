package com.garment.user.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.common.infrastructure.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户数据对象（DO）
 * <p>
 * 用于 MyBatis Plus 持久化，与数据库表一一对应。
 * 与领域实体 User 分离，保证领域层的纯洁性。
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class UserDO extends BaseEntity {

    /** 用户名（全局唯一） */
    private String username;

    /** 密码（BCrypt 加密存储） */
    private String password;

    /** 昵称 */
    private String nickname;

    /** 头像 URL */
    private String avatar;

    /** 手机号 */
    @TableField("phone")
    private String phone;

    /** 邮箱 */
    @TableField("email")
    private String email;

    /** 用户状态：ACTIVE / FROZEN / DELETED */
    private String status;

    /** 最后登录时间 */
    private LocalDateTime lastLoginAt;

    /** 最后登录 IP */
    private String lastLoginIp;
}