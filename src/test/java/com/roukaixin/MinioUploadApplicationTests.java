package com.roukaixin;

import com.roukaixin.oss.properties.OssProperties;
import com.roukaixin.oss.strategy.UploadStrategy;
import com.roukaixin.oss.strategy.UploadStrategyFactory;
import io.minio.GetBucketPolicyArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MinioUploadApplicationTests {

//    @Autowired
    MinioClient minioClient;

    @Autowired
    OssProperties ossProperties;

    @SneakyThrows
    @Test
    void contextLoads() {

        String bucketPolicy = minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket("minio-upload")
                .build());
    }

    @Test
    @SneakyThrows
    void ossProperties() {
        UploadStrategy instance = UploadStrategyFactory.getInstance();
        System.out.println(instance.getClass().getName());
    }

    @Test
    void jarDir() {
        System.out.println(System.getProperty("user.dir"));

        System.out.println(System.getProperties());
    }

}
