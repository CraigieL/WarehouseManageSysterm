# 定义构建阶段的基础镜像
FROM node:14 AS build-stage

# 设置工作目录
WORKDIR /app

# 复制 'package.json' 和 'package-lock.json'
COPY package.json package-lock.json* ./

# 安装项目依赖
RUN npm install

# 复制其他必要的项目文件和目录
COPY public/ public/
COPY src/ src/
COPY babel.config.js ./
COPY jsconfig.json ./
COPY vue.config.js ./

# 构建应用
RUN npm run build

# 运行阶段，使用 Nginx 镜像
FROM nginx:stable-alpine as production-stage

# 从构建阶段拷贝构建结果到 nginx 容器的目录中
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 自定义的 nginx 配置
COPY nginx-config/default.conf /etc/nginx/conf.d/default.conf

# 暴露 80 端口
EXPOSE 80

# 以前台方式启动 nginx
CMD ["nginx", "-g", "daemon off;"]
