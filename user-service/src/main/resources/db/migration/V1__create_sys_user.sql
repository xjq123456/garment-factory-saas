-- ============================================================
-- 用户表 sys_user
-- 支持多租户（tenant_id 字段）与软删除（status 字段）
-- ============================================================
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id`            BIGINT       NOT NULL COMMENT '雪花主键',
    `tenant_id`     BIGINT       NOT NULL DEFAULT 0 COMMENT '租户ID',
    `username`      VARCHAR(32)  NOT NULL COMMENT '用户名（全局唯一）',
    `password`      VARCHAR(128) NOT NULL COMMENT '密码（BCrypt加密）',
    `nickname`      VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
    `avatar`        VARCHAR(256) DEFAULT NULL COMMENT '头像URL',
    `phone`         VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `email`         VARCHAR(64)  DEFAULT NULL COMMENT '邮箱',
    `status`        VARCHAR(16)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/FROZEN/DELETED',
    `last_login_at` DATETIME     DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(45)  DEFAULT NULL COMMENT '最后登录IP',
    `created_by`    BIGINT       DEFAULT NULL COMMENT '创建人ID',
    `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by`    BIGINT       DEFAULT NULL COMMENT '更新人ID',
    `updated_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_phone` (`phone`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';