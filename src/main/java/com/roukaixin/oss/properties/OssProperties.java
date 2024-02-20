package com.roukaixin.oss.properties;

import com.roukaixin.oss.enums.OssTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * oss 配置
 *
 * @author 不北咪
 * @date 2024/2/20 上午9:46
 */
@ConfigurationProperties(prefix = "oss")
@Component
@Setter
@Getter
public class OssProperties {

    /**
     * 类别（local(本地)，minio）
     */
    private OssTypeEnum type;


}
