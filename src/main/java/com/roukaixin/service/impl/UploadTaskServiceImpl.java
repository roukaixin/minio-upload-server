package com.roukaixin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roukaixin.mapper.UploadTaskMapper;
import com.roukaixin.oss.strategy.UploadStrategy;
import com.roukaixin.oss.strategy.UploadStrategyFactory;
import com.roukaixin.pojo.R;
import com.roukaixin.pojo.UploadTask;
import com.roukaixin.pojo.dto.FileInfoDTO;
import com.roukaixin.pojo.dto.UploadPart;
import com.roukaixin.service.UploadTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 分片上传-分片任务记录
 *
 * @author 不北咪
 * @date 2023/3/11 23:11
 */
@Service
@Slf4j
public class UploadTaskServiceImpl extends ServiceImpl<UploadTaskMapper, UploadTask> implements UploadTaskService {


    @Override
    public R<UploadTask> createMultipartUploadId(FileInfoDTO fileInfoDto) {
        UploadStrategy instance = UploadStrategyFactory.getInstance();
        UploadTask multipartUpload = instance.createMultipartUpload(fileInfoDto);
        return R.ok("创建分片任务成功", multipartUpload);
    }

    @Override
    public R<Object> uploadPartAsync(UploadPart uploadPart) {
        UploadStrategy instance = UploadStrategyFactory.getInstance();
        instance.uploadPartAsync(uploadPart);
        return null;
    }

//    @Resource
//    private CustomMinioClient customMinioClient;

//    @Resource
//    private MinioProperties minioProperties;

//    @Override
//    public R<UploadTask> createMultipartUploadId(FileInfoDTO fileInfoDto) {
//        LambdaQueryWrapper<UploadTask> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(UploadTask::getFileIdentifier,fileInfoDto.getFileIdentifier());
//        UploadTask task = uploadTaskMapper.selectOne(wrapper);
//        if (task == null) {
//            // 没有上传任务
//            String objectName = "/" + DateUtil.formatDate(new Date());
//            Multimap<String, String> headers = null;
//            if (StringUtils.hasText(fileInfoDto.getFileType())) {
//                String type = fileInfoDto.getFileType().split("/")[0];
//                objectName = objectName + "/" + type;
//                headers = HashMultimap.create();
//                headers.put("Content-Type", fileInfoDto.getFileType());
//            }
//            objectName = objectName + "/" + fileInfoDto.getFileIdentifier() +
//                    fileInfoDto.getFileName().substring(fileInfoDto.getFileName().lastIndexOf("."));
//            // 生成 uploadId
//            String uploadId = customMinioClient.uploadId(minioProperties.getBucket(), null, objectName, headers, null);
//
//            UploadTask build = UploadTask.builder()
//                    .uploadId(uploadId)
//                    .fileIdentifier(fileInfoDto.getFileIdentifier())
//                    .fileName(fileInfoDto.getFileName())
//                    .fileType(fileInfoDto.getFileType())
//                    .bucketName(minioProperties.getBucket())
//                    .objectKey(objectName)
//                    .totalSize(fileInfoDto.getTotalSize())
//                    .chunkSize(fileInfoDto.getChunkSize())
//                    .chunkNumber((int) Math.ceil(fileInfoDto.getTotalSize() * 1.0 / fileInfoDto.getChunkSize()))
//                    .completed(false).build();
//            uploadTaskMapper.insert(build);
//            return R.ok("创建 uploadId 成功", build);
//        } else if (task.isCompleted()) {
//            // 已经合并了
//            return R.ok("分片已经合并",task);
//        } else {
//            // 还没有合并文件
//            return R.ok("已经创建，请上传分片", task);
//        }
//    }
//
//    @SneakyThrows
//    @Override
//    public R<Object> uploadPartAsync(UploadPart uploadPart) {
//        // 上传任务
//        UploadTask uploadTask = uploadTaskMapper.selectOne(new LambdaQueryWrapper<UploadTask>()
//                .eq(UploadTask::getFileIdentifier, uploadPart.getFileIdentifier()));
//        Multimap<String, String> headers = null;
//        if (StringUtils.hasText(uploadTask.getFileType())) {
//            headers = HashMultimap.create();
//            headers.put("Content-Type", uploadTask.getFileType());
//        }
//        String etag;
//        try {
//            CompletableFuture<UploadPartResponse> partAsync = customMinioClient.uploadPartAsync(
//                    uploadTask.getBucketName(), uploadTask.getObjectKey(),
//                    uploadPart.getFile().getInputStream(), uploadPart.getFile().getSize(),
//                    uploadTask.getUploadId(), uploadPart.getPartNumber(), headers);
//            etag = partAsync.get().etag();
//            log.info("etag：{}", etag);
//        } catch (Exception e) {
//            log.error("上传文件失败", e);
//            // 上传失败，清除上传任务
//            customMinioClient.abortMultipartUploadAsync(uploadTask.getBucketName(), uploadTask.getObjectKey(),
//                    uploadTask.getUploadId());
//            LambdaQueryWrapper<UploadTask> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(UploadTask::getUploadId, uploadTask.getUploadId());
//            uploadTaskMapper.delete(wrapper);
//        }
//        return R.ok("分片上传成功");
//    }
//
//    @SneakyThrows
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public R<String> completeMultipartUploadAsync(FileInfoDTO fileInfoDto) {
//        // 上传任务
//        UploadTask uploadTask = uploadTaskMapper.selectOne(new LambdaQueryWrapper<UploadTask>()
//                .eq(UploadTask::getFileIdentifier, fileInfoDto.getFileIdentifier()));
//        CompletableFuture<ListPartsResponse> partsAsync = customMinioClient.listPartsAsync(uploadTask.getBucketName(),
//                uploadTask.getObjectKey(),uploadTask.getChunkNumber(), uploadTask.getUploadId());
//        List<Part> partList = partsAsync.get().result().partList();
//        // 判断是否上传全部分片
//        if (uploadTask.getChunkNumber() != partList.size()){
//            throw new RuntimeException("分片没有上传完成，不能进行合并");
//        }
//        Part[] parts = new Part[partList.size()];
//        log.info("分片列表大小：{}",partList.size());
//        for (int i = 0; i < partList.size(); i++) {
//            Part part = partList.get(i);
//            parts[i] = new Part(i+1,part.etag());
//        }
//        try {
//            long startTime = System.currentTimeMillis();
//            customMinioClient.completeMultipartUploadAsync(uploadTask.getBucketName(), uploadTask.getObjectKey(),
//                    uploadTask.getUploadId(), parts);
//            long endTime = System.currentTimeMillis();
//            log.info("合并分片耗时：{} ms",endTime - startTime);
//            uploadTask.setCompleted(true);
//            LambdaUpdateWrapper<UploadTask> wrapper = new LambdaUpdateWrapper<>();
//            wrapper.eq(UploadTask::getId, uploadTask.getId());
//            uploadTaskMapper.update(uploadTask,wrapper);
//        }catch (Exception e){
//            throw new RuntimeException("合并分片失败，请重新上传");
//        }
//
//        return R.ok("合并分片成功",uploadTask.getUploadId());
//    }
}
