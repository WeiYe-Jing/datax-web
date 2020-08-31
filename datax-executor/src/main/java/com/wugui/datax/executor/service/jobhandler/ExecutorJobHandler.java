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
import com.wugui.datax.executor.util.MapUtils;
import com.wugui.datax.executor.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wugui.datax.executor.service.command.BuildCommand.buildDataXExecutorCmd;
import static com.wugui.datax.executor.service.command.BuildCommand.buildDataXParamToMap;
import static com.wugui.datax.executor.service.jobhandler.DataXConstant.DEFAULT_DATAX_PY;
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

        HashMap<String, String> keyValueMap = buildDataXParamToMap(trigger);

        String jobJson = replaceVariable(trigger.getJobJson(),keyValueMap);

        //Generate JSON temporary file
        tmpFilePath = generateTemJsonFile(jobJson);
        try {
            //检查运行环境
            checkEnv();
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

    /**
     * 检查运行环境
     * 打印到日志中
     */
    private void checkEnv() {
        //检查是否手动配置PythonHOME
        Map<Boolean, String> checkPythonHOME = checkpythonPath();
        JobLogger.log("------------------"+MapUtils.getFirstOrNull(checkPythonHOME));
        //检查是否正确配置datax.py
        Map<Boolean, String> checkDataXPATH = checkDataXPATH();
        JobLogger.log("------------------"+MapUtils.getFirstOrNull(checkDataXPATH));
        //检查python版本是否是2.x版本
        Map<Boolean, String> checkPyVersionIs2X = checkPyVersionIs2X();
        JobLogger.log("------------------"+MapUtils.getFirstOrNull(checkPyVersionIs2X));
    }


    public static String replaceVariable(final String param,HashMap<String, String> variableMap) {
        Map<String, String> mapping = new HashMap<String, String>();

        Matcher matcher = VARIABLE_PATTERN.matcher(param);
        while (matcher.find()) {
            String variable = matcher.group(2);

            String value = variableMap.get(variable);
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
        tmpFilePath = jsonPath + File.separator + "jobTmp-" + IdUtil.simpleUUID() + ".conf";
        // 根据json写入到临时本地文件
        try (PrintWriter writer = new PrintWriter(tmpFilePath, "UTF-8")) {
            writer.println(jobJson);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            JobLogger.log("JSON temporary file write exception：" + e.getMessage());
        }
        return tmpFilePath;
    }

    /**
     * 检查是否手动配置了pythonPath
     *
     * @return
     */
    public Map<Boolean, String> checkpythonPath() {
        Map<Boolean, String> result = new HashMap<Boolean, String>();
        if (pythonPath == null || pythonPath.equals("python")) {
            result.put(false, "没有手动配置pythonPath");
            return result;
        } else {
            result.put(true, "手动配置了pythonPath为: " + pythonPath);
            return result;
        }
    }

    /**
     * 检查dataX的配置是否正确
     * 避免用户将PYTHON_PATH设置为python的环境变量路径
     *
     * @return k:true or false
     * v:msg
     */
    public Map<Boolean, String> checkDataXPATH() {
        Map<Boolean, String> result = new HashMap<Boolean, String>();
        //兼容只配置 DATAX_HOME 情况
        String dataxHome = SystemUtils.getDataXHomePath();
        String dataxHomePyFile = dataxHome + "bin" +  File.separator + DEFAULT_DATAX_PY;
        if (!FileUtil.exist(dataxHomePyFile) || dataXPyPath == null ||
                !dataXPyPath.endsWith("datax.py")
                || "".equals(dataXPyPath)
        ) {
            result.put(false, String.format("%S %S", "你的datax配置的可能不正确,您配置的DataX的地址为" + dataXPyPath, "应该为:$DATAX_HOME/bin/datax.py"));
            return result;
        }
        result.put(true, "您配置的DataX的地址正确!");
        return result;
    }

    /**
     * 检查python的环境是否是2.X版本
     *
     * @return true 为是2.x版本
     * false 则不是 or 检查失败
     */
    public Map<Boolean, String> checkPyVersionIs2X() {
        Map<Boolean, String> result = new HashMap<Boolean, String>();
        String pythonVersion = getPyVersion();
        //判断python版本
        if (pythonVersion != null && !"".equals(pythonVersion.toString())) {
            if (pythonVersion.toLowerCase().startsWith("python 2.") || pythonVersion.toLowerCase().startsWith("python2.")) {
                result.put(true, "python版本为2.x!");
                return result;
            } else {
                result.put(false, "python版本不是2.x!,您的python版本为: " + pythonVersion.toString());
                return result;
            }
        } else {
            result.put(false, "检查python版本失败!可能系统中无python环境...");
            return result;
        }
    }

    /**
     * 获取python的版本
     *
     * @return
     */
    public String getPyVersion() {
        Process p = null;
        String pythonVersion = null;
        try {
            p = Runtime.getRuntime().exec(new String[]{
                    "python", "--version"
            });
            try (
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            ) {
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println("---------------");
                while ((line = br2.readLine()) != null) {
                    sb.append(line);
                }
                pythonVersion = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pythonVersion;
    }

}
