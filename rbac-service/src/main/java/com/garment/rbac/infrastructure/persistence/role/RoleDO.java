package com.garment.rbac.infrastructure.persistence.role;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.garment.rbac.domain.role.vo.RoleStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色持久化对象
 */
@Data
@TableName("sys_role")
public class RoleDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
    private String roleName;
    private String roleKey;
    private Integer sortOrder;
    private RoleStatus status;
    private String remark;
    private Long createBy;
    private Long updateBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isDeleted;
}