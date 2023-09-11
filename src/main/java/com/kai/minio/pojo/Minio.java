package com.kai.minio.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * minio 属性
 * @author pankx
 * @date 2023/9/11 0:12
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class Minio {
    /**
     * 访问地址（ip + port）
     */
    private String endpoint;

    /**
     * 账号
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 桶
     */
    private String bucket;
}
