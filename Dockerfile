FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 创建非 root 用户运行，企业安全规范
RUN addgroup -S treemangroup && adduser -S treeman -G treemangroup
USER treeman

# 从构建阶段复制 jar 包
COPY target/*.jar app.jar
ENV MYSQL_PORT=3307
ENV REDIS_PORT=6380
# 声明端口（Spring Boot 默认 8080）
EXPOSE 8080

# JVM 参数：容器内存感知，避免 OOM
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
