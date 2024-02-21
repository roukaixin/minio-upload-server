package com.roukaixin.oss.strategy;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roukaixin.oss.properties.LocalProperties;
import com.roukaixin.oss.properties.OssProperties;
import com.roukaixin.pojo.UploadTask;
import com.roukaixin.pojo.dto.FileInfoDTO;
import com.roukaixin.service.UploadTaskService;
import com.roukaixin.utils.UploadUtils;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

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
                .eq(UploadTask::getFileIdentifier, fileInfo.getFileIdentifier()));
        if (Objects.isNull(one)) {
            // 上传任务不存在，新增一个上传任务
            UploadTask build = UploadTask
                    .builder()
                    .fileIdentifier(fileInfo.getFileIdentifier())
                    .fileName(fileInfo.getFileName())
                    .fileType(fileInfo.getFileType())
                    .bucketName(localProperties.getRootPath())
                    .objectKey(UploadUtils.getObjectKey(fileInfo.getFileType(), fileInfo.getFileIdentifier(),
                            fileInfo.getFileName(), ossProperties.getType()))
                    .totalSize(fileInfo.getTotalSize())
                    .chunkSize(fileInfo.getChunkSize())
                    .chunkNumber(
                            BigDecimal.valueOf(fileInfo.getTotalSize())
                                    .subtract(BigDecimal.valueOf(fileInfo.getChunkSize()))
                                    .setScale(0, RoundingMode.UP).intValue()
                    )
                    .build();
            // 把上传人物保存到数据库
            uploadTaskService.save(build);
            return build;
        } else {
            // 任务以存在
            return one;
        }
    }
}
