# 依赖的基础镜像
FROM maven:3.5-jdk-8-alpine as builder

# Copy local code to the container image.
# 工作目录
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
# 打包构建，跳过测试
RUN mvn package -DskipTests

# Run the web service on container startup.
# 启动运行镜像的时候，默认执行该命令(在启动时可以指定或修改命令参数)
CMD ["java","-jar","/app/target/user-center-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]


