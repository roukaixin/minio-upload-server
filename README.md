# 后端服务

支持 minio 、local(本地) 上传，并支持分片上传、秒传、断点续传

## 运行项目

- 使用 idea 打开项目，下载相关依赖
- 导入 sql 文件
- 修改 application.yml 中数据库信息和 minio 信息
- 启动项目。启动类是 `MinioUploadApplication.java`

### 创建数据库
```mysql
create schema upload collate utf8mb4_general_ci;
```

### 创建表

```mysql
create table upload_task
(
    id              bigint                       not null comment '主键 id'
        primary key,
    oss_type        varchar(12)                  null comment 'oss 类型。（minio、本地(local)）',
    upload_id       varchar(128)                 null comment '分片上传的 uploadId （minio需要，local不需要）',
    file_identifier char(32)                     not null comment '文件唯一标识（md5）',
    file_name       varchar(32)                  null comment '文件名',
    file_type       varchar(32)                  null comment '文件类型',
    bucket_name     varchar(32)                  null comment 'minio:所属桶名/local:上传路径',
    object_key      varchar(128)                 null comment '文件的 key（minio:桶下的相对路径，local:上传路径之后的相对路径）',
    total_size      bigint                       null comment '文件大小 （单位：byte）',
    chunk_size      bigint                       null comment '每块分片大小（单位：byte）',
    chunk_number    int                          null comment '分片数量',
    is_completed    tinyint unsigned default '0' null comment '是否已经上传完成（0否，1是）。说明：合并成功才算是上传完成',
    constraint upload_task_un
        unique (file_identifier)
)
    comment '分片上传的任务';
```