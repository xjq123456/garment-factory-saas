package com.garment.rbac.domain.role.entity;

import com.garment.common.infrastructure.BaseEntity;
import com.garment.rbac.domain.role.vo.RoleStatus;
import com.garment.rbac.domain.shared.vo.RoleKey;
import com.garment.rbac.domain.shared.vo.RoleName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色聚合根
 */
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Role extends BaseEntity {

    /** 租户ID */
    private Long tenantId;

    /** 角色名称 */
    private RoleName roleName;

    /** 角色标识 */
    private RoleKey roleKey;

    /** 排序 */
    private Integer sortOrder;

    /** 状态 */
    private RoleStatus status;

    /** 备注 */
    private String remark;

    /** 关联的菜单ID集合（非持久化，由 Repository 层关联查询填充） */
    private Set<Long> menuIds = new HashSet<>();

    // ======== 业务方法 ========

    /**
     * 禁用角色
     */
    public void disable() {
        this.status = RoleStatus.DISABLED;
    }

    /**
     * 启用角色
     */
    public void enable() {
        this.status = RoleStatus.ENABLED;
    }

    /**
     * 判断是否超级管理员角色
     */
    public boolean isSuperAdmin() {
        return "super_admin".equals(roleKey.getValue());
    }

    /**
     * 设置角色菜单关联
     */
    public void assignMenus(Set<Long> newMenuIds) {
        this.menuIds.clear();
        if (newMenuIds != null) {
            this.menuIds.addAll(newMenuIds);
        }
    }

    /**
     * 创建角色工厂方法
     */
    public static Role create(Long tenantId, String roleName, String roleKey,
                               Integer sortOrder, String remark) {
        Role role = new Role();
        role.setTenantId(tenantId);
        role.setRoleName(RoleName.of(roleName));
        role.setRoleKey(RoleKey.of(roleKey));
        role.setSortOrder(sortOrder != null ? sortOrder : 0);
        role.setStatus(RoleStatus.ENABLED);
        role.setRemark(remark);
        return role;
    }
}