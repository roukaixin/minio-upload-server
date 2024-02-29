package com.roukaixin.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.roukaixin.oss.enums.OssTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分片上传-分片任务记录
 *
 * @author 不北咪
 * @date 2023/3/11 23:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadTask {

    /**
     * mybatis plus 自动生成
     */
    private Long id;

    /**
     * oss 类型。（minio、本地(local)）
     */
    private OssTypeEnum ossType;

    /**
     * 分片上传的 uploadId
     */
    private String uploadId;

    /**
     * 文件唯一标识（md5）
     */
    private String fileIdentifier;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 所属桶名
     */
    private String bucketName;

    /**
     * 文件的key（minio的路径）。构成：yyyy-MM-dd/type/fileIdentifier/fileName
     */
    private String objectKey;

    /**
     * 保存的全路径名
     */
    private String saveFullPath;

    /**
     * 文件大小（byte）
     */
    private Long totalSize;

    /**
     * 每个分片大小（byte）
     */
    private Long chunkSize;

    /**
     * 分片数量
     */
    private Integer chunkNumber;

    /**
     * 是否上传完成
     */
    @TableField(value = "is_completed")
    private boolean completed;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    @TableLogic
    private boolean deleted;
}