package com.roukaixin.oss.strategy;

import com.roukaixin.oss.properties.LocalProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * 本地上传实现类
 *
 * @author 不北咪
 * @date 2024/2/21 上午10:52
 */
@Component
@ConditionalOnBean(value = LocalProperties.class)
public class LocalStrategy implements UploadStrategy{



}
