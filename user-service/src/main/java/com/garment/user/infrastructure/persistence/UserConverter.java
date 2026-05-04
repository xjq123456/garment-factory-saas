package com.garment.user.infrastructure.persistence;

import com.garment.user.domain.identity.entity.User;
import com.garment.user.domain.identity.vo.Email;
import com.garment.user.domain.identity.vo.Phone;
import com.garment.user.domain.identity.vo.UserStatus;

/**
 * 用户领域实体与数据对象之间的转换器
 */
public final class UserConverter {

    private UserConverter() {
    }

    /**
     * 领域实体 -> 数据对象
     */
    public static UserDO toDO(User user) {
        if (user == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        userDO.setId(user.getId());
        userDO.setTenantId(user.getTenantId());
        userDO.setUsername(user.getUsername());
        userDO.setPassword(user.getPassword());
        userDO.setNickname(user.getNickname());
        userDO.setAvatar(user.getAvatar());
        userDO.setPhone(user.getPhone() != null ? user.getPhone().getValue() : null);
        userDO.setEmail(user.getEmail() != null ? user.getEmail().getValue() : null);
        userDO.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
        userDO.setLastLoginAt(user.getLastLoginAt());
        userDO.setLastLoginIp(user.getLastLoginIp());
        userDO.setCreatedBy(user.getCreatedBy());
        userDO.setCreatedAt(user.getCreatedAt());
        userDO.setUpdatedBy(user.getUpdatedBy());
        userDO.setUpdatedAt(user.getUpdatedAt());
        return userDO;
    }

    /**
     * 数据对象 -> 领域实体
     */
    public static User toDomain(UserDO userDO) {
        if (userDO == null) {
            return null;
        }
        User user = new User();
        user.setId(userDO.getId());
        user.setTenantId(userDO.getTenantId());
        user.setUsername(userDO.getUsername());
        user.setPassword(userDO.getPassword());
        user.setNickname(userDO.getNickname());
        user.setAvatar(userDO.getAvatar());
        if (userDO.getPhone() != null && !userDO.getPhone().isEmpty()) {
            user.setPhone(new Phone(userDO.getPhone()));
        }
        if (userDO.getEmail() != null && !userDO.getEmail().isEmpty()) {
            user.setEmail(new Email(userDO.getEmail()));
        }
        if (userDO.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(userDO.getStatus()));
        }
        user.setLastLoginAt(userDO.getLastLoginAt());
        user.setLastLoginIp(userDO.getLastLoginIp());
        user.setCreatedBy(userDO.getCreatedBy());
        user.setCreatedAt(userDO.getCreatedAt());
        user.setUpdatedBy(userDO.getUpdatedBy());
        user.setUpdatedAt(userDO.getUpdatedAt());
        return user;
    }
}