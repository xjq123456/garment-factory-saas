package com.garment.rbac.domain.role.repository;

import com.garment.rbac.domain.role.entity.Role;

import java.util.List;
import java.util.Optional;

/**
 * 角色仓储接口
 */
public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(Long id);

    List<Role> findAll(Long tenantId);

    Optional<Role> findByRoleKey(String roleKey, Long tenantId);

    void deleteById(Long id);

    List<Role> findByIds(List<Long> ids, Long tenantId);

    /**
     * 保存角色菜单关联
     */
    void saveRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 查询角色关联的菜单ID
     */
    List<Long> findMenuIdsByRoleId(Long roleId);

    /**
     * 删除角色所有菜单关联
     */
    void deleteRoleMenus(Long roleId);
}