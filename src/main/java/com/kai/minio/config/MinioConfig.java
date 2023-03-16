package com.kai.minio.config;

import com.kai.minio.CustomMinioClient;
import com.kai.minio.pojo.Minio;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ConditionalOnBean(value = {
        Minio.class
})
public class MinioConfig {

    @Resource
    private Minio minio;

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
