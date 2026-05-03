# Kubernetes 部署（主体骨架）

本目录预留集群部署清单的扩展点，**当前不包含生产级细节**（资源限制、探针、HPA、NetworkPolicy、密钥管理等需随业务落地后补充）。

## 建议目录结构（后续演进）

```text
deploy/kubernetes/
├── README.md                 # 本说明
├── base/                     # 通用：Namespace、ConfigMap 模板等
├── overlays/
│   ├── dev/                  # 开发环境差异（副本数、镜像 tag）
│   └── prod/                 # 生产环境差异
└── services/                 # 各微服务 Deployment + Service（可由脚本从模板生成）
```

## 与 Docker 镜像的关系

- 各可运行模块根目录已有独立 `Dockerfile`，构建上下文为该模块目录，且需已执行 `mvn package` 生成 `target/*.jar`。
- `common-infrastructure` 为依赖库，**不单独打容器镜像**。

## 下一步实现清单（示例）

1. 为每个服务补充 `Deployment`（镜像、`containerPort` 与 `application.yml` 中端口一致）、`Service`（ClusterIP 或 headless）。
2. 网关对外暴露：`Ingress` 或 `LoadBalancer` + TLS。
3. Nacos / MySQL / Redis / Elasticsearch 等：使用 Helm 官方 chart 或运维提供的外部服务，通过 `ConfigMap`/`Secret` 注入连接串。
4. 接入 CI：构建镜像 → 推送仓库 → `kubectl apply` 或 GitOps（Argo CD）。

示例占位清单见 `example/` 目录。
