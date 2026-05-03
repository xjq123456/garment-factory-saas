# Garment Factory SaaS (服装工厂数字化平台)

## 1. 项目简介
本项目是一个专门为服装加工行业设计的 SaaS 平台，采用微服务架构与 **DDD (领域驱动设计)** 模式。系统旨在解决服装生产中从订单下达、物料管理到工序追踪的全流程数字化问题。

## 2. 核心技术栈
*   **后端核心**: Spring Cloud Alibaba, Spring Boot 3.x
*   **网关层**: Spring Cloud Gateway
*   **认证鉴权**: Spring Security + JWT
*   **持久层**: MyBatis Plus (集成多租户插件)
*   **数据库**: MySQL (全平台共享数据库，逻辑隔离)
*   **服务治理**: Nacos (注册中心 + 配置中心)

## 3. 微服务模块定义与职责

| 模块名 | 预设端口 | 核心职责 (Context) |
| :--- | :--- | :--- |
| **`api-gateway`** | 8080 | 统一入口、Token 校验、租户 ID 提取、动态路由、跨域处理 |
| **`user-service`** | 8100 | **用户服务**: 账号、租户成员、身份凭证 (Identity) |
| **`rbac-service`** | 8110 | **权限服务**: 角色、权限、资源与授权策略 (RBAC)；端口与 `report-service`(8700) 错开 |
| **`production-service`** | 8200 | **生产中心**: 工序流转、生产排期等 |
| **`inventory-service`** | 8300 | **库存中心**: 面料、辅料、成品的入库、出库与盘点 |
| **`order-service`** | 8400 | **订单中心**: 加工单与销售订单生命周期 |
| **`marketing-service`** | 8500 | **营销**: 优惠券、工厂加盟活动、客户 CRM |
| **`payment-service`** | 8520 | **支付与对账**（原 8500 让给营销） |
| **`live-service`** | 8550 | **直播**: 实时数据抓取、直播间爆单预警 |
| **`ecommerce-adapter`** | 8600 | **电商对接**: 抖音、淘宝、TikTok 等 API 桥接（`print-service` 迁至 8620） |
| **`print-service`** | 8620 | **打印任务**（原 8600 让给电商适配） |
| **`style-service`** | 8650 | **款式**: 款号、版型、工艺属性 |
| **`device-service`** | 8660 | **物理设备**: 车间终端、采集器、物联网接入 |
| **`report-service`** | 8700 | **异步报表导出**: 计件工资、财务损益等 |
| **`ai-advisor-service`** | 8800 | **企业 AI 顾问**: 基于生产数据的决策建议 |
| **`ai-selection-service`** | 8900 | **AI 选品**: 流行趋势、以图搜布 |
| **`search-service`** | 9000 | **全文搜索** (Elasticsearch)：款式、订单、库存检索 |
| **`common-infrastructure`** | - | **公共基础设施**: 通用工具、多租户、全局异常等 |

---

## 4. DDD 命名与代码结构规范 (Cursor 参照规范)

每个微服务内部应严格遵循以下包结构，确保业务逻辑与技术实现解耦：
```text
com.garment.{service_name}
├── interfaces          # 接口层: REST Controllers, Web 入口
├── application         # 应用层: 组装领域服务, 处理 DTO 转化, 事务控制
├── domain              # 领域层: 业务核心 (聚合根、实体、领域服务、Repository 接口)
│   ├── identity        # (仅限 user-service) 身份与凭证
│   ├── role            # (仅限 rbac-service) 角色子域
│   ├── permission      # (仅限 rbac-service) 权限子域
│   └── shared          # 共享领域组件
├── infrastructure      # 基础设施层: Mapper、外部适配
└── config              # 配置层: Spring @Configuration 与属性绑定

