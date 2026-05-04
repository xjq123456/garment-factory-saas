package com.garment.rbac.infrastructure.persistence.rolemenu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色菜单关联持久化对象
 */
@Data
@TableName("sys_role_menu")
public class RoleMenuDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long roleId;
    private Long menuId;
}