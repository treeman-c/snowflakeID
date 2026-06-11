#!/bin/bash
set -e

IMAGE=$1      # 镜像名:tag
CONTAINER=$2  # 容器名
APP_PORT=$3   # 端口

echo "=== 开始部署 ==="
echo "镜像: $IMAGE"
echo "容器: $CONTAINER"
echo "端口: $APP_PORT"

# 1. 停止并删除旧容器
docker ps -a --filter "name=snow" --format "{{.ID}}" | xargs -r docker rm --force
echo "停止并删除旧容器..."
docker volume ls | grep snow | awk '{print $2}' | xargs -r docker volume rm
echo "删除旧卷"


echo "脚本运行结束!"
exit 0
