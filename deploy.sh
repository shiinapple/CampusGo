#!/bin/bash

# ==========================================
# CampusGo 极速部署脚本 (最终版)
# 搭配 IDEA Maven package 使用
# ==========================================

# --- 1. 核心配置 ---
SERVER_IP="49.233.32.212"
SERVER_USER="root"
# 对应你项目中的实际打好的包路径
LOCAL_JAR_PATH="CampusGo-app/target/CampusGo-app.jar"
# 服务器上的工作目录
REMOTE_BASE_DIR="/root/campus"
REMOTE_TARGET_DIR="/root/campus/target"
# 容器与镜像名称
IMAGE_NAME="system/campusgo-app:1.0-snapshot"
CONTAINER_NAME="CampusGo"

# 颜色输出配置
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m'

# --- 2. 本地预检 ---
echo -e "${YELLOW}🚀 [1/3] 检查本地环境...${NC}"

# 检查 JAR 包是否存在
if [ ! -f "${LOCAL_JAR_PATH}" ]; then
    echo -e "${RED}❌ 错误：找不到 JAR 包！${NC}"
    echo -e "${YELLOW}请确保已在 IDEA 中执行 Maven -> CampusGo (Root) -> Lifecycle -> package${NC}"
    exit 1
fi

# 检查服务器连通性
nc -w 3 -z ${SERVER_IP} 22 > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ 错误：无法连接到服务器 ${SERVER_IP}，请检查网络或 VPN。${NC}"
    exit 1
fi

echo -e "${GREEN}✅ 检查通过，准备上传...${NC}"

# --- 3. 远程清理与上传 ---
echo -e "${YELLOW}🧹 [2/3] 清理远程旧镜像并上传新包...${NC}"

# 远程执行：停止容器并删除旧镜像
ssh ${SERVER_USER}@${SERVER_IP} << EOF
    mkdir -p ${REMOTE_TARGET_DIR}
    docker rm -f ${CONTAINER_NAME} > /dev/null 2>&1 || true
    docker rmi -f ${IMAGE_NAME} > /dev/null 2>&1 || true
    rm -f ${REMOTE_TARGET_DIR}/CampusGo-app.jar
EOF

# 上传 JAR 包
scp ${LOCAL_JAR_PATH} ${SERVER_USER}@${SERVER_IP}:${REMOTE_TARGET_DIR}/
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ 上传失败！${NC}"
    exit 1
fi

# --- 4. 云端构建与启动 ---
echo -e "${YELLOW}🐳 [3/3] 服务器端构建并启动服务...${NC}"

# 使用 -t 开启交互式终端，方便观察 Docker 构建进度
ssh -t ${SERVER_USER}@${SERVER_IP} << EOF
    cd ${REMOTE_BASE_DIR}

    # 构建新镜像
    echo "开始构建 Docker 镜像..."
    docker build --no-cache -t ${IMAGE_NAME} -f ./Dockerfile .

    # 启动容器 (优先使用 docker-compose，确保网络配置一致)
    if [ -d "dev-ops" ]; then
        cd dev-ops
        docker-compose -f docker-compose-app.yml up -d
    else
        # 兜底启动方案
        docker run -d --name ${CONTAINER_NAME} -p 8091:8091 ${IMAGE_NAME}
    fi

    echo -e "${GREEN}✅ 部署完成！正在进入日志监控 (按 Ctrl+C 退出日志)...${NC}"
    docker logs -f ${CONTAINER_NAME}
EOF