package com.kai.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传文件信息
 *
 * @author 不北咪
 * @date 2023/3/12 0:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDTO {

    /**
     * 文件MD5值
     */
    private String fileIdentifier;

    /**
     * 文件名字
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小总数
     */
    private Long totalSize;

    /**
     * 分片大小（字节byte）
     */
    private Long chunkSize;
}
