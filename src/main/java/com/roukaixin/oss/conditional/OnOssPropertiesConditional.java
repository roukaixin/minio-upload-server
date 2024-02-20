package com.roukaixin.oss.conditional;

import com.roukaixin.oss.enums.OssTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 判断 OssProperties 类是否注入
 *
 * @author 不北咪
 * @date 2024/2/20 上午10:07
 */
@Slf4j
public class OnOssPropertiesConditional implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        log.info("OssPropertiesConditional 注解元数据中的全部注解 -> {}", metadata.getAnnotations().stream().count());
        OssTypeEnum ossType = context.getEnvironment().getProperty("oss.type", OssTypeEnum.class);
        if (ossType != null) {
            switch (ossType) {
                case LOCAL, MINIO -> {
                    return true;
                }
                default -> {
                    return false;
                }
            }
        }
        return false;
    }
}
