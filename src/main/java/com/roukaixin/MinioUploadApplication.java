package com.roukaixin;

import com.roukaixin.oss.minio.properties.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动类
 * @author pankx
 * @date 2023/9/11 0:01
 */
@SpringBootApplication
public class MinioUploadApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MinioUploadApplication.class, args);
        MinioProperties bean = run.getBean(MinioProperties.class);
        System.out.println(bean);
    }
}
