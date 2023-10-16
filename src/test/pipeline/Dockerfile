FROM nginx:1.14

WORKDIR /app

# 生成时间戳
ARG BUILD_DATE
RUN echo "Build date: ${BUILD_DATE}" > build-info.txt
