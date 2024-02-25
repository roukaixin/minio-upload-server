package com.roukaixin;

import com.roukaixin.oss.properties.OssProperties;
import com.roukaixin.oss.strategy.UploadStrategy;
import com.roukaixin.oss.strategy.UploadStrategyFactory;
import com.roukaixin.pojo.dto.FileInfoDTO;
import io.minio.GetBucketPolicyArgs;
import io.minio.MinioClient;
import jakarta.annotation.Resource;
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

    @Resource
    private UploadStrategy uploadStrategy;

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

    @Test
    void uploadTmp() {
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        fileInfoDTO.setFileIdentifier("1");
        fileInfoDTO.setFileName("1.jpg");
        fileInfoDTO.setFileType("image/jpg");
        fileInfoDTO.setTotalSize(20000L);
        fileInfoDTO.setChunkSize(100L);
        uploadStrategy.createMultipartUpload(fileInfoDTO);
    }

}
