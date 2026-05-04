-- ============================================================
-- style-service 数据库初始化脚本
-- 款式/商品中心：款式、SKU、BOM、分类
-- ============================================================

-- 款式分类表
CREATE TABLE IF NOT EXISTS `biz_style_category` (
    `id`          BIGINT       NOT NULL COMMENT '雪花主键',
    `tenant_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '租户ID',
    `parent_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '父分类ID（0=顶级）',
    `name`        VARCHAR(64)  NOT NULL COMMENT '分类名称',
    `sort_order`  INT          NOT NULL DEFAULT 0 COMMENT '排序号',
    `icon`        VARCHAR(255) DEFAULT NULL COMMENT '分类图标URL',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态（0=禁用 1=启用）',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0=正常 1=已删除）',
    `version`     INT          NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `created_by`  BIGINT       DEFAULT NULL COMMENT '创建人ID',
    `updated_by`  BIGINT       DEFAULT NULL COMMENT '更新人ID',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_parent` (`tenant_id`, `parent_id`),
    KEY `idx_tenant_name` (`tenant_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='款式分类表';

-- 款式主表
CREATE TABLE IF NOT EXISTS `biz_style` (
    `id`              BIGINT        NOT NULL COMMENT '雪花主键',
    `tenant_id`       BIGINT        NOT NULL DEFAULT 0 COMMENT '租户ID',
    `style_code`      VARCHAR(64)   NOT NULL COMMENT '款号（租户内唯一）',
    `style_name`      VARCHAR(128)  NOT NULL COMMENT '款式名称',
    `category_id`     BIGINT        DEFAULT NULL COMMENT '分类ID',
    `season`          VARCHAR(32)   DEFAULT NULL COMMENT '季节（如 2024春夏）',
    `year`            VARCHAR(16)   DEFAULT NULL COMMENT '年份',
    `pattern_type`    VARCHAR(32)   DEFAULT NULL COMMENT '版型（修身/宽松/直筒等）',
    `craft_desc`      TEXT          DEFAULT NULL COMMENT '工艺说明',
    `design_sketch`   VARCHAR(512)  DEFAULT NULL COMMENT '设计图URL',
    `main_image`      VARCHAR(512)  DEFAULT NULL COMMENT '主图URL',
    `images`          JSON          DEFAULT NULL COMMENT '图片列表JSON',
    `tags`            JSON          DEFAULT NULL COMMENT '标签JSON数组',
    `status`          TINYINT       NOT NULL DEFAULT 0 COMMENT '状态（0=草稿 1=已发布 2=已审核 3=已停用）',
    `remark`          VARCHAR(512)  DEFAULT NULL COMMENT '备注',
    `deleted`         TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除（0=正常 1=已删除）',
    `version`         INT           NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `created_by`      BIGINT        DEFAULT NULL COMMENT '创建人ID',
    `updated_by`      BIGINT        DEFAULT NULL COMMENT '更新人ID',
    `created_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_style_code` (`tenant_id`, `style_code`),
    KEY `idx_tenant_category` (`tenant_id`, `category_id`),
    KEY `idx_tenant_status` (`tenant_id`, `status`),
    KEY `idx_tenant_season` (`tenant_id`, `season`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='款式主表';

-- SKU 表（尺码×颜色组合）
CREATE TABLE IF NOT EXISTS `biz_style_sku` (
    `id`              BIGINT       NOT NULL COMMENT '雪花主键',
    `tenant_id`       BIGINT       NOT NULL DEFAULT 0 COMMENT '租户ID',
    `style_id`        BIGINT       NOT NULL COMMENT '款式ID',
    `sku_code`        VARCHAR(64)  NOT NULL COMMENT 'SKU编码（租户内唯一）',
    `color`           VARCHAR(32)  NOT NULL COMMENT '颜色',
    `color_code`      VARCHAR(16)  DEFAULT NULL COMMENT '颜色编码',
    `size`            VARCHAR(16)  NOT NULL COMMENT '尺码',
    `size_type`       VARCHAR(16)  DEFAULT NULL COMMENT '尺码类型（SML/数字/欧码等）',
    `barcode`         VARCHAR(64)  DEFAULT NULL COMMENT '条码',
    `weight`          DECIMAL(8,2) DEFAULT NULL COMMENT '重量(g)',
    `extra_price`     DECIMAL(12,2) DEFAULT 0.00 COMMENT 'SKU加价',
    `status`          TINYINT      NOT NULL DEFAULT 1 COMMENT '状态（0=禁用 1=启用）',
    `sort_order`      INT          NOT NULL DEFAULT 0 COMMENT '排序号',
    `deleted`         TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `version`         INT          NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `created_by`      BIGINT       DEFAULT NULL,
    `updated_by`      BIGINT       DEFAULT NULL,
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_sku_code` (`tenant_id`, `sku_code`),
    KEY `idx_style_id` (`style_id`),
    KEY `idx_tenant_style_color_size` (`tenant_id`, `style_id`, `color`, `size`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='款式SKU表';

-- BOM 物料清单主表
CREATE TABLE IF NOT EXISTS `biz_style_bom` (
    `id`              BIGINT        NOT NULL COMMENT '雪花主键',
    `tenant_id`       BIGINT        NOT NULL DEFAULT 0 COMMENT '租户ID',
    `style_id`        BIGINT        NOT NULL COMMENT '款式ID',
    `bom_code`        VARCHAR(64)   NOT NULL COMMENT 'BOM编码',
    `bom_name`        VARCHAR(128)  NOT NULL COMMENT 'BOM名称',
    `version_no`      VARCHAR(32)   NOT NULL DEFAULT 'V1.0' COMMENT '版本号',
    `status`          TINYINT       NOT NULL DEFAULT 0 COMMENT '状态（0=草稿 1=已确认 2=已废弃）',
    `remark`          VARCHAR(512)  DEFAULT NULL COMMENT '备注',
    `deleted`         TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `version`         INT           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `created_by`      BIGINT        DEFAULT NULL,
    `updated_by`      BIGINT        DEFAULT NULL,
    `created_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_bom_code` (`tenant_id`, `bom_code`),
    KEY `idx_style_id` (`style_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='BOM物料清单主表';

-- BOM 物料明细表
CREATE TABLE IF NOT EXISTS `biz_style_bom_item` (
    `id`              BIGINT        NOT NULL COMMENT '雪花主键',
    `tenant_id`       BIGINT        NOT NULL DEFAULT 0 COMMENT '租户ID',
    `bom_id`          BIGINT        NOT NULL COMMENT 'BOM主表ID',
    `material_name`   VARCHAR(128)  NOT NULL COMMENT '物料名称',
    `material_code`   VARCHAR(64)   DEFAULT NULL COMMENT '物料编码',
    `material_type`   VARCHAR(32)   DEFAULT NULL COMMENT '物料类型（面料/辅料/包装等）',
    `specification`   VARCHAR(128)  DEFAULT NULL COMMENT '规格说明',
    `unit`            VARCHAR(16)   NOT NULL COMMENT '计量单位（米/码/个/kg等）',
    `quantity`        DECIMAL(12,4) NOT NULL COMMENT '用量',
    `unit_price`      DECIMAL(12,4) DEFAULT NULL COMMENT '单价',
    `supplier`        VARCHAR(128)  DEFAULT NULL COMMENT '供应商',
    `color`           VARCHAR(32)   DEFAULT NULL COMMENT '颜色',
    `remark`          VARCHAR(255)  DEFAULT NULL COMMENT '备注',
    `sort_order`      INT           NOT NULL DEFAULT 0 COMMENT '排序号',
    `deleted`         TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `version`         INT           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `created_by`      BIGINT        DEFAULT NULL,
    `updated_by`      BIGINT        DEFAULT NULL,
    `created_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_bom_id` (`bom_id`),
    KEY `idx_tenant_material` (`tenant_id`, `material_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='BOM物料明细表';