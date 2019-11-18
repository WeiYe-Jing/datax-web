package com.wugui.datax.admin.service.impl;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wugui.datax.admin.log.EtlJobFileAppender;
import com.wugui.datax.admin.log.EtlJobLogger;
import com.wugui.datax.admin.log.LogResult;
import com.wugui.datax.admin.service.IDataxJobService;
import com.wugui.datax.admin.service.IJobLogService;
import com.wugui.datax.admin.util.ProcessUtil;
import com.wugui.datax.admin.entity.JobLog;
import com.wugui.datax.admin.thread.ExecDataXOutputThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @program: com.wugui.datax-all
 * @author: huzekang
 * @create: 2019-06-17 11:26
 **/
@Slf4j
@Service
public class IDataxJobServiceImpl implements IDataxJobService {

    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("com.wugui.datax-job-%d").build();

    private ExecutorService jobPool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    private String logFilePath;

    private final static ConcurrentMap<String, String> jobTmpFiles = Maps.newConcurrentMap();

    /**
     * 日志文件保存目录
     */
    @Value("${app.etlLogDir}")
    private String etlLogDir;

    @Autowired
    private IJobLogService jobLogService;

    @Override
    public String startJob(String jobJson, Long jobId) {

        jobPool.submit(() -> {
            final String tmpFilePath = "jobTmp-" + System.currentTimeMillis() + ".conf";
            // 根据json写入到临时本地文件
            try (PrintWriter writer = new PrintWriter(tmpFilePath, "UTF-8")) {
                writer.println(jobJson);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                log.info("JSON 临时文件写入异常：{0}", e);
            }
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"python", getDataXPyPath(), tmpFilePath});
                String processId = ProcessUtil.getProcessId(p);
                jobTmpFiles.put(processId, tmpFilePath);
                Long id = saveLog(jobId, processId);
                EtlJobFileAppender.appendLog(logFilePath, "\n\nJob:  " + jobId + ",Datax运行进程Id:  " + processId);
                ExecDataXOutputThread output = new ExecDataXOutputThread(id, p.getInputStream(), logFilePath, tmpFilePath);
                ExecDataXOutputThread error = new ExecDataXOutputThread(id, p.getErrorStream(), logFilePath, tmpFilePath);
                output.start();
                error.start();
            } catch (Exception e) {
                EtlJobFileAppender.appendLog(logFilePath, "\n\n经DataX智能分析,该任务最可能的错误原因是:\n" + "DATAX_HOME或者Job数据库配置信息有误");
                log.error("job 执行异常：{0}", e);
            }
        });
        return "success";
    }

    private Long saveLog(Long jobId, String processId) {
        //根据jobId和当前时间戳生成日志文件名
        String logFileName = jobId.toString().concat("_").concat(StrUtil.toString(System.currentTimeMillis()).concat(".log"));
        logFilePath = etlLogDir.concat(logFileName);
        //记录日志
        JobLog jobLog = new JobLog();
        jobLog.setJobId(jobId);
        jobLog.setLogFilePath(logFilePath);
        jobLog.setPid(processId);
        jobLog.setHandleCode(0);
        jobLog.setHandleTime(new Date());
        jobLogService.save(jobLog);
        return jobLog.getId();
    }

    private String getDataXPyPath() {
        String dataxPyPath;
        String dataXHome = System.getenv("DATAX_HOME");
        if (StringUtils.isBlank(dataXHome)) {
            log.error("DATAX_HOME 环境变量为NULL");
            EtlJobFileAppender.appendLog(logFilePath, "DATAX_HOME 环境变量为NULL");
        }
        String osName = System.getProperty("os.name");
        dataXHome = osName.contains("Windows") ? (!dataXHome.endsWith("\\") ? dataXHome.concat("\\") : dataXHome) : (!dataXHome.endsWith("/") ? dataXHome.concat("/") : dataXHome);
        dataxPyPath = dataXHome + "datax.py";
        return dataxPyPath;
    }


    @Override
    public LogResult viewJogLog(Long id, int fromLineNum) {
        QueryWrapper<JobLog> queryWrapper = new QueryWrapper<>();
        //根据id获取最新的日志文件路径
        queryWrapper.lambda().eq(JobLog::getJobId, id).orderByDesc(JobLog::getHandleTime);
        List<JobLog> list = jobLogService.list(queryWrapper);
        //取最新的一条记录
        if (list.isEmpty()) {
            return new LogResult(1, 1, "没有找到对应的日志文件！", true);
        } else {
            //取出路径，读取文件
            return EtlJobLogger.readLog(list.get(0).getLogFilePath(), fromLineNum);
        }
    }

    @Override
    public Boolean killJob(String pid, Long id) {
        boolean result = ProcessUtil.killProcessByPid(pid);
        //  删除临时文件
        if (!CollectionUtils.isEmpty(jobTmpFiles)) {
            String pathname = jobTmpFiles.get(pid);
            if (pathname != null) {
                FileUtil.del(new File(pathname));
            }
        }
        jobLogService.update(null, Wrappers.<JobLog>lambdaUpdate().set(JobLog::getHandleMsg, "job running，killed").set(JobLog::getHandleCode, 500).eq(JobLog::getId, id));
        return result;
    }

}
