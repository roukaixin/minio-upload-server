# 后端服务

支持 minio 、local(本地) 上传，并支持分片上传、秒传、断点续传

## 运行项目

- 使用 idea 打开项目，下载相关依赖
- 导入 sql 文件
- 修改 application.yml 中数据库信息和 minio 信息
- 启动项目。启动类是 `MinioUploadApplication.java`