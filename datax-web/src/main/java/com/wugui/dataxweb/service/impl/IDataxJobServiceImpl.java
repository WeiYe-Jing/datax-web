package com.wugui.dataxweb.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.common.spi.ErrorCode;
import com.alibaba.datax.core.Engine;
import com.alibaba.datax.core.util.ExceptionTracker;
import com.alibaba.datax.core.util.FrameworkErrorCode;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wugui.dataxweb.service.IDataxJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * @program: datax-all
 * @author: huzekang
 * @create: 2019-06-17 11:26
 **/
@Slf4j
@Service
public class IDataxJobServiceImpl implements IDataxJobService {
    private  ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("datax-job-%d").build();

    private  ExecutorService jobPool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    @Override
    public String startJobByJsonStr(String jobJson) {

        jobPool.submit(() -> {

            final String tmpFilePath = "jobTmp-" + System.currentTimeMillis() + ".conf";
            // 根据json写入到临时本地文件
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(tmpFilePath, "UTF-8");
                writer.println(jobJson);

            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }

            try {
                // 使用临时本地文件启动datax作业
                Engine.entry(tmpFilePath);
                //  删除临时文件
                FileUtil.del(new File(tmpFilePath));
            } catch (Throwable e) {
                log.error("\n\n经DataX智能分析,该任务最可能的错误原因是:\n" + ExceptionTracker.trace(e));

                if (e instanceof DataXException) {
                    DataXException tempException = (DataXException) e;
                    ErrorCode errorCode = tempException.getErrorCode();
                    if (errorCode instanceof FrameworkErrorCode) {
                        FrameworkErrorCode tempErrorCode = (FrameworkErrorCode) errorCode;
                    }
                }

            }
        });

        return "success";
    }

}
