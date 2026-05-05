package com.garment.rbac.application;

import com.garment.common.domain.BizException;
import com.garment.common.domain.AuthUserContext;
import com.garment.rbac.domain.permission.entity.Menu;
import com.garment.rbac.domain.permission.repository.MenuRepository;
import com.garment.rbac.domain.permission.vo.MenuType;
import com.garment.rbac.domain.role.entity.Role;
import com.garment.rbac.domain.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RBAC 应用服务
 */
@Service
@RequiredArgsConstructor
public class RbacAppService {

    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;

    // ==================== 角色管理 ====================

    @Transactional
    public Role createRole(String roleName, String roleKey, Integer sortOrder, String remark) {
        Long tenantId = AuthUserContext.requireTenantId();
        roleRepository.findByRoleKey(roleKey, tenantId).ifPresent(r -> {
            throw new BizException("角色标识已存在: " + roleKey);
        });
        Role role = Role.create(tenantId, roleName, roleKey, sortOrder, remark);
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRole(Long roleId, String roleName, Integer sortOrder, String remark) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BizException("角色不存在: " + roleId));
        role.setRoleName(com.garment.rbac.domain.shared.vo.RoleName.of(roleName));
        role.setSortOrder(sortOrder);
        role.setRemark(remark);
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long roleId) {
        roleRepository.findById(roleId)
                .orElseThrow(() -> new BizException("角色不存在: " + roleId));
        roleRepository.deleteRoleMenus(roleId);
        roleRepository.deleteById(roleId);
    }

    @Transactional
    public void enableRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BizException("角色不存在: " + roleId));
        role.enable();
        roleRepository.save(role);
    }

    @Transactional
    public void disableRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BizException("角色不存在: " + roleId));
        role.disable();
        roleRepository.save(role);
    }

    public List<Role> listRoles() {
        Long tenantId = AuthUserContext.requireTenantId();
        return roleRepository.findAll(tenantId);
    }

    // ==================== 菜单管理 ====================

    @Transactional
    public Menu createMenu(Long parentId, String menuName, int menuType,
                           String path, String component, String icon,
                           String permission, Integer sortOrder, Integer visible, String remark) {
        Long tenantId = AuthUserContext.requireTenantId();
        MenuType type = MenuType.fromCode(menuType);
        Menu menu = Menu.create(tenantId, parentId, menuName, type, path, component, icon, permission, sortOrder);
        menu.setVisible(visible);
        menu.setRemark(remark);
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu updateMenu(Long menuId, String menuName, String path, String component,
                           String icon, String permission, Integer sortOrder, Integer visible, String remark) {
        Menu menu = menuRepository.findById(menuId);
        if (menu == null) {
            throw new BizException("菜单不存在: " + menuId);
        }
        menu.setMenuName(menuName);
        menu.setPath(path);
        menu.setComponent(component);
        menu.setIcon(icon);
        menu.setPermission(permission);
        menu.setSortOrder(sortOrder);
        menu.setVisible(visible);
        menu.setRemark(remark);
        return menuRepository.save(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId);
        if (menu == null) {
            throw new BizException("菜单不存在: " + menuId);
        }
        menuRepository.deleteById(menuId);
    }

    public List<Menu> listMenus() {
        Long tenantId = AuthUserContext.requireTenantId();
        return menuRepository.findAll(tenantId);
    }

    public List<Menu> getMenuTree() {
        Long tenantId = AuthUserContext.requireTenantId();
        List<Menu> allMenus = menuRepository.findAll(tenantId);
        return buildTree(allMenus, 0L);
    }

    private List<Menu> buildTree(List<Menu> allMenus, Long parentId) {
        return allMenus.stream()
                .filter(m -> parentId.equals(m.getParentId()))
                .map(m -> {
                    m.setChildren(buildTree(allMenus, m.getId()));
                    return m;
                })
                .collect(Collectors.toList());
    }

    // ==================== 角色菜单授权 ====================

    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleRepository.findById(roleId)
                .orElseThrow(() -> new BizException("角色不存在: " + roleId));
        roleRepository.saveRoleMenus(roleId, menuIds);
    }

    public List<Menu> getMenusByRole(Long roleId) {
        return menuRepository.findByRoleId(roleId);
    }

    public List<Long> getMenuIdsByRole(Long roleId) {
        return roleRepository.findMenuIdsByRoleId(roleId);
    }
}