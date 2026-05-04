package com.garment.rbac.infrastructure.persistence.menu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.rbac.domain.permission.entity.Menu;
import com.garment.rbac.domain.permission.repository.MenuRepository;
import com.garment.rbac.infrastructure.persistence.rolemenu.RoleMenuDO;
import com.garment.rbac.infrastructure.persistence.rolemenu.RoleMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单仓储实现
 */
@Repository
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuMapper menuMapper;
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public Menu save(Menu menu) {
        MenuDO menuDO = MenuConverter.toDO(menu);
        if (menuDO.getId() == null) {
            menuMapper.insert(menuDO);
            menu.setId(menuDO.getId());
        } else {
            menuMapper.updateById(menuDO);
        }
        return menu;
    }

    @Override
    public Menu findById(Long id) {
        MenuDO menuDO = menuMapper.selectById(id);
        return menuDO != null ? MenuConverter.toDomain(menuDO) : null;
    }

    @Override
    public List<Menu> findAll(Long tenantId) {
        LambdaQueryWrapper<MenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MenuDO::getTenantId, tenantId)
               .eq(MenuDO::getIsDeleted, 0)
               .orderByAsc(MenuDO::getSortOrder);
        return menuMapper.selectList(wrapper)
                .stream()
                .map(MenuConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Menu> findByParentId(Long parentId, Long tenantId) {
        LambdaQueryWrapper<MenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MenuDO::getParentId, parentId)
               .eq(MenuDO::getTenantId, tenantId)
               .eq(MenuDO::getIsDeleted, 0)
               .orderByAsc(MenuDO::getSortOrder);
        return menuMapper.selectList(wrapper)
                .stream()
                .map(MenuConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        MenuDO menuDO = menuMapper.selectById(id);
        if (menuDO != null) {
            menuDO.setIsDeleted(1);
            menuMapper.updateById(menuDO);
        }
    }

    @Override
    public List<Menu> findByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenuDO::getRoleId, roleId);
        List<Long> menuIds = roleMenuMapper.selectList(wrapper)
                .stream()
                .map(RoleMenuDO::getMenuId)
                .collect(Collectors.toList());
        if (menuIds.isEmpty()) {
            return Collections.emptyList();
        }
        return findByIds(menuIds, null);
    }

    @Override
    public List<Menu> findByIds(List<Long> ids, Long tenantId) {
        LambdaQueryWrapper<MenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MenuDO::getId, ids)
               .eq(MenuDO::getIsDeleted, 0);
        if (tenantId != null) {
            wrapper.eq(MenuDO::getTenantId, tenantId);
        }
        return menuMapper.selectList(wrapper)
                .stream()
                .map(MenuConverter::toDomain)
                .collect(Collectors.toList());
    }
}