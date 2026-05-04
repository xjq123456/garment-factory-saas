package com.garment.rbac.infrastructure.persistence.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色 MyBatis-Plus Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {
}