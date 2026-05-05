package com.garment.rbac.interfaces.rest;

import com.garment.common.domain.AuthUserContext;
import com.garment.common.interfaces.R;
import com.garment.rbac.application.RbacAppService;
import com.garment.rbac.domain.permission.entity.Menu;
import com.garment.rbac.domain.role.entity.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RBAC 管理控制器
 */
@RestController
@RequestMapping("/api/rbac")
@RequiredArgsConstructor
public class RbacController {

    private final RbacAppService rbacAppService;

    // ==================== 角色管理 ====================

    @PostMapping("/roles")
    public R<Role> createRole(@RequestBody RoleRequest request) {
        return R.ok(rbacAppService.createRole(
                request.getRoleName(), request.getRoleKey(),
                request.getSortOrder(), request.getRemark()));
    }

    @PutMapping("/roles/{roleId}")
    public R<Role> updateRole(@PathVariable Long roleId, @RequestBody RoleRequest request) {
        return R.ok(rbacAppService.updateRole(roleId,
                request.getRoleName(), request.getSortOrder(), request.getRemark()));
    }

    @DeleteMapping("/roles/{roleId}")
    public R<Void> deleteRole(@PathVariable Long roleId) {
        rbacAppService.deleteRole(roleId);
        return R.ok(null);
    }

    @PutMapping("/roles/{roleId}/enable")
    public R<Void> enableRole(@PathVariable Long roleId) {
        rbacAppService.enableRole(roleId);
        return R.ok(null);
    }

    @PutMapping("/roles/{roleId}/disable")
    public R<Void> disableRole(@PathVariable Long roleId) {
        rbacAppService.disableRole(roleId);
        return R.ok(null);
    }

    @GetMapping("/roles")
    public R<List<Role>> listRoles() {
        return R.ok(rbacAppService.listRoles());
    }

    // ==================== 菜单管理 ====================

    @PostMapping("/menus")
    public R<Menu> createMenu(@RequestBody MenuRequest request) {
        return R.ok(rbacAppService.createMenu(
                request.getParentId(), request.getMenuName(), request.getMenuType(),
                request.getPath(), request.getComponent(), request.getIcon(),
                request.getPermission(), request.getSortOrder(),
                request.getVisible(), request.getRemark()));
    }

    @PutMapping("/menus/{menuId}")
    public R<Menu> updateMenu(@PathVariable Long menuId, @RequestBody MenuRequest request) {
        return R.ok(rbacAppService.updateMenu(menuId,
                request.getMenuName(), request.getPath(), request.getComponent(),
                request.getIcon(), request.getPermission(), request.getSortOrder(),
                request.getVisible(), request.getRemark()));
    }

    @DeleteMapping("/menus/{menuId}")
    public R<Void> deleteMenu(@PathVariable Long menuId) {
        rbacAppService.deleteMenu(menuId);
        return R.ok(null);
    }

    @GetMapping("/menus")
    public R<List<Menu>> listMenus() {
        return R.ok(rbacAppService.listMenus());
    }

    @GetMapping("/menus/tree")
    public R<List<Menu>> getMenuTree() {
        return R.ok(rbacAppService.getMenuTree());
    }

    // ==================== 角色菜单授权 ====================

    @PostMapping("/roles/{roleId}/menus")
    public R<Void> assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        rbacAppService.assignMenus(roleId, menuIds);
        return R.ok(null);
    }

    @GetMapping("/roles/{roleId}/menus")
    public R<List<Menu>> getMenusByRole(@PathVariable Long roleId) {
        return R.ok(rbacAppService.getMenusByRole(roleId));
    }

    @GetMapping("/roles/{roleId}/menu-ids")
    public R<List<Long>> getMenuIdsByRole(@PathVariable Long roleId) {
        return R.ok(rbacAppService.getMenuIdsByRole(roleId));
    }

    // ==================== 请求体 ====================

    @Data
    public static class RoleRequest {
        private String roleName;
        private String roleKey;
        private Integer sortOrder;
        private String remark;
    }

    @Data
    public static class MenuRequest {
        private Long parentId;
        private String menuName;
        private int menuType;
        private String path;
        private String component;
        private String icon;
        private String permission;
        private Integer sortOrder;
        private Integer visible;
        private String remark;
    }
}