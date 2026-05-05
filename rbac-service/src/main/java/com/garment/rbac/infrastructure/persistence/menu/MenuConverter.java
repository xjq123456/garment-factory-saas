package com.garment.rbac.infrastructure.persistence.menu;

import com.garment.rbac.domain.permission.entity.Menu;
import com.garment.rbac.domain.permission.vo.MenuType;

/**
 * Menu DO ↔ Domain 转换器
 */
public final class MenuConverter {

    private MenuConverter() {}

    public static MenuDO toDO(Menu menu) {
        MenuDO d = new MenuDO();
        d.setId(menu.getId());
        d.setTenantId(menu.getTenantId());
        d.setParentId(menu.getParentId());
        d.setMenuName(menu.getMenuName());
        d.setMenuType(menu.getMenuType());
        d.setPath(menu.getPath());
        d.setComponent(menu.getComponent());
        d.setIcon(menu.getIcon());
        d.setPermission(menu.getPermission());
        d.setSortOrder(menu.getSortOrder());
        d.setVisible(menu.getVisible());
        d.setStatus(menu.getStatus());
        d.setRemark(menu.getRemark());
        d.setCreateBy(menu.getCreateBy());
        d.setUpdateBy(menu.getUpdateBy());
        d.setCreateTime(menu.getCreateTime());
        d.setUpdateTime(menu.getUpdateTime());
        d.setIsDeleted(menu.getDeleted());
        return d;
    }

    public static Menu toDomain(MenuDO d) {
        Menu menu = new Menu();
        menu.setId(d.getId());
        menu.setTenantId(d.getTenantId());
        menu.setParentId(d.getParentId());
        menu.setMenuName(d.getMenuName());
        menu.setMenuType(d.getMenuType());
        menu.setPath(d.getPath());
        menu.setComponent(d.getComponent());
        menu.setIcon(d.getIcon());
        menu.setPermission(d.getPermission());
        menu.setSortOrder(d.getSortOrder());
        menu.setVisible(d.getVisible());
        menu.setStatus(d.getStatus());
        menu.setRemark(d.getRemark());
        menu.setCreateBy(d.getCreateBy());
        menu.setUpdateBy(d.getUpdateBy());
        menu.setCreateTime(d.getCreateTime());
        menu.setUpdateTime(d.getUpdateTime());
        menu.setDeleted(d.getIsDeleted());
        return menu;
    }
}