package com.roukaixin.controller;

import com.roukaixin.pojo.R;
import com.roukaixin.pojo.UploadTask;
import com.roukaixin.pojo.dto.FileInfoDTO;
import com.roukaixin.service.UploadTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author pankx
 * @date 2023/9/10 23:25
 */
@RestController
@RequestMapping("/v1/minio")
@Tag(name = "上传管理模块")
@RequiredArgsConstructor
public class UploadTaskController {

//    private final MinioClient minioClient;

//    private final MinioProperties minioProperties;

    private final UploadTaskService uploadTaskService;

    @Operation(summary = "创建分片上传")
    @PostMapping("/createMultipartUploadId")
    @CrossOrigin
    public R<UploadTask> createMultipartUploadId(@RequestBody FileInfoDTO fileInfoDto){
        return uploadTaskService.createMultipartUploadId(fileInfoDto);
    }

//    @Operation(summary = "上传分片")
//    @PutMapping("/uploadPartAsync")
//    @CrossOrigin
//    public R<Object> uploadPartAsync(UploadPart uploadPart){
//        return uploadTaskService.uploadPartAsync(uploadPart);
//    }
//
//
//    @Operation(summary = "合并分片")
//    @PostMapping("/completeMultipartUploadAsync")
//    @CrossOrigin
//    public R<String> completeMultipartUploadAsync(@RequestBody FileInfoDTO fileInfoDto){
//        return uploadTaskService.completeMultipartUploadAsync(fileInfoDto);
//    }
//
//    @SneakyThrows
//    @PutMapping("upload")
//    public void upload(MultipartFile file){
//        minioClient.putObject(
//                PutObjectArgs.builder()
//                        .bucket(minioProperties.getBucket())
//                        .object(file.getOriginalFilename())
//                        .stream(file.getInputStream(),file.getSize(),-1)
//                        .contentType(file.getContentType())
//                .build());
//    }
}
