package com.wugui.datax.executor.service.jobhandler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.wugui.datatx.core.biz.model.HandleProcessCallbackParam;
import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.biz.model.TriggerParam;
import com.wugui.datatx.core.handler.AbstractJobHandler;
import com.wugui.datatx.core.handler.annotation.JobHandler;
import com.wugui.datatx.core.log.JobLogger;
import com.wugui.datatx.core.thread.ProcessCallbackThread;
import com.wugui.datatx.core.util.ProcessUtil;
import com.wugui.datax.executor.service.logparse.LogStatistics;
import com.wugui.datax.executor.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wugui.datax.executor.service.command.BuildCommand.buildDataXExecutorCmd;
import static com.wugui.datax.executor.service.command.BuildCommand.buildDataXParamToMap;
import static com.wugui.datax.executor.service.jobhandler.DataXConstant.DEFAULT_JSON;
import static com.wugui.datax.executor.service.logparse.AnalysisStatistics.analysisStatisticsLog;

/**
 * DataX任务运行
 *
 * @author jingwk 2019-11-16
 */

@JobHandler(value = "executorJobHandler")
@Component
public class ExecutorJobHandler extends AbstractJobHandler {

    @Value("${datax.executor.jsonpath}")
    private String jsonPath;

    @Value("${datax.pypath}")
    private String dataXPyPath;

    @Value("${datax.executor.pythonpath}")
    private  String pythonPath;


    private  static final Pattern VARIABLE_PATTERN = Pattern.compile("(\\$)\\{?(\\w+)\\}?");
    @Override
    public ReturnT<String> execute(TriggerParam trigger) {

        int exitValue = -1;
        Thread errThread = null;
        String tmpFilePath;
        LogStatistics logStatistics = null;
        //Generate JSON temporary file
//        String jobJson=replaceVariable(trigger.getJobJson(),null);

        HashMap<String, String> keyValueMap = buildDataXParamToMap(trigger);
        String jobJson = replaceVariable(trigger.getJobJson(),keyValueMap);
        tmpFilePath = generateTemJsonFile(jobJson);
        try {
            String[] cmdarrayFinal = buildDataXExecutorCmd(trigger, tmpFilePath, dataXPyPath,pythonPath);
            String cmd = StringUtils.join(cmdarrayFinal, " ");
            JobLogger.log("------------------Command CMD is :" + cmd);
            final Process process = Runtime.getRuntime().exec(cmdarrayFinal);
            String prcsId = ProcessUtil.getProcessId(process);
            JobLogger.log("------------------DataX process id: " + prcsId);
            JOB_TEM_FILES.put(prcsId, tmpFilePath);
            //update datax process id
            HandleProcessCallbackParam prcs = new HandleProcessCallbackParam(trigger.getLogId(), trigger.getLogDateTime(), prcsId);
            ProcessCallbackThread.pushCallBack(prcs);
            // log-thread
            Thread futureThread;
            FutureTask<LogStatistics> futureTask = new FutureTask<>(() -> analysisStatisticsLog(new BufferedInputStream(process.getInputStream())));
            futureThread = new Thread(futureTask);
            futureThread.start();

            errThread = new Thread(() -> {
                try {
                    analysisStatisticsLog(new BufferedInputStream(process.getErrorStream()));
                } catch (IOException e) {
                    JobLogger.log(e);
                }
            });

            logStatistics = futureTask.get();
            errThread.start();
            // exit code: 0=success, 1=error
            exitValue = process.waitFor();
            // log-thread join
            errThread.join();
        } catch (Exception e) {
            JobLogger.log(e);
        } finally {
            if (errThread != null && errThread.isAlive()) {
                errThread.interrupt();
            }
            //  删除临时文件
            if (FileUtil.exist(tmpFilePath)) {
                FileUtil.del(new File(tmpFilePath));
            }
        }
        if (exitValue == 0) {
            return new ReturnT<>(200, logStatistics.toString());
        } else {
            return new ReturnT<>(AbstractJobHandler.FAIL.getCode(), "command exit value(" + exitValue + ") is failed");
        }
    }


    public static String replaceVariable(final String param,HashMap<String, String> variableMap) {
        Map<String, String> mapping = new HashMap<String, String>();

        Matcher matcher = VARIABLE_PATTERN.matcher(param);
        while (matcher.find()) {
            String variable = matcher.group(2);

            String value = variableMap.get(variable).toString();
            if (StringUtils.isBlank(value)) {
                value = matcher.group();
            }
            mapping.put(matcher.group(), value);
        }

        String retString = param;
        for (final String key : mapping.keySet()) {
            retString = retString.replace(key, mapping.get(key));
        }

        return retString;
    }



    private String generateTemJsonFile(String jobJson) {
        String tmpFilePath;
        String dataXHomePath = SystemUtils.getDataXHomePath();
        if (StringUtils.isNotEmpty(dataXHomePath)) {
            jsonPath = dataXHomePath + DEFAULT_JSON;
        }
        if (!FileUtil.exist(jsonPath)) {
            FileUtil.mkdir(jsonPath);
        }
        tmpFilePath = jsonPath + "jobTmp-" + IdUtil.simpleUUID() + ".conf";
        // 根据json写入到临时本地文件
        try (PrintWriter writer = new PrintWriter(tmpFilePath, "UTF-8")) {
            writer.println(jobJson);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            JobLogger.log("JSON 临时文件写入异常：" + e.getMessage());
        }
        return tmpFilePath;
    }

}
