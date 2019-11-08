package com.wugui.dataxweb.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.LogOutputStream;

import java.util.LinkedList;
import java.util.List;

/**
 * CollectingLogOutputStream
 *
 * @author jingwk
 * @version 1.0
 * @since 2019/11/17
 */

@Slf4j
public class CollectingLogOutputStream extends LogOutputStream {

    private final List<String> lines = new LinkedList<String>();

    private String logFilePath;

    public CollectingLogOutputStream(String logFilePath){
        this.logFilePath=logFilePath;
    }

    @Override
    protected void processLine(String line, int logLevel) {
        lines.add(line);
        EtlJobFileAppender.appendLog(logFilePath, line);
        log.info("日志级别{}：{}", logLevel, line);
    }

    public List<String> getLines() {
        return lines;
    }
}
