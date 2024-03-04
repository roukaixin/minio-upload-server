package com.roukaixin.oss.strategy;

import com.roukaixin.pojo.UploadTask;
import com.roukaixin.pojo.dto.FileInfoDTO;
import com.roukaixin.pojo.dto.UploadPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传接口
 *
 * @author 不北咪
 * @date 2024/2/21 上午10:51
 */
public interface UploadStrategy {


    /**
     * 创建文件分片，minio 会返回 uploadId，local只是插入把数据插入数据库
     * @param fileInfo 文件信息
     * @return 上传文件信息
     */
    UploadTask createMultipartUpload(FileInfoDTO fileInfo);

    /**
     * 上传分片
     * @param uploadPart 分片信息
     */
    void uploadPartAsync(UploadPart uploadPart);

    /**
     * 合并分片
     * @param fileInfo 文件信息
     * @return 是否合并成功
     */
    boolean completeMultipartUploadAsync(FileInfoDTO fileInfo);

    /**
     * 普通上传
     * @param file 文件
     * @return boolean
     */
    boolean upload(MultipartFile file);
}
