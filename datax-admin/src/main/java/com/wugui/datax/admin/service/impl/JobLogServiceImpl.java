package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datax.admin.log.EtlJobLogger;
import com.wugui.datax.admin.log.LogResult;
import com.wugui.datax.admin.mapper.JobLogMapper;
import com.wugui.datax.admin.entity.JobLog;
import com.wugui.datax.admin.service.IJobLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 抽取日志记录表表服务实现类
 *
 * @author zhouhongfa@gz-yibo.com
 * @version v1.0
 * @since 2019-06-27
 */
@Service
@Transactional(readOnly = true)
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements IJobLogService {

    @Override
    public LogResult viewJobLog(String logFilePath, int fromLineNum) {
            return EtlJobLogger.readLog(logFilePath, fromLineNum);
    }
}