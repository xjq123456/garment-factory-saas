-- 加工单（生产工单）
CREATE TABLE production_order (
    id              BIGINT          NOT NULL PRIMARY KEY,
    tenant_id       BIGINT          NOT NULL,
    order_no        VARCHAR(64)     NOT NULL,
    customer_id     BIGINT,
    customer_name   VARCHAR(128),
    style_id        BIGINT,
    style_name      VARCHAR(128),
    total_quantity  DECIMAL(12,2)   DEFAULT 0,
    unit            VARCHAR(16)     DEFAULT '件',
    unit_price      DECIMAL(12,2),
    total_amount    DECIMAL(14,2),
    delivery_date   DATE,
    status          VARCHAR(32)     NOT NULL DEFAULT 'DRAFT',
    remark          VARCHAR(512),
    deleted         INT             DEFAULT 0,
    version         INT             DEFAULT 0,
    created_by      BIGINT,
    created_at      DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_by      BIGINT,
    updated_at      DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_order_no (order_no, tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 加工单明细
CREATE TABLE production_order_item (
    id                  BIGINT          NOT NULL PRIMARY KEY,
    tenant_id           BIGINT          NOT NULL,
    order_id            BIGINT          NOT NULL,
    sku_id              BIGINT,
    sku_code            VARCHAR(64),
    color               VARCHAR(32),
    size                VARCHAR(32),
    quantity            DECIMAL(12,2)   DEFAULT 0,
    completed_quantity  DECIMAL(12,2)   DEFAULT 0,
    remark              VARCHAR(256),
    deleted             INT             DEFAULT 0,
    version             INT             DEFAULT 0,
    created_by          BIGINT,
    created_at          DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_by          BIGINT,
    updated_at          DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 销售单
CREATE TABLE sales_order (
    id                BIGINT          NOT NULL PRIMARY KEY,
    tenant_id         BIGINT          NOT NULL,
    order_no          VARCHAR(64)     NOT NULL,
    customer_id       BIGINT,
    customer_name     VARCHAR(128),
    total_amount      DECIMAL(14,2)   DEFAULT 0,
    paid_amount       DECIMAL(14,2)   DEFAULT 0,
    delivery_date     DATE,
    status            VARCHAR(32)     NOT NULL DEFAULT 'DRAFT',
    shipping_address  VARCHAR(512),
    remark            VARCHAR(512),
    deleted           INT             DEFAULT 0,
    version           INT             DEFAULT 0,
    created_by        BIGINT,
    created_at        DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_by        BIGINT,
    updated_at        DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_sales_order_no (order_no, tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 销售单明细
CREATE TABLE sales_order_item (
    id          BIGINT          NOT NULL PRIMARY KEY,
    tenant_id   BIGINT          NOT NULL,
    order_id    BIGINT          NOT NULL,
    sku_id      BIGINT,
    sku_code    VARCHAR(64),
    color       VARCHAR(32),
    size        VARCHAR(32),
    quantity    DECIMAL(12,2)   DEFAULT 0,
    unit_price  DECIMAL(12,2),
    amount      DECIMAL(14,2),
    remark      VARCHAR(256),
    deleted     INT             DEFAULT 0,
    version     INT             DEFAULT 0,
    created_by  BIGINT,
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_by  BIGINT,
    updated_at  DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_sales_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
