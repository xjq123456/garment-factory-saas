-- ============================================================
-- RBAC 权限服务数据库表
-- 数据库: garment_rbac
-- ============================================================

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id`          BIGINT       NOT NULL COMMENT '雪花主键',
    `tenant_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '租户ID',
    `role_name`   VARCHAR(64)  NOT NULL COMMENT '角色名称',
    `role_key`    VARCHAR(128) NOT NULL COMMENT '角色标识（如 admin / operator）',
    `sort_order`  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1=启用 0=禁用',
    `remark`      VARCHAR(512) DEFAULT NULL COMMENT '备注',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=已删除',
    `version`     INT          NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by`  BIGINT       DEFAULT NULL COMMENT '创建人ID',
    `updated_by`  BIGINT       DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_role_key` (`tenant_id`, `role_key`, `deleted`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单/权限表（菜单和权限统一存储，通过 type 区分）
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id`          BIGINT       NOT NULL COMMENT '雪花主键',
    `tenant_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '租户ID',
    `parent_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '父菜单ID，0表示顶级',
    `menu_name`   VARCHAR(64)  NOT NULL COMMENT '菜单/权限名称',
    `menu_type`   TINYINT      NOT NULL COMMENT '类型：1=目录 2=菜单 3=按钮/权限',
    `path`        VARCHAR(256) DEFAULT NULL COMMENT '路由地址',
    `component`   VARCHAR(256) DEFAULT NULL COMMENT '前端组件路径',
    `icon`        VARCHAR(128) DEFAULT NULL COMMENT '图标',
    `permission`  VARCHAR(128) DEFAULT NULL COMMENT '权限标识（如 user:list / user:add）',
    `sort_order`  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `visible`     TINYINT      NOT NULL DEFAULT 1 COMMENT '是否可见：1=可见 0=隐藏',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1=启用 0=禁用',
    `remark`      VARCHAR(512) DEFAULT NULL COMMENT '备注',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `version`     INT          NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by`  BIGINT       DEFAULT NULL COMMENT '创建人ID',
    `updated_by`  BIGINT       DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单/权限表';

-- 用户-角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id`          BIGINT   NOT NULL COMMENT '雪花主键',
    `tenant_id`   BIGINT   NOT NULL DEFAULT 0 COMMENT '租户ID',
    `user_id`     BIGINT   NOT NULL COMMENT '用户ID',
    `role_id`     BIGINT   NOT NULL COMMENT '角色ID',
    `deleted`     TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`  BIGINT   DEFAULT NULL COMMENT '创建人ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`tenant_id`, `user_id`, `role_id`, `deleted`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';

-- 角色-菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `id`          BIGINT   NOT NULL COMMENT '雪花主键',
    `tenant_id`   BIGINT   NOT NULL DEFAULT 0 COMMENT '租户ID',
    `role_id`     BIGINT   NOT NULL COMMENT '角色ID',
    `menu_id`     BIGINT   NOT NULL COMMENT '菜单/权限ID',
    `deleted`     TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`  BIGINT   DEFAULT NULL COMMENT '创建人ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`tenant_id`, `role_id`, `menu_id`, `deleted`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单关联表';

-- 初始化默认数据：超级管理员角色
INSERT INTO `sys_role` (`id`, `tenant_id`, `role_name`, `role_key`, `sort_order`, `status`, `remark`)
VALUES (1, 0, '超级管理员', 'super_admin', 1, 1, '系统内置超级管理员，拥有全部权限');