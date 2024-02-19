package com.roukaixin;

import io.minio.GetBucketPolicyArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MinioUploadApplicationTests {

    @Autowired
    MinioClient minioClient;

    @SneakyThrows
    @Test
    void contextLoads() {

        String bucketPolicy = minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket("minio-upload")
                .build());
    }

}
