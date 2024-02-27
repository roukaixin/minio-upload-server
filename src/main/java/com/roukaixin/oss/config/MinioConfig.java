package com.roukaixin.oss.config;

import com.roukaixin.oss.pojo.CustomMinioClient;
import com.roukaixin.oss.properties.MinioProperties;
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
        MinioProperties.class
})
public class MinioConfig {

    private final MinioProperties minioProperties;

    public MinioConfig(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Bean
    public CustomMinioClient customMinioClient(){
        return
                CustomMinioClient.build(
                        CustomMinioClient
                                .builder()
                                .endpoint(minioProperties.getEndpoint())
                                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                                .build());
    }

    @Bean
    public MinioClient minioClient(){
        return
                MinioClient
                        .builder()
                        .endpoint(minioProperties.getEndpoint())
                        .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                        .build();
    }
}
