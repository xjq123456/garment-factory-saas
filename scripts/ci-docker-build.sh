#!/usr/bin/env bash
# 流水线辅助脚本骨架：在本地或 CI 中批量构建各服务镜像的入口。
# 使用前请先在仓库根目录执行: mvn -B package -DskipTests
# 后续可改为: 并行构建、传入 REGISTRY/TAG、buildx 多架构等。

set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

MODULES=(
  gateway-service
  user-service
  rbac-service
  production-service
  inventory-service
  order-service
  payment-service
  print-service
  marketing-service
  ecommerce-adapter
  live-service
  style-service
  device-service
  report-service
  ai-advisor-service
  ai-selection-service
  search-service
)

# TODO: REGISTRY=... TAG=${CI_COMMIT_SHORT_SHA:-local}
echo "ROOT=$ROOT"
for m in "${MODULES[@]}"; do
  echo "[skip] docker build -t garment/${m}:local -f ${m}/Dockerfile ${m}/"
done
echo "骨架脚本结束。实现阶段请取消注释真实 docker build 并处理镜像仓库认证。"
