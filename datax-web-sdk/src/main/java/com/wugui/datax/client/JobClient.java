package com.wugui.datax.client;

import cn.hutool.json.JSONObject;
import com.wugui.datax.client.enums.GlueTypeEnum;
import com.wugui.datax.client.model.ReturnT;
import com.wugui.datax.client.model.JobInfo;
import com.wugui.datax.client.model.JobLog;
import com.wugui.datax.client.util.AppUtil;
import com.wugui.datax.client.util.HttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * datax-web sdk调用客户端
 *
 * @author Locki
 * @date 2020/9/23
 */
public class JobClient {
    private ResourceBundle rb = ResourceBundle.getBundle("sdk-config");
    private String accessKey = rb.getString("datax.web.admin.accessKey");
    private String secretKey = rb.getString("datax.web.admin.secretKey");
    private String webUrl = rb.getString("datax.web.admin.url");
    private String add = rb.getString("datax.web.admin.api.add");
    private String load = rb.getString("datax.web.admin.api.loadJob");
    private String update = rb.getString("datax.web.admin.api.update");
    private String delete = rb.getString("datax.web.admin.api.delete");
    private String start = rb.getString("datax.web.admin.api.start");
    private String stop = rb.getString("datax.web.admin.api.stop");
    private String log = rb.getString("datax.web.admin.api.log");

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    /**
     * API调用返回值封装
     *
     * @param result
     * @return
     */
    private ReturnT returns(String result) {
        ReturnT returnT = null;
        JSONObject object = new JSONObject(result);
        if ("200".equals(object.getStr("code"))) {
            returnT = new ReturnT(true, object.getStr("msg"), object.getStr("content"));
        } else {
            returnT = new ReturnT(false, object.getStr("msg"), object.getStr("content"));
        }
        return returnT;
    }

    /**
     * 添加任务
     *
     * @param jobInfo
     * @return
     */
    public ReturnT createJob(JobInfo jobInfo) {
        String url = webUrl + add;
        ReturnT returnT = null;
        try {
            Map<String, String> paramMap = jobToMap(jobInfo);
            String result = new HttpUtil().postJson(url, addSign(paramMap));
            returnT = returns(result);
        } catch (Exception e) {
            e.printStackTrace();
            returnT = new ReturnT(false, e.getMessage(), null);
        }
        return returnT;
    }

    /**
     * 修改任务
     *
     * @param jobInfo
     * @return
     */
    public ReturnT updateJob(JobInfo jobInfo) {
        String url = webUrl + update;
        ReturnT returnT = null;
        try {
            Map<String, String> paramMap = jobToMap(jobInfo);
            String result = new HttpUtil().postJson(url, addSign(paramMap));
            returnT = returns(result);
        } catch (Exception e) {
            e.printStackTrace();
            returnT = new ReturnT(false, e.getMessage(), null);
        }
        return returnT;
    }

    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    public ReturnT deleteJob(String id) {
        String url = webUrl + delete + "/" + id;
        ReturnT returnT = null;
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("id", id);
            String result = new HttpUtil().postJson(url, addSign(paramMap));
            returnT = returns(result);
        } catch (Exception e) {
            e.printStackTrace();
            returnT = new ReturnT(false, e.getMessage(), null);
        }
        return returnT;
    }

    /**
     * 查询任务
     *
     * @param id
     * @return
     * @throws Exception
     */
    public JobInfo getJob(String id) {
        String url = webUrl + load + "?id=" + id;
        JobInfo jobInfo = null;
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("id", id);
            String result = new HttpUtil().postJson(url, addSign(paramMap));
            JSONObject object = new JSONObject(result);
            jobInfo = object.toBean(JobInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobInfo;
    }

    /**
     * 停用任务
     *
     * @param id
     * @return
     */
    public ReturnT stopJob(String id) {
        String url = webUrl + stop + "?id=" + id;
        ReturnT returnT = null;
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("id", id);
            String result = new HttpUtil().postJson(url, addSign(paramMap));
            returnT = returns(result);
        } catch (Exception e) {
            e.printStackTrace();
            returnT = new ReturnT(false, e.getMessage(), null);
        }
        return returnT;
    }

    /**
     * 启用任务
     *
     * @param id
     * @return
     */
    public ReturnT startJob(String id) {
        String url = webUrl + start + "?id=" + id;
        ReturnT returnT = null;
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("id", id);
            String result = new HttpUtil().postJson(url, addSign(paramMap));
            returnT = returns(result);
        } catch (Exception e) {
            e.printStackTrace();
            returnT = new ReturnT(false, e.getMessage(), null);
        }
        return returnT;
    }

    /**
     * 任务执行日志
     *
     * @param id
     * @return
     */
    public List<JobLog> jobLog(String id) {
        String url = webUrl + log;
        List<JobLog> logs = null;
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("jobId", id);
            paramMap.put("jobGroup", "0");
            paramMap.put("logStatus", "-1");
            String result = new HttpUtil().get(url, addSign(paramMap));
            JSONObject object = new JSONObject(result);
            logs = object.getJSONObject("content").getJSONArray("data").toList(JobLog.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * 将任务参数转换为Map<String, String>作为http请求参数
     *
     * @param jobInfo
     * @return
     */
    private Map<String, String> jobToMap(JobInfo jobInfo) {
        Map<String, String> map = new HashMap<>();
        if (jobInfo.getId() > 0) {
            map.put("id", String.valueOf(jobInfo.getId()));
        }
        map.put("jobGroup", String.valueOf(jobInfo.getJobGroup()));
        map.put("jobCron", jobInfo.getJobCron());
        map.put("jobDesc", jobInfo.getJobDesc());
        map.put("projectId", jobInfo.getProjectId());
        map.put("author", jobInfo.getAuthor());
        map.put("alarmEmail", jobInfo.getAlarmEmail());
        map.put("executorRouteStrategy", String.valueOf(jobInfo.getExecutorRouteStrategy()));
        map.put("executorHandler", jobInfo.getExecutorHandler());
        map.put("executorBlockStrategy", String.valueOf(jobInfo.getExecutorBlockStrategy()));
        map.put("executorTimeout", String.valueOf(jobInfo.getExecutorTimeout()));
        map.put("executorFailRetryCount", String.valueOf(jobInfo.getExecutorFailRetryCount()));
        //通过SDK只能添加JAVA_BEAN类型
        map.put("glueType", GlueTypeEnum.JAVA_BEAN.name());
        map.put("executorParam", jobInfo.getExecutorParam());
        map.put("jobJson", jobInfo.getExecutorParam());
        return map;
    }

    /**
     * 签名
     *
     * @param params
     * @return {@link Map< String, String>}
     * @author jiangyang
     * @date 2020/10/15
     */
    private Map<String, String> addSign(Map<String, String> params) {
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("accessKey", this.accessKey);
        String sign = AppUtil.sign(params, this.secretKey);
        params.put("sign", sign);
        return params;
    }
}
