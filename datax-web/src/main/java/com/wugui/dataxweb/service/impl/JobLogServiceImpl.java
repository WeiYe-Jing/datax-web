package com.wugui.dataxweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.dataxweb.dao.JobLogMapper;
import com.wugui.dataxweb.entity.JobLog;
import com.wugui.dataxweb.log.EtlJobLogger;
import com.wugui.dataxweb.log.LogResult;
import com.wugui.dataxweb.service.IJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IJobLogService jobLogService;

    @Override
    public LogResult viewJobLog(String logFilePath,int fromLineNum) {
            return EtlJobLogger.readLog(logFilePath, fromLineNum);
    }
}