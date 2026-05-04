-- ============================================================
-- 库存服务数据库初始化脚本
-- 服装行业SaaS - 库存管理模块
-- ============================================================

-- 仓库表
CREATE TABLE inv_warehouse (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '仓库ID',
    tenant_id       BIGINT          NOT NULL DEFAULT 0 COMMENT '租户ID',
    warehouse_code  VARCHAR(32)     NOT NULL COMMENT '仓库编码',
    warehouse_name  VARCHAR(64)     NOT NULL COMMENT '仓库名称',
    warehouse_type  TINYINT         NOT NULL DEFAULT 1 COMMENT '仓库类型: 1-成品仓 2-原材料仓 3-半成品仓',
    contact_person  VARCHAR(32)     DEFAULT NULL COMMENT '联系人',
    contact_phone   VARCHAR(20)     DEFAULT NULL COMMENT '联系电话',
    address         VARCHAR(256)    DEFAULT NULL COMMENT '仓库地址',
    status          TINYINT         NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    remark          VARCHAR(512)    DEFAULT NULL COMMENT '备注',
    create_by       BIGINT          DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT          DEFAULT NULL COMMENT '更新人ID',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    UNIQUE KEY uk_tenant_code (tenant_id, warehouse_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库表';

-- 成品库存表 (SKU维度)
CREATE TABLE inv_stock (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '库存ID',
    tenant_id       BIGINT          NOT NULL DEFAULT 0 COMMENT '租户ID',
    warehouse_id    BIGINT          NOT NULL COMMENT '仓库ID',
    sku_id          BIGINT          NOT NULL COMMENT 'SKU ID',
    style_id        BIGINT          DEFAULT NULL COMMENT '款号ID',
    style_code      VARCHAR(32)     DEFAULT NULL COMMENT '款号编码',
    color           VARCHAR(32)     DEFAULT NULL COMMENT '颜色',
    size            VARCHAR(16)     DEFAULT NULL COMMENT '尺码',
    total_qty       INT             NOT NULL DEFAULT 0 COMMENT '总库存数量',
    available_qty   INT             NOT NULL DEFAULT 0 COMMENT '可用库存数量',
    locked_qty      INT             NOT NULL DEFAULT 0 COMMENT '锁定库存数量(待出库)',
    safety_stock    INT             DEFAULT 0 COMMENT '安全库存',
    unit            VARCHAR(16)     DEFAULT '件' COMMENT '单位',
    remark          VARCHAR(512)    DEFAULT NULL COMMENT '备注',
    create_by       BIGINT          DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT          DEFAULT NULL COMMENT '更新人ID',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    UNIQUE KEY uk_tenant_warehouse_sku (tenant_id, warehouse_id, sku_id),
    INDEX idx_sku_id (sku_id),
    INDEX idx_style_id (style_id),
    INDEX idx_style_code (style_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成品库存表';

-- 原材料库存表
CREATE TABLE inv_material_stock (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '库存ID',
    tenant_id       BIGINT          NOT NULL DEFAULT 0 COMMENT '租户ID',
    warehouse_id    BIGINT          NOT NULL COMMENT '仓库ID',
    material_id     BIGINT          NOT NULL COMMENT '原材料ID',
    material_code   VARCHAR(64)     DEFAULT NULL COMMENT '原材料编码',
    material_name   VARCHAR(128)    DEFAULT NULL COMMENT '原材料名称',
    material_type   TINYINT         DEFAULT 1 COMMENT '原材料类型: 1-面料 2-辅料 3-包装材料',
    total_qty       DECIMAL(12,2)   NOT NULL DEFAULT 0 COMMENT '总库存数量',
    available_qty   DECIMAL(12,2)   NOT NULL DEFAULT 0 COMMENT '可用库存数量',
    locked_qty      DECIMAL(12,2)   NOT NULL DEFAULT 0 COMMENT '锁定库存数量',
    safety_stock    DECIMAL(12,2)   DEFAULT 0 COMMENT '安全库存',
    unit            VARCHAR(16)     DEFAULT NULL COMMENT '单位(米/码/公斤/个等)',
    batch_no        VARCHAR(64)     DEFAULT NULL COMMENT '批次号',
    remark          VARCHAR(512)    DEFAULT NULL COMMENT '备注',
    create_by       BIGINT          DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT          DEFAULT NULL COMMENT '更新人ID',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    UNIQUE KEY uk_tenant_warehouse_material_batch (tenant_id, warehouse_id, material_id, batch_no),
    INDEX idx_material_id (material_id),
    INDEX idx_material_code (material_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='原材料库存表';

-- 成品库存变动日志表
CREATE TABLE inv_stock_log (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    tenant_id       BIGINT          NOT NULL DEFAULT 0 COMMENT '租户ID',
    warehouse_id    BIGINT          NOT NULL COMMENT '仓库ID',
    sku_id          BIGINT          NOT NULL COMMENT 'SKU ID',
    change_type     TINYINT         NOT NULL COMMENT '变动类型: 1-入库 2-出库 3-调拨入 4-调拨出 5-盘点盈 6-盘点亏 7-锁定 8-解锁',
    change_qty      INT             NOT NULL COMMENT '变动数量(正数为增加,负数为减少)',
    before_qty      INT             NOT NULL COMMENT '变动前数量',
    after_qty       INT             NOT NULL COMMENT '变动后数量',
    biz_type        VARCHAR(32)     DEFAULT NULL COMMENT '业务类型(采购入库/销售出库/生产入库等)',
    biz_no          VARCHAR(64)     DEFAULT NULL COMMENT '业务单号',
    operator_id     BIGINT          DEFAULT NULL COMMENT '操作人ID',
    operator_name   VARCHAR(32)     DEFAULT NULL COMMENT '操作人姓名',
    remark          VARCHAR(512)    DEFAULT NULL COMMENT '备注',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_tenant_warehouse_sku (tenant_id, warehouse_id, sku_id),
    INDEX idx_biz_no (biz_no),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成品库存变动日志表';

-- 原材料库存变动日志表
CREATE TABLE inv_material_stock_log (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    tenant_id       BIGINT          NOT NULL DEFAULT 0 COMMENT '租户ID',
    warehouse_id    BIGINT          NOT NULL COMMENT '仓库ID',
    material_id     BIGINT          NOT NULL COMMENT '原材料ID',
    change_type     TINYINT         NOT NULL COMMENT '变动类型: 1-入库 2-出库 3-调拨入 4-调拨出 5-盘点盈 6-盘点亏',
    change_qty      DECIMAL(12,2)   NOT NULL COMMENT '变动数量',
    before_qty      DECIMAL(12,2)   NOT NULL COMMENT '变动前数量',
    after_qty       DECIMAL(12,2)   NOT NULL COMMENT '变动后数量',
    biz_type        VARCHAR(32)     DEFAULT NULL COMMENT '业务类型',
    biz_no          VARCHAR(64)     DEFAULT NULL COMMENT '业务单号',
    batch_no        VARCHAR(64)     DEFAULT NULL COMMENT '批次号',
    operator_id     BIGINT          DEFAULT NULL COMMENT '操作人ID',
    operator_name   VARCHAR(32)     DEFAULT NULL COMMENT '操作人姓名',
    remark          VARCHAR(512)    DEFAULT NULL COMMENT '备注',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_tenant_warehouse_material (tenant_id, warehouse_id, material_id),
    INDEX idx_biz_no (biz_no),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='原材料库存变动日志表';