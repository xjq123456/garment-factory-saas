# Garment Factory SaaS — 服装工厂 SaaS 管理系统

> 面向中小型服装工厂的企业级 SaaS 管理平台，基于 Spring Boot 3 + Spring Cloud 2023 微服务架构，采用 DDD（领域驱动设计）分层风格开发。

---

## 📐 项目架构

```
garment-factory-saas
├── gateway-service          # API 网关（Spring Cloud Gateway）
├── common-infrastructure    # 公共基础设施模块（通用组件、工具类）
├── user-service             # 用户服务
├── rbac-service             # 权限管理服务（RBAC）
├── style-service            # 款式管理服务
├── order-service            # 订单管理服务
├── production-service       # 生产管理服务
├── inventory-service        # 库存管理服务
├── payment-service          # 支付管理服务
├── device-service           # 设备管理服务
├── marketing-service        # 营销管理服务
├── ecommerce-adapter        # 电商对接服务
├── live-service             # 直播管理服务
├── report-service           # 报表分析服务
├── search-service           # 搜索服务
├── print-service            # 打印服务
├── ai-advisor-service       # AI 选品顾问服务
├── ai-selection-service     # AI 智能选品服务
├── deploy/                  # 部署配置（Kubernetes）
├── scripts/                 # CI/CD 脚本
└── pom.xml                  # 父 POM（模块聚合）
```

## 🏗️ 技术栈

| 类别 | 技术选型 |
|------|---------|
| **语言/框架** | Java 17 / Spring Boot 3.2.x / Spring Cloud 2023 |
| **API 网关** | Spring Cloud Gateway (WebFlux) |
| **数据库** | MySQL 8.0 |
| **ORM** | MyBatis-Plus 3.5.x |
| **数据库迁移** | Flyway |
| **缓存** | Redis (Spring Data Redis) |
| **认证授权** | Spring Security + JWT + RBAC |
| **多租户** | ThreadLocal 租户上下文 + MyBatis-Plus 拦截器自动注入 |
| **容器化** | Docker + Kubernetes |
| **CI/CD** | GitHub Actions + 脚本自动化 |

## 🧱 DDD 分层架构

每个业务微服务均遵循 DDD 四层架构：

```
com.garment.{module}
├── interfaces/          # 用户接口层（REST Controller、DTO 转换）
├── application/         # 应用层（AppService、Command、编排领域对象）
├── domain/              # 领域层（Entity、ValueObject、Repository 接口、DomainEvent）
├── infrastructure/      # 基础设施层（Repository 实现、MyBatis Mapper、DO 转换）
└── shared/              # 模块内共享配置（SecurityConfig 等）
```

**核心基类**（`common-infrastructure`）：
- `AggregateRoot` — 聚合根基类，支持领域事件注册
- `ValueObject` — 值对象基类
- `DomainEvent` — 领域事件基类
- `BaseEntity` — 基础实体（审计字段：createTime, updateTime, createBy, updateBy）
- `BizException` — 业务异常
- `TenantContext` — 租户上下文（ThreadLocal）
- `JwtUtils` — JWT 工具类
- `R<T>` / `PageResult<T>` — 统一响应封装

## 🔧 微服务详情

### ✅ 已完成

| 服务 | 端口 | 说明 | 完成度 |
|------|------|------|--------|
| **gateway-service** | 8080 | API 网关，JWT 鉴权过滤器，路由转发 | ✅ 完整 |
| **common-infrastructure** | — | 公共组件库（自动装配） | ✅ 完整 |
| **user-service** | 8081 | 用户注册/登录、密码加密、JWT Token | ✅ 完整 |
| **rbac-service** | 8082 | 角色管理、菜单权限、角色-菜单绑定 | ✅ 完整 |

### 🔨 领域层已完成

| 服务 | 说明 | 完成度 |
|------|------|--------|
| **style-service** | 款式管理、SKU、BOM、分类 | 🟡 领域层大部分完成，需补全 Application/Infrastructure/Interfaces 层 |

### 📋 待开发（仅有模块骨架）

| 服务 | 说明 |
|------|------|
| **order-service** | 订单管理（下单、订单状态流转、跟单） |
| **production-service** | 生产管理（工单、排产、工序、报工、质检） |
| **inventory-service** | 库存管理（入库、出库、盘点、库存预警） |
| **payment-service** | 支付管理（收款、付款、对账） |
| **device-service** | 设备管理（设备台账、维保、OEE） |
| **marketing-service** | 营销管理（客户、报价、打样） |
| **ecommerce-adapter** | 电商平台对接（淘宝/1688/拼多多订单同步） |
| **live-service** | 直播管理（直播间、选品、订单关联） |
| **report-service** | 报表分析（产能、库存、财务报表） |
| **search-service** | 全文检索（ES 搜索订单/款式/客户） |
| **print-service** | 打印服务（吊牌、洗唛、快递单、生产单） |
| **ai-advisor-service** | AI 选品顾问（智能推荐、市场分析） |
| **ai-selection-service** | AI 智能选品（自动选款、趋势预测） |

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

### 本地启动

```bash
# 1. 克隆项目
git clone https://github.com/xjq123456/garment-factory-saas.git
cd garment-factory-saas

# 2. 编译所有模块
mvn clean install -DskipTests

# 3. 启动基础设施（按需）
# 启动 MySQL、Redis

# 4. 启动网关
cd gateway-service && mvn spring-boot:run

# 5. 启动业务服务
cd user-service && mvn spring-boot:run
cd rbac-service && mvn spring-boot:run
```

### Docker 部署

```bash
# 构建镜像
./scripts/ci-docker-build.sh

# Kubernetes 部署
kubectl apply -k deploy/kubernetes/
```

## 📖 API 概览

所有 API 经由 **gateway-service** (8080 端口) 统一入口：

| 服务 | 路由前缀 | 说明 |
|------|---------|------|
| user-service | `/api/user/**` | 用户注册、登录、信息管理 |
| rbac-service | `/api/rbac/**` | 角色、菜单、权限管理 |
| style-service | `/api/style/**` | 款式、SKU、BOM、分类管理 |
| order-service | `/api/order/**` | 订单管理（待开发） |
| production-service | `/api/production/**` | 生产管理（待开发） |
| inventory-service | `/api/inventory/**` | 库存管理（待开发） |
| payment-service | `/api/payment/**` | 支付管理（待开发） |
| device-service | `/api/device/**` | 设备管理（待开发） |
| marketing-service | `/api/marketing/**` | 营销管理（待开发） |

## 🗺️ 开发路线图

### Phase 1 — 基础平台 ✅
- [x] 项目骨架 & 公共组件
- [x] 用户服务（注册/登录/JWT）
- [x] 权限服务（RBAC）
- [x] API 网关

### Phase 2 — 核心业务 🚧
- [ ] 款式管理服务（补全 Application/Infrastructure/Interfaces 层）
- [ ] 订单管理服务
- [ ] 生产管理服务
- [ ] 库存管理服务

### Phase 3 — 财务 & 设备
- [ ] 支付管理服务
- [ ] 设备管理服务
- [ ] 报表分析服务

### Phase 4 — 营销 & 电商
- [ ] 营销管理服务
- [ ] 电商对接服务
- [ ] 直播管理服务

### Phase 5 — 智能化
- [ ] 搜索服务（ES 集成）
- [ ] 打印服务
- [ ] AI 选品顾问
- [ ] AI 智能选品

## 📄 License

[MIT License](LICENSE)