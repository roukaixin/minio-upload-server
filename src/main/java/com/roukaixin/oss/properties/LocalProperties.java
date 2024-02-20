package com.roukaixin.oss.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 本地上传模式
 *
 * @author 不北咪
 * @date 2024/2/20 下午3:43
 */
@Setter
@Getter
@ConditionalOnProperty(prefix = "oss", value = "type", havingValue = "local")
@ConfigurationProperties(prefix = "oss.config.local")
@Component
public class LocalProperties {

    /**
     * 保存根目录（相当于 minio bucket）,可选值：为空或绝对路径
     */
    private String rootPath;
}
