package com.roukaixin.oss.strategy;

import com.roukaixin.oss.enums.OssTypeEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上传工厂（获取具体的实现类）
 *
 * @author 不北咪
 * @date 2024/2/21 上午10:57
 */
@Component
@Slf4j
public class UploadStrategyFactory {

    private static Map<String, UploadStrategy> UPLOAD_STRATEGY_MAP;

    private static ApplicationContext APPLICATION_CONTEXT;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private final Map<String, UploadStrategy> uploadStrategyHashMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        APPLICATION_CONTEXT = applicationContext;
        UPLOAD_STRATEGY_MAP = uploadStrategyHashMap;
    }

    public static UploadStrategy getInstance() {
        OssTypeEnum property = APPLICATION_CONTEXT.getEnvironment().getProperty("oss.type", OssTypeEnum.class);
        if (property == null) {
            throw new RuntimeException("没有配置上传所需的配置");
        }
        return UPLOAD_STRATEGY_MAP.get(property.name().toLowerCase() + "Strategy");

    }
}
