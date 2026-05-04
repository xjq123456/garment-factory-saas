package com.garment.rbac.infrastructure.persistence.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.rbac.domain.role.entity.Role;
import com.garment.rbac.domain.role.repository.RoleRepository;
import com.garment.rbac.infrastructure.persistence.rolemenu.RoleMenuDO;
import com.garment.rbac.infrastructure.persistence.rolemenu.RoleMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色仓储实现
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public Role save(Role role) {
        RoleDO roleDO = RoleConverter.toDO(role);
        if (roleDO.getId() == null) {
            roleMapper.insert(roleDO);
            role.setId(roleDO.getId());
        } else {
            roleMapper.updateById(roleDO);
        }
        return role;
    }

    @Override
    public Optional<Role> findById(Long id) {
        RoleDO roleDO = roleMapper.selectById(id);
        if (roleDO == null) {
            return Optional.empty();
        }
        return Optional.of(RoleConverter.toDomain(roleDO));
    }

    @Override
    public List<Role> findAll(Long tenantId) {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDO::getTenantId, tenantId)
               .eq(RoleDO::getIsDeleted, 0)
               .orderByAsc(RoleDO::getSortOrder);
        return roleMapper.selectList(wrapper)
                .stream()
                .map(RoleConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Role> findByRoleKey(String roleKey, Long tenantId) {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDO::getRoleKey, roleKey)
               .eq(RoleDO::getTenantId, tenantId)
               .eq(RoleDO::getIsDeleted, 0);
        RoleDO roleDO = roleMapper.selectOne(wrapper);
        if (roleDO == null) {
            return Optional.empty();
        }
        return Optional.of(RoleConverter.toDomain(roleDO));
    }

    @Override
    public void deleteById(Long id) {
        RoleDO roleDO = roleMapper.selectById(id);
        if (roleDO != null) {
            roleDO.setIsDeleted(1);
            roleMapper.updateById(roleDO);
        }
    }

    @Override
    public List<Role> findByIds(List<Long> ids, Long tenantId) {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDO::getTenantId, tenantId)
               .in(RoleDO::getId, ids)
               .eq(RoleDO::getIsDeleted, 0);
        return roleMapper.selectList(wrapper)
                .stream()
                .map(RoleConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void saveRoleMenus(Long roleId, List<Long> menuIds) {
        // 先删除原有关联
        deleteRoleMenus(roleId);
        // 批量插入新关联
        for (Long menuId : menuIds) {
            RoleMenuDO roleMenuDO = new RoleMenuDO();
            roleMenuDO.setRoleId(roleId);
            roleMenuDO.setMenuId(menuId);
            roleMenuMapper.insert(roleMenuDO);
        }
    }

    @Override
    public List<Long> findMenuIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenuDO::getRoleId, roleId);
        return roleMenuMapper.selectList(wrapper)
                .stream()
                .map(RoleMenuDO::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRoleMenus(Long roleId) {
        LambdaQueryWrapper<RoleMenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenuDO::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);
    }
}