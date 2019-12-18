package com.wugui.datax.executor.service.jobhandler;

import cn.hutool.core.io.FileUtil;
import com.wugui.datatx.core.biz.model.HandleProcessCallbackParam;
import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.biz.model.TriggerParam;
import com.wugui.datatx.core.handler.IJobHandler;
import com.wugui.datatx.core.handler.annotation.JobHandler;
import com.wugui.datatx.core.log.JobLogger;
import com.wugui.datatx.core.thread.ProcessCallbackThread;
import com.wugui.datatx.core.util.ProcessUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * DataX任务运行
 *
 * @author jingwk 2019-11-16
 */

@JobHandler(value = "executorJobHandler")
@Component
public class ExecutorJobHandler extends IJobHandler {

    @Value("${datax.executor.jsonpath}")
    private String jsonpath;

    @Value("${datax.pypath}")
    private String dataXPyPath;

    @Override
    public ReturnT<String> executeDataX(TriggerParam tgParam) throws Exception {

        int exitValue = -1;
        BufferedReader bufferedReader = null;
        String tmpFilePath = null;
        String line = null;
        //生成Json临时文件
        tmpFilePath = generateTemJsonFile(tgParam.getJobJson());
        try {
            Map<String,String> params=new HashMap<>();
            params.put("-j","-Xms2G -Xmx2G");
            //params.put("--jvm","-Xms2G"+"\" \""+"-Xmx2G");
            String doc="";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                doc=entry.getKey()+"\""+entry.getValue()+"\"";
                //doc=entry.getKey()+"\" \""+"\""+entry.getValue()+"\"";
            }
            // command process
            System.out.println(doc);
            Process process = Runtime.getRuntime().exec(new String[]{"python",dataXPyPath,doc.replaceAll(" ", "\" \""), tmpFilePath});
            String processId = ProcessUtil.getProcessId(process);
            JobLogger.log("------------------DataX运行进程Id: " + processId);
            jobTmpFiles.put(processId, tmpFilePath);
            //更新任务进程Id
            ProcessCallbackThread.pushCallBack(new HandleProcessCallbackParam(tgParam.getLogId(),tgParam.getLogDateTime(),processId));
            InputStreamReader input = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(input);
            while ((line = bufferedReader.readLine()) != null) {
                JobLogger.log(line);
            }

            InputStreamReader error = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(error);
            while ((line = bufferedReader.readLine()) != null) {
                JobLogger.log(line);
            }
            // command exit
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {
            JobLogger.log(e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
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

    private String generateTemJsonFile(String jobJson) {
        String tmpFilePath;
        if (!FileUtil.exist(jsonpath)) {
            FileUtil.mkdir(jsonpath);
        }
        tmpFilePath = jsonpath + "jobTmp-" + System.currentTimeMillis() + ".conf";
        // 根据json写入到临时本地文件
        try (PrintWriter writer = new PrintWriter(tmpFilePath, "UTF-8")) {
            writer.println(jobJson);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            JobLogger.log("JSON 临时文件写入异常：" + e.getMessage());
        }
        return tmpFilePath;
    }
}
