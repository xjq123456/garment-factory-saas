package com.garment.rbac.infrastructure.persistence.rolemenu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色菜单关联 Mapper
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuDO> {
}