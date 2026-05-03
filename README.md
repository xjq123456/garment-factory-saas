# garment-factory-saas

面向服装加工与工厂协同的 **多租户 SaaS 平台**，采用 **Spring Cloud Alibaba** 与 **DDD（领域驱动设计）** 拆分子域。当前仓库以 **Maven 多模块** 形式管理网关、各业务微服务与公共基础设施；业务实现仍在持续迭代中，模块与端口以代码与配置为准。

## 项目目标

覆盖从 **用户与权限、订单与生产、库存与款式** 到 **营销支付、电商与直播、设备与报表**，并预留 **搜索、企业 AI 顾问与 AI 选品** 等增强能力，支撑服装厂数字化管理、外部渠道对接与数据化决策。

## 技术栈（规划）

- **运行时**: JDK 17，Spring Boot 3.x  
- **微服务与治理**: Spring Cloud、Spring Cloud Alibaba（Nacos 注册/配置）、Spring Cloud Gateway  
- **服务间通信**: REST（经网关统一路由）；后续可按需引入 OpenFeign 等  
- **数据与搜索**: MySQL、MyBatis Plus（规划）；Elasticsearch（`search-service` 接入点）  
- **文档**: Knife4j（网关聚合 OpenAPI 3）

更细的约定（如 DDD 分包、网关与 Knife4j 配置）见 [gateway-service/README.md](gateway-service/README.md)。

## 微服务模块一览

| 模块 | 默认端口 | 职责摘要 |
|------|-----------|----------|
| `gateway-service` | 8080 | 统一入口、路由、跨域与可观测性相关配置 |
| `user-service` | 8100 | 用户与身份：账号、租户成员、凭证 |
| `rbac-service` | 8110 | 权限：角色、权限、资源与授权策略 |
| `production-service` | 8200 | 生产：工序、排期与车间执行 |
| `inventory-service` | 8300 | 库存：面料、辅料、成品出入库与盘点 |
| `order-service` | 8400 | 订单：加工单与销售订单生命周期 |
| `marketing-service` | 8500 | 营销：优惠券、加盟活动、客户 CRM |
| `payment-service` | 8520 | 支付与对账 |
| `live-service` | 8550 | 直播：实时数据、爆单预警 |
| `ecommerce-adapter` | 8600 | 电商对接：抖音、淘宝、TikTok 等 API 桥接 |
| `print-service` | 8620 | 打印任务与编排 |
| `style-service` | 8650 | 款式：款号、版型与工艺属性 |
| `device-service` | 8660 | 物理设备：终端、采集器、物联网接入 |
| `report-service` | 8700 | 异步报表导出（如计件工资、损益） |
| `ai-advisor-service` | 8800 | 企业 AI 顾问：基于生产数据的决策建议 |
| `ai-selection-service` | 8900 | AI 选品：趋势、以图搜布等 |
| `search-service` | 9000 | 全文检索：款式、订单、库存等 |
| `common-infrastructure` | — | 公共库：跨服务工具、多租户与通用技术组件 |

父工程坐标：`com.garmentfactory:garment-factory-saas`。各业务服务 Java 根包一般为 `com.garment.<领域短名>`（网关模块为 `com.garmentfactory.gateway`）。

## 本地构建

在仓库根目录执行（需本机 **JDK 17** 与 Maven）：

```bash
mvn -q package -DskipTests
```

仅构建某一模块时，可使用 `-pl <模块名> -am`。

## 容器与部署（骨架）

- **Docker**：除 `common-infrastructure`（纯依赖库）外，每个可运行模块根目录均有独立 `Dockerfile`。在对应模块下先执行 `mvn package -DskipTests`，再于该目录执行 `docker build`（详见各 `Dockerfile` 顶部注释）。
- **CI**：GitHub Actions 见 [.github/workflows/ci.yml](.github/workflows/ci.yml)（当前仅 Maven 编译；镜像推送与部署为 TODO）。
- **批量构建镜像占位脚本**：[scripts/ci-docker-build.sh](scripts/ci-docker-build.sh)。
- **Kubernetes**：主体说明与示例清单见 [deploy/kubernetes/README.md](deploy/kubernetes/README.md)（`base/namespace.yaml`、`example/` 占位；生产级探针、资源与密钥后续补全）。

## 许可

见仓库根目录 [LICENSE](LICENSE)。
