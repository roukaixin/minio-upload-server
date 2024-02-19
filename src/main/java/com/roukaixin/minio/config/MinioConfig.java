package com.roukaixin.minio.config;

import com.roukaixin.minio.CustomMinioClient;
import com.roukaixin.minio.pojo.Minio;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * minio 配置类
 * @author pankx
 * @date 2023/9/11 0:01
 */
@Configuration
@ConditionalOnBean(value = {
        Minio.class
})
public class MinioConfig {

    private final Minio minio;

    public MinioConfig(Minio minio) {
        this.minio = minio;
    }

    @Bean
    public CustomMinioClient customMinioClient(){
        return
                CustomMinioClient.build(
                        CustomMinioClient
                                .builder()
                                .endpoint(minio.getEndpoint())
                                .credentials(minio.getAccessKey(), minio.getSecretKey())
                                .build());
    }

    @Bean
    public MinioClient minioClient(){
        return
                MinioClient.builder().endpoint(minio.getEndpoint()).credentials(minio.getAccessKey(),minio.getSecretKey()).build();
    }
}
