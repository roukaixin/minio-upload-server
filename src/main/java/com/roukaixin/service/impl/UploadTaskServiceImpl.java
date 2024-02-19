package com.roukaixin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.roukaixin.mapper.UploadTaskMapper;
import com.roukaixin.minio.CustomMinioClient;

import com.roukaixin.minio.properties.MinioProperties;
import com.roukaixin.pojo.R;
import com.roukaixin.pojo.UploadTask;
import com.roukaixin.pojo.dto.FileInfoDTO;
import com.roukaixin.pojo.dto.UploadPart;
import com.roukaixin.service.UploadTaskService;
import io.minio.ListPartsResponse;
import io.minio.UploadPartResponse;
import io.minio.messages.Part;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 分片上传-分片任务记录
 *
 * @author 不北咪
 * @date 2023/3/11 23:11
 */
@Service
@Slf4j
public class UploadTaskServiceImpl implements UploadTaskService{

    @Resource
    private UploadTaskMapper uploadTaskMapper;

    @Resource
    private CustomMinioClient customMinioClient;

    @Resource
    private MinioProperties minioProperties;

    @Override
    public R<UploadTask> createMultipartUploadId(FileInfoDTO fileInfoDto) {
        LambdaQueryWrapper<UploadTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UploadTask::getFileIdentifier,fileInfoDto.getFileIdentifier());
        UploadTask task = uploadTaskMapper.selectOne(wrapper);
        if (task != null && task.getCompleted().equals(1)){
            // 已经合并了
            return R.ok("分片已经合并",task);
        }else if (task == null) {
            // 没有上传任务
            String objectName = "/" + DateUtil.formatDate(new Date());
            Multimap<String, String> headers = null;
            if (StringUtils.hasText(fileInfoDto.getFileType())) {
                String type = fileInfoDto.getFileType().split("/")[0];
                objectName = objectName + "/" + type;
                headers = HashMultimap.create();
                headers.put("Content-Type", fileInfoDto.getFileType());
            }
            objectName = objectName + "/" + fileInfoDto.getFileIdentifier() +
                    fileInfoDto.getFileName().substring(fileInfoDto.getFileName().lastIndexOf("."));
            // 生成 uploadId
            String uploadId = customMinioClient.uploadId(minioProperties.getBucket(), null, objectName, headers, null);

            UploadTask build = UploadTask.builder()
                    .uploadId(uploadId)
                    .fileIdentifier(fileInfoDto.getFileIdentifier())
                    .fileName(fileInfoDto.getFileName())
                    .fileType(fileInfoDto.getFileType())
                    .bucketName(minioProperties.getBucket())
                    .objectKey(objectName)
                    .totalSize(fileInfoDto.getTotalSize())
                    .chunkSize(fileInfoDto.getChunkSize())
                    .chunkNumber((int) Math.ceil(fileInfoDto.getTotalSize() * 1.0 / fileInfoDto.getChunkSize()))
                    .completed(0).build();
            uploadTaskMapper.insert(build);
            return R.ok("创建 uploadId 成功", build);
        } else if (task.getCompleted().equals(0)){
            // 还没有合并文件
            return R.ok("已经创建，请上传分片", task);
        }else {
            return new R<UploadTask>().setStatus(500).setMessage("错误操作");
        }
    }

    @SneakyThrows
    @Override
    public R<Object> uploadPartAsync(UploadPart uploadPart) {
        // 上传任务
        UploadTask uploadTask = uploadTaskMapper.selectOne(new LambdaQueryWrapper<UploadTask>()
                .eq(UploadTask::getFileIdentifier, uploadPart.getFileIdentifier()));
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
            uploadTaskMapper.delete(wrapper);
        }
        return R.ok("分片上传成功");
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> completeMultipartUploadAsync(FileInfoDTO fileInfoDto) {
        // 上传任务
        UploadTask uploadTask = uploadTaskMapper.selectOne(new LambdaQueryWrapper<UploadTask>()
                .eq(UploadTask::getFileIdentifier, fileInfoDto.getFileIdentifier()));
        CompletableFuture<ListPartsResponse> partsAsync = customMinioClient.listPartsAsync(uploadTask.getBucketName(),
                uploadTask.getObjectKey(),uploadTask.getChunkNumber(), uploadTask.getUploadId());
        List<Part> partList = partsAsync.get().result().partList();
        // 判断是否上传全部分片
        if (uploadTask.getChunkNumber() != partList.size()){
            throw new RuntimeException("分片没有上传完成，不能进行合并");
        }
        Part[] parts = new Part[partList.size()];
        log.info("分片列表大小：{}",partList.size());
        for (int i = 0; i < partList.size(); i++) {
            Part part = partList.get(i);
            parts[i] = new Part(i+1,part.etag());
        }
        try {
            long startTime = System.currentTimeMillis();
            customMinioClient.completeMultipartUploadAsync(uploadTask.getBucketName(), uploadTask.getObjectKey(),
                    uploadTask.getUploadId(), parts);
            long endTime = System.currentTimeMillis();
            log.info("合并分片耗时：{} ms",endTime - startTime);
            uploadTask.setCompleted(1);
            LambdaUpdateWrapper<UploadTask> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(UploadTask::getId, uploadTask.getId());
            uploadTaskMapper.update(uploadTask,wrapper);
        }catch (Exception e){
            throw new RuntimeException("合并分片失败，请重新上传");
        }

        return R.ok("合并分片成功",uploadTask.getUploadId());
    }
}
