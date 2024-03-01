package com.roukaixin.oss.strategy;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roukaixin.oss.properties.LocalProperties;
import com.roukaixin.oss.properties.OssProperties;
import com.roukaixin.pojo.UploadTask;
import com.roukaixin.pojo.dto.FileInfoDTO;
import com.roukaixin.pojo.dto.UploadPart;
import com.roukaixin.service.UploadTaskService;
import com.roukaixin.utils.UploadUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 本地上传实现类
 *
 * @author 不北咪
 * @date 2024/2/21 上午10:52
 */
@Component
@ConditionalOnBean(value = LocalProperties.class)
@Slf4j
public class LocalStrategy implements UploadStrategy{

    @Resource
    private UploadTaskService uploadTaskService;

    @Resource
    private LocalProperties localProperties;

    @Resource
    private OssProperties ossProperties;


    @Override
    public UploadTask createMultipartUpload(FileInfoDTO fileInfo) {
        // 判断上传任务是否存在
        UploadTask one = uploadTaskService.getOne(Wrappers.<UploadTask>lambdaQuery()
                .eq(UploadTask::getFileIdentifier, fileInfo.getFileIdentifier())
                .eq(UploadTask::getOssType, ossProperties.getType())
        );
        if (Objects.isNull(one)) {
            // 上传任务不存在，新增一个上传任务
            String objectKey = UploadUtils.getObjectKey(fileInfo.getFileType(), fileInfo.getFileIdentifier(),
                    fileInfo.getFileName(), ossProperties.getType());
            // 获取保存临时文件的绝对路径
            String tmpSavePath = UploadUtils.getTmpSavePath(localProperties.getRootPath(), objectKey,
                    ossProperties.getType());
            UploadTask build = UploadTask.builder()
                    .ossType(ossProperties.getType())
                    .fileIdentifier(fileInfo.getFileIdentifier())
                    .fileName(fileInfo.getFileName())
                    .fileType(fileInfo.getFileType())
                    .bucketName(localProperties.getRootPath())
                    .objectKey(objectKey)
                    .saveFullPath(UploadUtils.getSavePath(localProperties.getRootPath(), objectKey,
                            ossProperties.getType()) + fileInfo.getFileName())
                    .totalSize(fileInfo.getTotalSize())
                    .chunkSize(fileInfo.getChunkSize())
                    .chunkNumber(
                            BigDecimal.valueOf(fileInfo.getTotalSize())
                                    .subtract(BigDecimal.valueOf(fileInfo.getChunkSize()))
                                    .setScale(0, RoundingMode.UP).intValue()
                    )
                    .build();
            // 把上传任务保存到数据库
            uploadTaskService.save(build);
            File file = new File(tmpSavePath);
            if (!file.exists()) {
                boolean mkdir = file.mkdirs();
                if (!mkdir) {
                    // 创建目录不成功
                    throw new RuntimeException("创建分片上传目录失败");
                }
            }
            return build;
        } else {
            // 任务以存在
            return one;
        }
    }

    @Override
    public void uploadPartAsync(UploadPart uploadPart) {
        UploadTask uploadTask = uploadTaskService.getOne(
                Wrappers.<UploadTask>lambdaQuery().eq(UploadTask::getFileIdentifier, uploadPart.getFileIdentifier())
                        .eq(UploadTask::getOssType, ossProperties.getType())
        );
        if (ObjectUtils.isEmpty(uploadTask)) {
            // 不存在上传任务
            throw new RuntimeException("不存在上传任务，请先创建任务");
        }
        String tmpSavePath = UploadUtils.getTmpSavePath(uploadTask.getBucketName(),
                uploadTask.getObjectKey(), ossProperties.getType());
        String filePath = tmpSavePath + UploadUtils.getSeparator(ossProperties.getType()) + uploadPart.getPartNumber();
        File file = new File(filePath);
        if (!file.exists()) {
            try(FileOutputStream outputStream = new FileOutputStream(file)) {
                MultipartFile partFile = uploadPart.getFile();
                outputStream.write(partFile.getBytes());
            } catch (Exception e) {
                if (e instanceof FileNotFoundException) {
                    // 文件不存在
                    log.error("文件不存在。", e);
                }
                log.error("异常信息", e);
                // 如果发生异常，就把文件当前分片文件删除掉
                if (file.exists()) {
                    boolean delete = file.delete();
                    if (delete) {
                        // 删除成功
                        log.info("删除分片文件成功");
                    } else {
                        log.info("删除分片文件失败");
                    }
                }
            }
        }

    }
}
