package com.roukaixin.oss.strategy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.roukaixin.oss.pojo.CustomMinioClient;
import com.roukaixin.oss.properties.MinioProperties;
import com.roukaixin.oss.properties.OssProperties;
import com.roukaixin.pojo.UploadTask;
import com.roukaixin.pojo.dto.FileInfoDTO;
import com.roukaixin.pojo.dto.UploadPart;
import com.roukaixin.service.UploadTaskService;
import com.roukaixin.utils.UploadUtils;
import io.minio.UploadPartResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

/**
 * minio 上传接口实现类
 *
 * @author 不北咪
 * @date 2024/2/21 上午10:55
 */
@Component
@ConditionalOnBean(value = MinioProperties.class)
@Slf4j
public class MinioStrategy implements UploadStrategy {

    @Resource
    private UploadTaskService uploadTaskService;

    @Resource
    private CustomMinioClient customMinioClient;

    @Resource
    private MinioProperties minioProperties;

    @Resource
    private OssProperties ossProperties;

    @Override
    public UploadTask createMultipartUpload(FileInfoDTO fileInfo) {
        LambdaQueryWrapper<UploadTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UploadTask::getFileIdentifier, fileInfo.getFileIdentifier());
        wrapper.eq(UploadTask::getOssType, ossProperties.getType());
        UploadTask task = uploadTaskService.getOne(wrapper);
        if (task == null) {
            // 没有上传任务
            String objectKey = UploadUtils.getObjectKey(fileInfo.getFileType(), fileInfo.getFileIdentifier(),
                    fileInfo.getFileName(), ossProperties.getType());

            Multimap<String, String> headers = null;
            if (StringUtils.hasText(fileInfo.getFileType())) {
                headers = HashMultimap.create();
                headers.put("Content-Type", fileInfo.getFileType());
            }
            // 生成 uploadId
            String uploadId = customMinioClient.uploadId(minioProperties.getBucket(), null, objectKey,
                    headers, null);

            UploadTask build = UploadTask.builder()
                    .ossType(ossProperties.getType())
                    .uploadId(uploadId)
                    .fileIdentifier(fileInfo.getFileIdentifier())
                    .fileName(fileInfo.getFileName())
                    .fileType(fileInfo.getFileType())
                    .bucketName(minioProperties.getBucket())
                    .objectKey(objectKey)
                    .saveFullPath(minioProperties.getEndpoint() + "/" + minioProperties.getBucket() + "/" + objectKey)
                    .totalSize(fileInfo.getTotalSize())
                    .chunkSize(fileInfo.getChunkSize())
                    .chunkNumber(
                            BigDecimal.valueOf(fileInfo.getTotalSize())
                            .divide(BigDecimal.valueOf(fileInfo.getChunkSize()), RoundingMode.UP)
                            .intValue()
                    )
                    .completed(false).build();
            uploadTaskService.save(build);
            return build;
        } else {
            return task;
        }
    }

    @Override
    public void uploadPartAsync(UploadPart uploadPart) {
        // 上传任务
        UploadTask uploadTask = uploadTaskService.getOne(new LambdaQueryWrapper<UploadTask>()
                .eq(UploadTask::getFileIdentifier, uploadPart.getFileIdentifier())
                .eq(UploadTask::getOssType, ossProperties.getType())
        );
        Multimap<String, String> headers = null;
        if (StringUtils.hasText(uploadTask.getFileType())) {
            headers = HashMultimap.create();
            headers.put("Content-Type", uploadTask.getFileType());
        }
        String etag;
        try {
            CompletableFuture<UploadPartResponse> partAsync = customMinioClient.uploadPartAsync(
                    uploadTask.getBucketName(), uploadTask.getObjectKey(),
                    uploadPart.getFile().getInputStream(), uploadPart.getFile().getSize(),
                    uploadTask.getUploadId(), uploadPart.getPartNumber(), headers);
            etag = partAsync.get().etag();
            log.info("etag：{}", etag);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            // 上传失败，清除上传任务
            customMinioClient.abortMultipartUploadAsync(uploadTask.getBucketName(), uploadTask.getObjectKey(),
                    uploadTask.getUploadId());
            LambdaQueryWrapper<UploadTask> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UploadTask::getUploadId, uploadTask.getUploadId());
            uploadTaskService.remove(wrapper);
        }
    }

    @Override
    public boolean completeMultipartUploadAsync(FileInfoDTO fileInfo) {
        return false;
    }
}
