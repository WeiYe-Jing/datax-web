package com.wugui.datax.admin.thread;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wugui.datax.admin.config.DataXAdminConfig;
import com.wugui.datax.admin.entity.JobLog;
import com.wugui.datax.admin.enums.HandleCodeEnum;
import com.wugui.datax.admin.log.EtlJobFileAppender;
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
    private Long id;

    public ExecDataXOutputThread(Long id, InputStream is, String logFilePath, String tmpFilePath) {
        this.id = id;
        this.is = is;
        this.logFilePath = logFilePath;
        this.tmpFilePath = tmpFilePath;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String line;
            int handleCode = HandleCodeEnum.SUCCESS.getValue();
            while ((line = br.readLine()) != null) {
                EtlJobFileAppender.appendLog(logFilePath, line);
                if (handleCode != HandleCodeEnum.FAIL.getValue() && line.contains("Exception")) {
                    handleCode = HandleCodeEnum.FAIL.getValue();
                }
                log.info(line);
            }
            //  删除临时文件
            FileUtil.del(new File(tmpFilePath));
            DataXAdminConfig.getDataXAdminConfig().getJobLogService().update(null, Wrappers.<JobLog>lambdaUpdate().set(JobLog::getHandleCode, handleCode).eq(JobLog::getId, id));
        } catch (IOException e) {
            log.error("DataX 执行异常：{0}", e);
        }
    }
}
