package com.kai.service;

import com.kai.pojo.UploadTask;
import com.kai.pojo.dto.FileInfoDTO;
import com.kai.pojo.dto.UploadPart;
import com.kai.utils.R;

/**
 * 分片上传-分片任务记录
 *
 * @author 不北咪
 * @date 2023/3/11 23:12
 */
public interface UploadTaskService{


    /**
     * 创建 uploadId
     * @param fileInfoDto 文件信息
     * @return R<UploadTask>
     */
    R<UploadTask> createMultipartUploadId(FileInfoDTO fileInfoDto);

    /**
     * 上传分片
     * @param uploadPart 分片信息
     * @return UploadTask
     */
    R<Object> uploadPartAsync(UploadPart uploadPart);

    /**
     * 合并分片
     * @param fileInfoDto 文件信息
     * @return CompletableFuture<ObjectWriteResponse>
     */
    R<String> completeMultipartUploadAsync(FileInfoDTO fileInfoDto);
}
