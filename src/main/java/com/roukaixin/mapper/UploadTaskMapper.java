package com.roukaixin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roukaixin.pojo.UploadTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分片上传-分片任务记录(UploadTask)表数据库访问层
 *
 * @author 不北咪
 * @date 2023-03-11 14:59:22
 */

@Mapper
public interface UploadTaskMapper extends BaseMapper<UploadTask> {

}

