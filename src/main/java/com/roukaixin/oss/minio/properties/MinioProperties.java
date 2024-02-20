package com.roukaixin.oss.minio.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * minio 属性
 * @author pankx
 * @date 2023/9/11 0:12
 */
@ConfigurationProperties(prefix = "oss.config.minio")
@ConditionalOnProperty(prefix = "oss", value = "type", havingValue = "minio")
@Setter
@Getter
@Component
public class MinioProperties {

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
