package com.wugui.dataxweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wugui.dataxweb.entity.JobLog;
import com.wugui.dataxweb.log.LogResult;

/**
 * 抽取日志记录表表服务接口
 *
 * @author zhouhongfa@gz-yibo.com
 * @version v1.0
 * @since 2019-06-27
 */
public interface IJobLogService extends IService<JobLog> {
    /**
     * 获取日志详情
     * @param logFilePath 日志文件路径
     * @param fromLineNum 行数
     * @return
     */
    LogResult viewJobLog(String logFilePath,int fromLineNum);
}