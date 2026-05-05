package com.garment.rbac.domain.permission.entity;

import com.garment.common.domain.BaseDomainEntity;
import com.garment.rbac.domain.permission.vo.MenuType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单/权限聚合根
 */
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Menu extends BaseDomainEntity {

    private Long parentId;
    private String menuName;
    private MenuType menuType;
    private String path;
    private String component;
    private String icon;
    private String permission;
    private Integer sortOrder;
    private Integer visible;
    private Integer status;
    private String remark;

    /** 子菜单（非持久化，用于构建菜单树） */
    private List<Menu> children = new ArrayList<>();

    public static Menu create(Long tenantId, Long parentId, String menuName,
                               MenuType menuType, String path, String component,
                               String icon, String permission, Integer sortOrder) {
        Menu menu = new Menu();
        menu.setTenantId(tenantId);
        menu.setParentId(parentId != null ? parentId : 0L);
        menu.setMenuName(menuName);
        menu.setMenuType(menuType);
        menu.setPath(path);
        menu.setComponent(component);
        menu.setIcon(icon);
        menu.setPermission(permission);
        menu.setSortOrder(sortOrder != null ? sortOrder : 0);
        menu.setVisible(1);
        menu.setStatus(1);
        return menu;
    }
}