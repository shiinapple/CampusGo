#!/bin/bash

# ==========================================
# CampusGo 极速部署脚本 (搭配 IDEA Maven 使用)
# ==========================================

SERVER_IP="49.233.32.212"
SERVER_USER="root"
LOCAL_JAR_PATH="CampusGo-app/target/CampusGo-app.jar"
REMOTE_BASE_DIR="/root/campus"
REMOTE_TARGET_DIR="/root/campus/target"
IMAGE_NAME="system/campusgo-app:1.0-snapshot"
CONTAINER_NAME="CampusGo"

# 颜色输出配置
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m'

echo -e "${YELLOW}🚀 [1/3] 检查本地 JAR 包...${NC}"
# 自动检测 IDEA 是否已经打好包了
if [ ! -f "${LOCAL_JAR_PATH}" ]; then
    echo -e "${RED}❌ 找不到 JAR 包！请先在 IDEA 右侧的 Maven 面板双击一下 package。${NC}"
    exit 1
fi
echo -e "${GREEN}✅ 找到最新的 JAR 包，准备起飞！${NC}"

echo -e "${YELLOW}🧹 [2/3] 连接服务器清理旧环境并上传...${NC}"
ssh ${SERVER_USER}@${SERVER_IP} << EOF
    docker rm -f ${CONTAINER_NAME} || true
    docker rmi -f ${IMAGE_NAME} || true
    docker image prune -f
    rm -f ${REMOTE_TARGET_DIR}/CampusGo-app.jar
EOF

scp ${LOCAL_JAR_PATH} ${SERVER_USER}@${SERVER_IP}:${REMOTE_TARGET_DIR}/
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ 上传失败，请检查网络！${NC}"
    exit 1
fi

echo -e "${YELLOW}🐳 [3/3] 云端构建镜像并启动服务...${NC}"
ssh -t ${SERVER_USER}@${SERVER_IP} << EOF
    cd ${REMOTE_BASE_DIR}
    docker build --no-cache -t ${IMAGE_NAME} -f ./Dockerfile .

    cd dev-ops
    docker-compose -f docker-compose-app.yml up -d

    echo -e "✅ 部署完成！马上为您打印日志："
    docker logs -f ${CONTAINER_NAME}
EOF