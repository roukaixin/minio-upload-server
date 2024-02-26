package com.roukaixin.oss.properties;

import com.roukaixin.validation.annotation.Path;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

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
@Validated
public class LocalProperties {

    /**
     * 保存根目录（相当于 minio bucket）。win：\ / : * ? " < > | ， linux：/ 和 null
     */
    @NotBlank(message = "本地上传时，保存根目录不能为空")
    @Path
    private String rootPath;
}
