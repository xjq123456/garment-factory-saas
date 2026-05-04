package com.garment.rbac.domain.permission.repository;

import com.garment.rbac.domain.permission.entity.Menu;

import java.util.List;

/**
 * 菜单仓储接口
 */
public interface MenuRepository {

    Menu save(Menu menu);

    Menu findById(Long id);

    List<Menu> findAll(Long tenantId);

    List<Menu> findByParentId(Long parentId, Long tenantId);

    void deleteById(Long id);

    List<Menu> findByRoleId(Long roleId);

    List<Menu> findByIds(List<Long> ids, Long tenantId);
}