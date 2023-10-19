# 基于 Java 17 基础镜像
FROM openjdk:173-jdk
# 设定时区
ENV TZ=Asia/Tokyo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 拷贝jar包
COPY target/wms-0.0.1-SNAPSHOT.jar /app.jar
# 指定容器监听的端口
EXPOSE 8083
# 入口
ENTRYPOINT ["java","-jar","/app.jar"]