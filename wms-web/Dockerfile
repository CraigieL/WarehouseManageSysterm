# 定义构建阶段的基础镜像
FROM node:14 AS build-stage

# 设置工作目录
WORKDIR /app

# 复制 'package.json' 和 'package-lock.json' (如果可用)
COPY package.json package-lock.json* ./

# 安装项目依赖
RUN npm install

# 复制其他必要的项目文件和目录
COPY public/ public/
COPY src/ src/
COPY babel.config.js ./
# 如果有其他必要的配置文件或目录，请在此处添加它们

# 构建应用
RUN npm run build

# 运行阶段，使用 Nginx 镜像
FROM nginx:stable-alpine as production-stage

# 从构建阶段拷贝构建结果到 nginx 容器的目录中
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 如果你有自定义的 nginx 配置，也可以在这里拷贝
# COPY path_to_your_nginx_config /etc/nginx/conf.d/default.conf

# 暴露 80 端口
EXPOSE 80

# 以前台方式启动 nginx
CMD ["nginx", "-g", "daemon off;"]
