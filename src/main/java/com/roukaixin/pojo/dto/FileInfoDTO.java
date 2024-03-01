package com.roukaixin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "文件 MD5 值")
    private String fileIdentifier;

    /**
     * 文件名字
     */
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String fileType;

    /**
     * 文件大小总数
     */
    @Schema(description = "文件大小。单位：字节(byte)")
    private Long totalSize;

    /**
     * 分片大小（单位：字节(byte)）
     */
    @Schema(description = "平均分片大小。单位：字节(byte)")
    private Long chunkSize;
}
