package com.wugui.dataxweb.thread;

import cn.hutool.core.io.FileUtil;
import com.wugui.dataxweb.log.EtlJobFileAppender;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Datax运行日志输出
 *
 * @author jingwk
 * @version 1.0
 * @since 2019/11/09
 */

@Slf4j
public class ExecDataXOutputThread extends Thread {

    private InputStream is;
    private String logFilePath;
    private String tmpFilePath;

    public ExecDataXOutputThread(InputStream is, String logFilePath, String tmpFilePath) {
        this.is = is;
        this.logFilePath = logFilePath;
        this.tmpFilePath = tmpFilePath;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                EtlJobFileAppender.appendLog(logFilePath, line);
                log.info(line);
            }
            //  删除临时文件
            FileUtil.del(new File(tmpFilePath));
        } catch (IOException e) {
            log.error("DataX 执行异常：{0}", e);
        }
    }
}
