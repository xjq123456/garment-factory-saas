-- ============================================================
-- 优惠券模板表
-- ============================================================
CREATE TABLE IF NOT EXISTS `mk_coupon` (
    `id`              BIGINT       NOT NULL COMMENT '主键（雪花）',
    `tenant_id`       BIGINT       NOT NULL COMMENT '租户ID',
    `name`            VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    `type`            TINYINT      NOT NULL DEFAULT 0 COMMENT '类型：0-满减券 1-折扣券 2-免运费券',
    `discount_value`  DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '面值/折扣率',
    `min_amount`      DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '最低消费金额',
    `total_quantity`  INT          NOT NULL DEFAULT 0 COMMENT '发放总量，0=不限量',
    `issued_quantity` INT          NOT NULL DEFAULT 0 COMMENT '已发放数量',
    `per_user_limit`  INT          NOT NULL DEFAULT 1 COMMENT '每人限领数量',
    `start_time`      DATETIME     NOT NULL COMMENT '有效期开始',
    `end_time`        DATETIME     NOT NULL COMMENT '有效期结束',
    `status`          TINYINT      NOT NULL DEFAULT 0 COMMENT '状态：0-草稿 1-已发布 2-已停用',
    `description`     VARCHAR(500) DEFAULT NULL COMMENT '使用说明',
    `deleted`         TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `version`         INT          NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`      BIGINT       DEFAULT NULL COMMENT '创建人',
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`      BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_status` (`tenant_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券模板表';

-- ============================================================
-- 优惠券领取记录表
-- ============================================================
CREATE TABLE IF NOT EXISTS `mk_coupon_record` (
    `id`          BIGINT   NOT NULL COMMENT '主键（雪花）',
    `tenant_id`   BIGINT   NOT NULL COMMENT '租户ID',
    `coupon_id`   BIGINT   NOT NULL COMMENT '优惠券ID',
    `user_id`     BIGINT   NOT NULL COMMENT '领取用户ID',
    `status`      TINYINT  NOT NULL DEFAULT 0 COMMENT '状态：0-已领取 1-已使用 2-已过期',
    `used_at`     DATETIME DEFAULT NULL COMMENT '使用时间',
    `order_id`    BIGINT   DEFAULT NULL COMMENT '关联订单ID',
    `deleted`     TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `version`     INT      NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`  BIGINT   DEFAULT NULL COMMENT '创建人',
    `updated_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`  BIGINT   DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `idx_coupon_id` (`tenant_id`, `coupon_id`),
    KEY `idx_user_id`   (`tenant_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券领取记录表';

-- ============================================================
-- 促销活动表
-- ============================================================
CREATE TABLE IF NOT EXISTS `mk_promotion` (
    `id`          BIGINT       NOT NULL COMMENT '主键（雪花）',
    `tenant_id`   BIGINT       NOT NULL COMMENT '租户ID',
    `name`        VARCHAR(200) NOT NULL COMMENT '活动名称',
    `description` VARCHAR(1000) DEFAULT NULL COMMENT '活动描述',
    `type`        TINYINT      NOT NULL DEFAULT 0 COMMENT '类型：0-满减 1-折扣 2-秒杀 3-团购',
    `rule_json`   TEXT         DEFAULT NULL COMMENT '活动规则JSON',
    `start_time`  DATETIME     NOT NULL COMMENT '活动开始时间',
    `end_time`    DATETIME     NOT NULL COMMENT '活动结束时间',
    `status`      TINYINT      NOT NULL DEFAULT 0 COMMENT '状态：0-草稿 1-已发布 2-进行中 3-已结束 4-已取消',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `version`     INT          NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`  BIGINT       DEFAULT NULL COMMENT '创建人',
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`  BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_status` (`tenant_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='促销活动表';

-- ============================================================
-- 客户表（CRM）
-- ============================================================
CREATE TABLE IF NOT EXISTS `mk_customer` (
    `id`                BIGINT       NOT NULL COMMENT '主键（雪花）',
    `tenant_id`         BIGINT       NOT NULL COMMENT '租户ID',
    `name`              VARCHAR(200) NOT NULL COMMENT '客户名称',
    `contact_name`      VARCHAR(100) DEFAULT NULL COMMENT '联系人',
    `phone`             VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    `email`             VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `address`           VARCHAR(500) DEFAULT NULL COMMENT '地址',
    `type`              TINYINT      NOT NULL DEFAULT 0 COMMENT '类型：0-个人 1-企业 2-工厂加盟商',
    `level`             TINYINT      NOT NULL DEFAULT 0 COMMENT '等级：0-普通 1-银牌 2-金牌 3-钻石',
    `source`            VARCHAR(50)  DEFAULT NULL COMMENT '来源渠道',
    `remark`            VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `total_consumption` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '累计消费金额',
    `last_order_at`     DATETIME     DEFAULT NULL COMMENT '最后交易时间',
    `deleted`           TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `version`           INT          NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `created_at`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`        BIGINT       DEFAULT NULL COMMENT '创建人',
    `updated_at`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`        BIGINT       DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_level` (`tenant_id`, `level`),
    KEY `idx_tenant_type`  (`tenant_id`, `type`),
    KEY `idx_phone`        (`tenant_id`, `phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表（CRM）';