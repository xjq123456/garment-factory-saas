package com.garment.rbac.infrastructure.persistence.role;

import com.garment.rbac.domain.role.entity.Role;
import com.garment.rbac.domain.role.vo.RoleStatus;
import com.garment.rbac.domain.shared.vo.RoleKey;
import com.garment.rbac.domain.shared.vo.RoleName;

/**
 * Role DO ↔ Domain 转换器
 */
public final class RoleConverter {

    private RoleConverter() {}

    public static RoleDO toDO(Role role) {
        RoleDO d = new RoleDO();
        d.setId(role.getId());
        d.setTenantId(role.getTenantId());
        d.setRoleName(role.getRoleName() != null ? role.getRoleName().getValue() : null);
        d.setRoleKey(role.getRoleKey() != null ? role.getRoleKey().getValue() : null);
        d.setSortOrder(role.getSortOrder());
        d.setStatus(role.getStatus());
        d.setRemark(role.getRemark());
        d.setCreateBy(role.getCreateBy());
        d.setUpdateBy(role.getUpdateBy());
        d.setCreateTime(role.getCreateTime());
        d.setUpdateTime(role.getUpdateTime());
        d.setIsDeleted(role.getDeleted());
        return d;
    }

    public static Role toDomain(RoleDO d) {
        Role role = new Role();
        role.setId(d.getId());
        role.setTenantId(d.getTenantId());
        if (d.getRoleName() != null) {
            role.setRoleName(RoleName.of(d.getRoleName()));
        }
        if (d.getRoleKey() != null) {
            role.setRoleKey(RoleKey.of(d.getRoleKey()));
        }
        role.setSortOrder(d.getSortOrder());
        role.setStatus(d.getStatus());
        role.setRemark(d.getRemark());
        role.setCreateBy(d.getCreateBy());
        role.setUpdateBy(d.getUpdateBy());
        role.setCreateTime(d.getCreateTime());
        role.setUpdateTime(d.getUpdateTime());
        role.setDeleted(d.getIsDeleted());
        return role;
    }
}