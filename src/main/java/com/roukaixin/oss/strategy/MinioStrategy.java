package com.roukaixin.oss.strategy;

import com.roukaixin.oss.properties.MinioProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * minio 上传接口实现类
 *
 * @author 不北咪
 * @date 2024/2/21 上午10:55
 */
@Component
@ConditionalOnBean(value = MinioProperties.class)
public class MinioStrategy implements UploadStrategy {

}