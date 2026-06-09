# ── 阶段一：构建 ──────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 先只复制 pom.xml，利用 Docker 缓存层
# 依赖没变化时不重新下载，大幅加速构建
COPY pom.xml .
RUN mvn dependency:go-offline -q

# 再复制源码并构建
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── 阶段二：运行 ──────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 创建非 root 用户运行，企业安全规范
RUN addgroup -S treemangroup && adduser -S treeman -G treemangroup
USER treeman

# 从构建阶段复制 jar 包
COPY --from=builder /app/target/*.jar app.jar

# 声明端口（Spring Boot 默认 8080）
EXPOSE 8080

# JVM 参数：容器内存感知，避免 OOM
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
