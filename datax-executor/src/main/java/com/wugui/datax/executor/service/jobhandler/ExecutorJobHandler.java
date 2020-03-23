package com.wugui.datax.executor.service.jobhandler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.wugui.datatx.core.biz.model.HandleProcessCallbackParam;
import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.biz.model.TriggerParam;
import com.wugui.datatx.core.handler.IJobHandler;
import com.wugui.datatx.core.handler.annotation.JobHandler;
import com.wugui.datatx.core.log.JobLogger;
import com.wugui.datatx.core.thread.ProcessCallbackThread;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.DateUtil;
import com.wugui.datatx.core.util.ProcessUtil;
import com.wugui.datax.executor.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.wugui.datax.executor.service.jobhandler.DataxOption.DEFAULT_DATAX_PY;

/**
 * DataX任务运行
 *
 * @author jingwk 2019-11-16
 */

@JobHandler(value = "executorJobHandler")
@Component
public class ExecutorJobHandler extends IJobHandler {

    @Value("${datax.executor.jsonpath}")
    private String jsonPath;

    @Value("${datax.pypath}")
    private String dataXPyPath;

    @Override
    public ReturnT<String> execute(TriggerParam trigger) {
        int exitValue = -1;
        Thread inputThread = null;
        Thread errThread = null;
        String tmpFilePath;
        //生成Json临时文件
        tmpFilePath = generateTemJsonFile(trigger.getJobJson());
        try {
            String[] cmdarrayFinal = buildCmd(trigger, tmpFilePath);
            final Process process = Runtime.getRuntime().exec(cmdarrayFinal);
            String prcsId = ProcessUtil.getProcessId(process);
            JobLogger.log("------------------DataX运行进程Id: " + prcsId);
            jobTmpFiles.put(prcsId, tmpFilePath);
            //更新任务进程Id
            HandleProcessCallbackParam prcs = new HandleProcessCallbackParam(trigger.getLogId(), trigger.getLogDateTime(), prcsId);
            ProcessCallbackThread.pushCallBack(prcs);
            // log-thread
            inputThread = new Thread(() -> {
                try {
                    reader(new BufferedInputStream(process.getInputStream()));
                } catch (IOException e) {
                    JobLogger.log(e);
                }
            });
            errThread = new Thread(() -> {
                try {
                    reader(new BufferedInputStream(process.getErrorStream()));
                } catch (IOException e) {
                    JobLogger.log(e);
                }
            });
            inputThread.start();
            errThread.start();
            // process-wait
            exitValue = process.waitFor();      // exit code: 0=success, 1=error
            // log-thread join
            inputThread.join();
            errThread.join();
        } catch (Exception e) {
            JobLogger.log(e);
        } finally {
            if (inputThread != null && inputThread.isAlive()) {
                inputThread.interrupt();
            }
            if (errThread != null && errThread.isAlive()) {
                errThread.interrupt();
            }
            //  删除临时文件
            if (FileUtil.exist(tmpFilePath)) {
                FileUtil.del(new File(tmpFilePath));
            }
        }
        if (exitValue == 0) {
            return IJobHandler.SUCCESS;
        } else {
            return new ReturnT<>(IJobHandler.FAIL.getCode(), "command exit value(" + exitValue + ") is failed");
        }
    }

    private String[] buildCmd(TriggerParam tgParam, String tmpFilePath) {
        // command process
        //"--loglevel=debug"
        List<String> cmdArr = new ArrayList<>();
        cmdArr.add("python");
        String dataXHomePath = SystemUtils.getDataXHomePath();
        if (StringUtils.isNotEmpty(dataXHomePath)) {
            dataXPyPath = dataXHomePath.contains("bin") ? dataXHomePath + DEFAULT_DATAX_PY : dataXHomePath + "bin" + File.separator + DEFAULT_DATAX_PY;
        }
        cmdArr.add(dataXPyPath);
        String doc = buildDataXParam(tgParam);
        if (StringUtils.isNotBlank(doc)) {
            cmdArr.add(doc.replaceAll(DataxOption.SPLIT_SPACE, DataxOption.TRANSFORM_SPLIT_SPACE));
        }
        cmdArr.add(tmpFilePath);
        return cmdArr.toArray(new String[cmdArr.size()]);
    }

    /**
     * 数据流reader（Input自动关闭，Output不处理）
     *
     * @param inputStream
     * @throws IOException
     */
    private static void reader(InputStream inputStream) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                JobLogger.log(line);
            }
            reader.close();
            inputStream = null;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private String buildDataXParam(TriggerParam tgParam) {
        StringBuilder doc = new StringBuilder();
        String jvmParam = tgParam.getJvmParam();
        String partitionStr = tgParam.getPartitionInfo();
        if (StringUtils.isNotBlank(jvmParam)) {
            doc.append(DataxOption.JVM_CM).append(DataxOption.TRANSFORM_QUOTES).append(jvmParam).append(DataxOption.TRANSFORM_QUOTES);
        }
        long tgSecondTime = tgParam.getTriggerTime().getTime() / 1000;
        if (StringUtils.isNotBlank(tgParam.getReplaceParam())) {
            long lastTime = tgParam.getStartTime().getTime() / 1000;
            if (doc.length() > 0) doc.append(DataxOption.SPLIT_SPACE);
            doc.append(DataxOption.PARAMS_CM).append(DataxOption.TRANSFORM_QUOTES).append(String.format(tgParam.getReplaceParam(), lastTime, tgSecondTime));
            if (StringUtils.isNotBlank(partitionStr)) {
                doc.append(DataxOption.SPLIT_SPACE);
                List<String> partitionInfo = Arrays.asList(partitionStr.split(Constants.SPLIT_COMMA));
                doc.append(String.format(DataxOption.PARAMS_CM_V_PT, buildPartition(partitionInfo)));
            }
            doc.append(DataxOption.TRANSFORM_QUOTES);
        } else {
            if (StringUtils.isNotBlank(partitionStr)) {
                List<String> partitionInfo = Arrays.asList(partitionStr.split(Constants.SPLIT_COMMA));
                if (doc.length() > 0) doc.append(DataxOption.SPLIT_SPACE);
                doc.append(DataxOption.PARAMS_CM).append(DataxOption.TRANSFORM_QUOTES).append(String.format(DataxOption.PARAMS_CM_V_PT, buildPartition(partitionInfo))).append(DataxOption.TRANSFORM_QUOTES);
            }
        }
        JobLogger.log("------------------命令参数: " + doc);
        return doc.toString();
    }

    private String buildPartition(List<String> partitionInfo) {
        String field = partitionInfo.get(0);
        int timeOffset = Integer.parseInt(partitionInfo.get(1));
        String timeFormat = partitionInfo.get(2);
        String partitionTime = DateUtil.format(DateUtil.addDays(new Date(), timeOffset), timeFormat);
        return field + Constants.EQUAL + partitionTime;
    }

    private String generateTemJsonFile(String jobJson) {
        String tmpFilePath;
        String dataXHomePath = SystemUtils.getDataXHomePath();
        if (StringUtils.isNotEmpty(dataXHomePath)) {
            jsonPath = dataXHomePath + DataxOption.DEFAULT_JSON;
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
