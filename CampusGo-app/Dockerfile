# 基础镜像，可以先执行 docker pull openjdk:17-jdk-slim
FROM openjdk:17-jdk-slim

# 安装 AWT 需要的图形库 (用于图片压缩处理)
RUN apt-get update && apt-get install -y libfreetype6 fontconfig && rm -rf /var/lib/apt/lists/*

# 作者
MAINTAINER xiaofuge

# 配置
ENV PARAMS=""

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD target/CampusGo-app.jar /CampusGo-app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /CampusGo-app.jar $PARAMS"]