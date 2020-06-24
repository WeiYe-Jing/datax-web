package com.wugui.datax.executor.service.logparse;

import com.wugui.datatx.core.log.JobLogger;
import com.wugui.datatx.core.util.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.wugui.datax.executor.service.jobhandler.DataXConstant.*;

/**
 * Log of analysis statistics
 *
 * @author jingwk 2020-06-07
 */

public class AnalysisStatistics {



    /**
     * Log of analysis statistics
     *
     * @param inputStream
     * @throws IOException
     */
    public static LogStatistics analysisStatisticsLog(InputStream inputStream) throws IOException {

        LogStatistics logStatistics = new LogStatistics();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(TASK_START_TIME_SUFFIX)) {
                    logStatistics.setTaskStartTime(subResult(line));
                } else if (line.contains(TASK_END_TIME_SUFFIX)) {
                    logStatistics.setTaskEndTime(subResult(line));
                } else if (line.contains(TASK_TOTAL_TIME_SUFFIX)) {
                    logStatistics.setTaskTotalTime(subResult(line));
                } else if (line.contains(TASK_AVERAGE_FLOW_SUFFIX)) {
                    logStatistics.setTaskAverageFlow(subResult(line));
                } else if (line.contains(TASK_RECORD_WRITING_SPEED_SUFFIX)) {
                    logStatistics.setTaskRecordWritingSpeed(subResult(line));
                } else if (line.contains(TASK_RECORD_READER_NUM_SUFFIX)) {
                    logStatistics.setTaskRecordReaderNum(Integer.parseInt(subResult(line)));
                } else if (line.contains(TASK_RECORD_WRITING_NUM_SUFFIX)) {
                    logStatistics.setTaskRecordWriteFailNum(Integer.parseInt(subResult(line)));
                }
                JobLogger.log(line);
            }
            reader.close();
            inputStream = null;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return logStatistics;
    }

    private static String subResult(String line) {
        if (StringUtils.isBlank(line)) return Constants.STRING_BLANK;
        int pos = line.indexOf(Constants.SPLIT_SCOLON);
        if (pos > 0) return line.substring(pos + 1).trim();
        return line.trim();
    }
}
