package com.roukaixin.utils;

import com.roukaixin.oss.enums.OssTypeEnum;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 上传工具类
 *
 * @author 不北咪
 * @date 2024/2/21 下午4:47
 */
public class UploadUtils {

    private UploadUtils() {

    }


    /**
     * 获取 objectKey
     * @param fileType 文件类型
     * @param fileIdentifier 文件md5
     * @param fileName 文件名
     * @param ossTypeEnum oss 类型
     * @return objectKey
     */
    public static String getObjectKey(String fileType, String fileIdentifier,
                                      String fileName, OssTypeEnum ossTypeEnum) {
        String separator;
        if (Objects.requireNonNull(ossTypeEnum) == OssTypeEnum.LOCAL) {
            separator = File.separator;
        } else {
            separator = "/";
        }
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        StringBuilder objectKey = new StringBuilder(date);
        if (StringUtils.hasText(fileType)) {
            String type = fileType.split("/")[0];
            objectKey.append(separator).append(type);
        }
        objectKey.append(separator).append(fileIdentifier).append(separator).append(fileName);
        return objectKey.toString();
    }

    /**
     * 获取分片上传保存的路径
     * @param rootPath 配置的目录
     * @param objectKey 文件路径
     * @return String
     */
    public static String getSavePath(String rootPath, String objectKey) {
        // 判断是否为绝对路径
        String osName = System.getProperty("os.name");
        switch (osName) {
            // linux 操作系统
            case "Linux" -> {
                break;
            }
            // windows
            case "windows" -> {

            }
            default -> throw new RuntimeException("获取不到操作系统");
        }
        return null;
    }
}
