package com.wugui.dataxweb.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Appender
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/6/27
 */
public class EtlJobFileAppender {
    private static Logger logger = LoggerFactory.getLogger(EtlJobFileAppender.class);

    // support log for child thread of job handler
//	public static ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    public static final InheritableThreadLocal<String> contextHolder = new InheritableThreadLocal<String>();

    /**
     * append log
     *
     * @param logFileName
     * @param appendLog
     */
    public static void appendLog(String logFileName, String appendLog) {

        // log file
        if (logFileName == null || logFileName.trim().length() == 0) {
            return;
        }
        File logFile = new File(logFileName);
        //getParentFile() 获取上级目录
        if (!logFile.getParentFile().exists()) {
            //创建目录
            logFile.getParentFile().mkdirs();
        }


        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return;
            }
        }

        // log
        if (appendLog == null) {
            appendLog = "";
        }
        appendLog += "\r\n";

        // append file content
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logFile, true);
            fos.write(appendLog.getBytes("utf-8"));
            fos.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

    }

}
