package com.roukaixin;

import com.roukaixin.oss.properties.OssProperties;
import io.minio.GetBucketPolicyArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class MinioUploadApplicationTests {

    @Autowired
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
        File file = new File("aa/bb");
        System.out.println(file);
    }

}
