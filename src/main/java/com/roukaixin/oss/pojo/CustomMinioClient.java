package com.roukaixin.oss.pojo;

import com.google.common.collect.Multimap;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Part;
import lombok.SneakyThrows;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

/**
 * 自定义 minio 客户端
 * @author pankx
 * @date 2023/9/11 0:16
 */
public class CustomMinioClient extends MinioAsyncClient {

    public CustomMinioClient(MinioAsyncClient client) {
        super(client);
    }

    public static CustomMinioClient build(MinioAsyncClient client){
        return new CustomMinioClient(client);
    }

    /**
     * 创建分片上传
     * @param bucketName 桶名
     * @param region 地区
     * @param objectName 对象名字（minio保存文件路径的全名）
     * @param headers 请求头（需要传递 headers.put("Content-Type", contentType)）
     * @param extraQueryParams 额外请求参数 (可选)
     * @return CompletableFuture
     */
    @Override
    protected CompletableFuture<CreateMultipartUploadResponse> createMultipartUploadAsync(String bucketName, String region, String objectName, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.createMultipartUploadAsync(bucketName, region, objectName, headers, extraQueryParams);
    }

    /**
     * 分片上传 uploadId
     * @param bucketName 桶名
     * @param region 地区
     * @param objectName 对象名字（minio保存文件路径的全名）
     * @param headers 请求头（需要传递 headers.put("Content-Type", contentType)）
     * @param extraQueryParams 额外请求参数 （可选）
     * @return CompletableFuture
     */
    @SneakyThrows
    public String uploadId(String bucketName, String region, String objectName, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) {
        return this.createMultipartUploadAsync(bucketName, region, objectName, headers, extraQueryParams)
                .get()
                .result()
                .uploadId();
    }

    /**
     * 获取分片上传 uploadId
     * @param bucketName 桶名
     * @param objectName 存储文件路径
     * @param headers 头 （例如：文件类型（Content-Type））
     * @return uploadId
     */
    @SneakyThrows
    public String uploadId(String bucketName, String objectName, Multimap<String, String> headers) {
        return
                this
                        .createMultipartUploadAsync(bucketName, null, objectName, headers, null)
                        .get()
                        .result()
                        .uploadId();
    }

    /**
     * 上传分片
     * @param bucketName 桶名
     * @param region 地区 （可选）
     * @param objectName 文件路径
     * @param data 文件数据
     * @param length 数据长度
     * @param uploadId uploadId
     * @param partNumber 第几个分片
     * @param extraHeaders 额外请求头 （可选）
     * @param extraQueryParams 额外查询参数 （可选）
     * @return CompletableFuture<UploadPartResponse> 上传分片之后的分片信息
     */
    @Override
    protected CompletableFuture<UploadPartResponse> uploadPartAsync(String bucketName, String region, String objectName, Object data, long length, String uploadId, int partNumber, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.uploadPartAsync(bucketName, region, objectName, data, length, uploadId, partNumber, extraHeaders, extraQueryParams);
    }

    /**
     * 上传分片
     * @param bucketName 桶名
     * @param objectName 文件路径
     * @param data 文件数据
     * @param length 数据长度
     * @param uploadId uploadId
     * @param partNumber 第几个分片 （eg：1：第一个分片）
     * @return CompletableFuture<UploadPartResponse> 上传分片之后的分片信息
     */
    @SneakyThrows
    public CompletableFuture<UploadPartResponse> uploadPartAsync(String bucketName, String objectName, Object data, long length, String uploadId, int partNumber,Multimap<String, String> extraHeaders) {
        return uploadPartAsync(bucketName, null, objectName, data, length, uploadId, partNumber, extraHeaders, null);
    }


    /**
     * 合并分片
     * @param bucketName 桶名
     * @param region 地区
     * @param objectName 文件路径
     * @param uploadId uploadId
     * @param parts 分片数据列表
     * @param extraHeaders 额外头部 （可选）
     * @param extraQueryParams 额外查询参数 （可选）
     * @return CompletableFuture<ObjectWriteResponse>
     */
    @Override
    protected CompletableFuture<ObjectWriteResponse> completeMultipartUploadAsync(String bucketName, String region, String objectName, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.completeMultipartUploadAsync(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams);
    }

    /**
     * 合并分片
     * @param bucketName 桶名
     * @param objectName 文件路径
     * @param uploadId uploadId
     * @param parts 分片数据列表
     * @return CompletableFuture<ObjectWriteResponse>
     */
    @SneakyThrows
    public CompletableFuture<ObjectWriteResponse> completeMultipartUploadAsync(String bucketName, String objectName, String uploadId, Part[] parts) {
        return completeMultipartUploadAsync(bucketName,null,objectName,uploadId,parts,null,null);
    }

    /**
     * 分片列表
     * @param bucketName 桶名
     * @param region 地区 （可选）
     * @param objectName 文件路径
     * @param maxParts 多少个分片 （可选）
     * @param partNumberMarker 第几个分片 （可选）
     * @param uploadId uploadId
     * @param extraHeaders 额外头 （可选）
     * @param extraQueryParams 额外查询参数 （可选）
     * @return CompletableFuture<ListPartsResponse>
     */
    @Override
    protected CompletableFuture<ListPartsResponse> listPartsAsync(String bucketName, String region, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.listPartsAsync(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams);
    }


    /**
     * 分片列表
     * @param bucketName 桶名
     * @param objectName 文件路径
     * @param maxParts 多少个分片，可选值：1-10000 （可选，不填默认10000）
     * @param uploadId uploadId
     * @return CompletableFuture<ListPartsResponse>
     */
    @SneakyThrows
    public CompletableFuture<ListPartsResponse> listPartsAsync(String bucketName, String objectName, Integer maxParts, String uploadId) {
        return listPartsAsync(bucketName,null,objectName,maxParts,null,uploadId,null,null);
    }

    /**
     * 中止上传分片
     * @param bucketName 桶名
     * @param region 地区
     * @param objectName 文件路径
     * @param uploadId uploadId
     * @param extraHeaders 额外头 （可选）
     * @param extraQueryParams 额外查询参数 （可选）
     * @return CompletableFuture<AbortMultipartUploadResponse>
     */
    @Override
    protected CompletableFuture<AbortMultipartUploadResponse> abortMultipartUploadAsync(String bucketName, String region, String objectName, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.abortMultipartUploadAsync(bucketName, region, objectName, uploadId, extraHeaders, extraQueryParams);
    }

    /**
     * 中止上传分片
     * @param bucketName 桶名
     * @param objectName 文件路径
     * @param uploadId uploadId
     * @return CompletableFuture<AbortMultipartUploadResponse>
     */
    @SneakyThrows
    public CompletableFuture<AbortMultipartUploadResponse> abortMultipartUploadAsync(String bucketName, String objectName, String uploadId) {
        return abortMultipartUploadAsync(bucketName, null, objectName, uploadId, null, null);
    }
}
