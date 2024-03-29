package com.roukaixin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 分片上传 参数信息
 *
 * @author 不北咪
 * @date 2023/3/12 22:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadPart {

    /**
     * 文件 md5
     */
    @Schema(description = "文件 md5 值")
    private String fileIdentifier;

    /**
     * 分片文件数据
     */
    @Schema(description = "分片文件二进制文件")
    private MultipartFile file;

    /**
     * 第几个分片
     */
    @Schema(description = "第几个分片")
    private int partNumber;
}
