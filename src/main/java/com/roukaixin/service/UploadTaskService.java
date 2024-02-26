package com.roukaixin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roukaixin.pojo.R;
import com.roukaixin.pojo.UploadTask;
import com.roukaixin.pojo.dto.FileInfoDTO;

/**
 * 分片上传-分片任务记录
 *
 * @author 不北咪
 * @date 2023/3/11 23:12
 */
public interface UploadTaskService extends IService<UploadTask> {


    /**
     * 创建 uploadId
     * @param fileInfoDto 文件信息
     * @return R<UploadTask>
     */
    R<UploadTask> createMultipartUploadId(FileInfoDTO fileInfoDto);

//    /**
//     * 上传分片
//     * @param uploadPart 分片信息
//     * @return UploadTask
//     */
//    R<Object> uploadPartAsync(UploadPart uploadPart);
//
//    /**
//     * 合并分片
//     * @param fileInfoDto 文件信息
//     * @return CompletableFuture<ObjectWriteResponse>
//     */
//    R<String> completeMultipartUploadAsync(FileInfoDTO fileInfoDto);
}
