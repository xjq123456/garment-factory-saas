package com.garment.rbac.infrastructure.persistence.menu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.rbac.domain.permission.vo.MenuType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单持久化对象
 */
@Data
@TableName("sys_menu")
public class MenuDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
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
    private Long createBy;
    private Long updateBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isDeleted;
}