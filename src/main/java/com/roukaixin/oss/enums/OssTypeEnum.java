package com.roukaixin.oss.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * oss 支持的类型
 *
 * @author 不北咪
 * @date 2024/2/20 上午9:51
 */
@Getter
public enum OssTypeEnum implements IEnum<String> {

    /**
     * 本地
     */
    LOCAL,

    /**
     * minio
     */
    MINIO;

    @Override
    public String getValue() {
        return this.name().toLowerCase();
    }
}
