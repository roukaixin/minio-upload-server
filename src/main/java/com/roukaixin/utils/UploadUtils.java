package com.roukaixin.utils;

import com.roukaixin.oss.enums.OsNameEnum;
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

    private final static String SLASH = "/";

    private final static String BACKSLASH = "\\\\";

    private final static char COLON = ':';

    private final static char BACKSLASH_CHAR = '\\';

    private final static int TWO = 2;

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
        String separator = getSeparator(ossTypeEnum);
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        StringBuilder objectKey = new StringBuilder(date);
        if (StringUtils.hasText(fileType)) {
            String type = fileType.split(SLASH)[0];
            objectKey.append(separator).append(type);
        }
        objectKey.append(separator).append(fileIdentifier).append(separator).append(fileName);
        return objectKey.toString();
    }


    /**
     * 获取临时保存的文件目录
     * @param rootPath 配置的目录
     * @param objectKey 文件路径
     * @param ossTypeEnum oss 类型
     * @return String
     */
    public static String getTmpSavePath(String rootPath, String objectKey, OssTypeEnum ossTypeEnum) {
        return getSavePath(rootPath, objectKey, ossTypeEnum) + "tmp";
    }

    /**
     * 获取分片上传保存的路径
     * @param rootPath 配置的目录
     * @param objectKey 文件路径
     * @param ossTypeEnum oss 类型
     * @return String 绝对路径
     */
    public static String getSavePath(String rootPath, String objectKey, OssTypeEnum ossTypeEnum) {
        // 判断是否为绝对路径
        OsNameEnum osName = getOsName();
        String replaceRootPath = rootPath.replaceAll(SLASH, File.separator).replaceAll(BACKSLASH, File.separator);
        String projectPath = System.getProperty("user.dir");
        final boolean b = replaceRootPath.charAt(replaceRootPath.length() - 1) == File.separatorChar;
        // 去掉文件名
        String separator = getSeparator(ossTypeEnum);
        String objectKeyPath = objectKey.substring(0, objectKey.lastIndexOf(separator) + 1);
        switch (osName) {
            // linux 操作系统
            case LINUX -> {
                if (replaceRootPath.charAt(0) == File.separatorChar) {
                    if (b) {
                        return replaceRootPath + objectKeyPath;
                    } else {
                        return replaceRootPath + File.separatorChar + objectKeyPath;
                    }
                } else {

                    if (b) {
                        return projectPath + File.separatorChar + replaceRootPath + objectKeyPath;
                    } else {
                        return projectPath + File.separatorChar + replaceRootPath + File.separatorChar + objectKeyPath;
                    }
                }
            }
            // windows
            case WINDOWS -> {
                if (replaceRootPath.charAt(1) == COLON && replaceRootPath.charAt(TWO) == BACKSLASH_CHAR) {
                    // 绝对路径
                    if (b) {
                        return replaceRootPath + objectKeyPath;
                    } else {
                        return replaceRootPath + File.separatorChar + objectKeyPath;
                    }
                } else {
                    if (b) {
                        return projectPath + File.separatorChar + replaceRootPath + objectKeyPath;
                    } else {
                        return projectPath + File.separatorChar + replaceRootPath + File.separatorChar + objectKeyPath;
                    }
                }
            }
            default -> throw new RuntimeException("获取不到操作系统");
        }
    }

    /**
     * 获取分割符号
     * @param ossTypeEnum oss 类型
     * @return String
     */
    public static String getSeparator(OssTypeEnum ossTypeEnum) {
        String separator;
        if (Objects.requireNonNull(ossTypeEnum) == OssTypeEnum.LOCAL) {
            separator = File.separator;
        } else {
            separator = SLASH;
        }
        return separator;
    }

    /**
     * 获取操作系统
     * @return 操作系统枚举类
     */
    public static OsNameEnum getOsName() {
        String osName = System.getProperty("os.name");
        String osNameLowerCase = osName.toLowerCase();
        if (osNameLowerCase.contains(OsNameEnum.LINUX.name().toLowerCase())) {
            return OsNameEnum.LINUX;
        } else if (osNameLowerCase.contains(OsNameEnum.WINDOWS.name().toLowerCase())) {
            return OsNameEnum.WINDOWS;
        } else {
            return OsNameEnum.OTHER;
        }
    }


}
