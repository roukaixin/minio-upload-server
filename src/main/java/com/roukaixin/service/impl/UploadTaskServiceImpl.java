package com.roukaixin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roukaixin.mapper.UploadTaskMapper;
import com.roukaixin.oss.properties.OssProperties;
import com.roukaixin.oss.strategy.UploadStrategy;
import com.roukaixin.oss.strategy.UploadStrategyFactory;
import com.roukaixin.pojo.R;
import com.roukaixin.pojo.UploadTask;
import com.roukaixin.pojo.dto.FileInfoDTO;
import com.roukaixin.pojo.dto.UploadPart;
import com.roukaixin.service.UploadTaskService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分片上传-分片任务记录
 *
 * @author 不北咪
 * @date 2023/3/11 23:11
 */
@Service
@Slf4j
public class UploadTaskServiceImpl extends ServiceImpl<UploadTaskMapper, UploadTask> implements UploadTaskService {


    @Resource
    private OssProperties ossProperties;

    @Override
    public R<UploadTask> createMultipartUploadId(FileInfoDTO fileInfoDto) {
        UploadStrategy instance = UploadStrategyFactory.getInstance();
        UploadTask multipartUpload = instance.createMultipartUpload(fileInfoDto);
        return R.ok("创建分片任务成功", multipartUpload);
    }

    @Override
    public R<String> uploadPartAsync(UploadPart uploadPart) {
        UploadStrategy instance = UploadStrategyFactory.getInstance();
        instance.uploadPartAsync(uploadPart);
        return R.ok("分片上传成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> completeMultipartUploadAsync(FileInfoDTO fileInfoDto) {
        UploadStrategy instance = UploadStrategyFactory.getInstance();
        if (!instance.completeMultipartUploadAsync(fileInfoDto)) {
            // 合并失败，删除上传任务
            remove(Wrappers.<UploadTask>lambdaQuery()
                    .eq(UploadTask::getFileIdentifier, fileInfoDto.getFileIdentifier())
                    .eq(UploadTask::getOssType, ossProperties.getType()));
            throw new RuntimeException("合并失败");
        }
        return R.ok("合并成功");
    }

}
