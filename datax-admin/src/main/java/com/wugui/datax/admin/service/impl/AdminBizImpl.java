package com.wugui.datax.admin.service.impl;

import com.wugui.datatx.core.biz.AdminBiz;
import com.wugui.datatx.core.biz.model.HandleCallbackParam;
import com.wugui.datatx.core.biz.model.HandleProcessCallbackParam;
import com.wugui.datatx.core.biz.model.RegistryParam;
import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.enums.IncrementTypeEnum;
import com.wugui.datatx.core.handler.IJobHandler;
import com.wugui.datax.admin.core.kill.KillJob;
import com.wugui.datax.admin.core.thread.JobTriggerPoolHelper;
import com.wugui.datax.admin.core.trigger.TriggerTypeEnum;
import com.wugui.datax.admin.core.util.I18nUtil;
import com.wugui.datax.admin.entity.JobInfo;
import com.wugui.datax.admin.entity.JobLog;
import com.wugui.datax.admin.mapper.JobInfoMapper;
import com.wugui.datax.admin.mapper.JobLogMapper;
import com.wugui.datax.admin.mapper.JobRegistryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static Logger logger = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    public JobLogMapper jobLogMapper;
    @Resource
    private JobInfoMapper jobInfoMapper;
    @Resource
    private JobRegistryMapper jobRegistryMapper;

    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        for (HandleCallbackParam handleCallbackParam : callbackParamList) {
            ReturnT<String> callbackResult = callback(handleCallbackParam);
            logger.debug(">>>>>>>>> JobApiController.callback {}, handleCallbackParam={}, callbackResult={}",
                    (callbackResult.getCode() == IJobHandler.SUCCESS.getCode() ? "success" : "fail"), handleCallbackParam, callbackResult);
        }

        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> processCallback(List<HandleProcessCallbackParam> callbackParamList) {
        for (HandleProcessCallbackParam handleProcessCallbackParam : callbackParamList) {
            ReturnT<String> callbackResult = processCallback(handleProcessCallbackParam);
            logger.debug(">>>>>>>>> JobApiController.processCallback {}, handleCallbackParam={}, callbackResult={}",
                    (callbackResult.getCode() == IJobHandler.SUCCESS.getCode() ? "success" : "fail"), handleProcessCallbackParam, callbackResult);
        }
        return ReturnT.SUCCESS;
    }

    private ReturnT<String> processCallback(HandleProcessCallbackParam handleProcessCallbackParam) {
        int result = jobLogMapper.updateProcessId(handleProcessCallbackParam.getLogId(), handleProcessCallbackParam.getProcessId());
        return result > 0 ? ReturnT.FAIL : ReturnT.SUCCESS;
    }


    private ReturnT<String> callback(HandleCallbackParam handleCallbackParam) {
        // valid log item
        JobLog log = jobLogMapper.load(handleCallbackParam.getLogId());
        if (log == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "log item not found.");
        }
        if (log.getHandleCode() > 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "log repeate callback.");     // avoid repeat callback, trigger child job etc
        }

        // trigger success, to trigger child job
        String callbackMsg = null;
        int resultCode = handleCallbackParam.getExecuteResult().getCode();

        if (IJobHandler.SUCCESS.getCode() == resultCode) {

            JobInfo jobInfo = jobInfoMapper.loadById(log.getJobId());

            updateIncrementParam(log, jobInfo.getIncrementType());

            if (jobInfo != null && jobInfo.getChildJobId() != null && jobInfo.getChildJobId().trim().length() > 0) {
                callbackMsg = "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>" + I18nUtil.getString("jobconf_trigger_child_run") + "<<<<<<<<<<< </span><br>";

                String[] childJobIds = jobInfo.getChildJobId().split(",");
                for (int i = 0; i < childJobIds.length; i++) {
                    int childJobId = (childJobIds[i] != null && childJobIds[i].trim().length() > 0 && isNumeric(childJobIds[i])) ? Integer.valueOf(childJobIds[i]) : -1;
                    if (childJobId > 0) {

                        JobTriggerPoolHelper.trigger(childJobId, TriggerTypeEnum.PARENT, -1, null, null);
                        ReturnT<String> triggerChildResult = ReturnT.SUCCESS;

                        // add msg
                        callbackMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg1"),
                                (i + 1),
                                childJobIds.length,
                                childJobIds[i],
                                (triggerChildResult.getCode() == ReturnT.SUCCESS_CODE ? I18nUtil.getString("system_success") : I18nUtil.getString("system_fail")),
                                triggerChildResult.getMsg());
                    } else {
                        callbackMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg2"),
                                (i + 1),
                                childJobIds.length,
                                childJobIds[i]);
                    }
                }

            }
        }

        //kill execution timeout DataX process
        if (!StringUtils.isEmpty(log.getProcessId()) && IJobHandler.FAIL_TIMEOUT.getCode() == resultCode) {
            KillJob.trigger(log.getId(), log.getTriggerTime(), log.getExecutorAddress(), log.getProcessId());
        }

        // handle msg
        StringBuffer handleMsg = new StringBuffer();
        if (log.getHandleMsg() != null) {
            handleMsg.append(log.getHandleMsg()).append("<br>");
        }
        if (handleCallbackParam.getExecuteResult().getMsg() != null) {
            handleMsg.append(handleCallbackParam.getExecuteResult().getMsg());
        }
        if (callbackMsg != null) {
            handleMsg.append(callbackMsg);
        }

        if (handleMsg.length() > 15000) {
            handleMsg = new StringBuffer(handleMsg.substring(0, 15000));  // text最大64kb 避免长度过长
        }

        // success, save log
        log.setHandleTime(new Date());
        log.setHandleCode(resultCode);
        log.setHandleMsg(handleMsg.toString());

        jobLogMapper.updateHandleInfo(log);
        jobInfoMapper.updateLastHandleCode(log.getJobId(), resultCode);

        return ReturnT.SUCCESS;
    }

    private void updateIncrementParam(JobLog log, Integer incrementType) {
        if (IncrementTypeEnum.ID.getCode() == incrementType) {
            jobInfoMapper.incrementIdUpdate(log.getJobId(),log.getMaxId());
        } else if (IncrementTypeEnum.TIME.getCode() == incrementType) {
            jobInfoMapper.incrementTimeUpdate(log.getJobId(), log.getTriggerTime());
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {

        // valid
        if (!StringUtils.hasText(registryParam.getRegistryGroup())
                || !StringUtils.hasText(registryParam.getRegistryKey())
                || !StringUtils.hasText(registryParam.getRegistryValue())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "Illegal Argument.");
        }

        int ret = jobRegistryMapper.registryUpdate(registryParam.getRegistryGroup(), registryParam.getRegistryKey(),
                registryParam.getRegistryValue(), registryParam.getCpuUsage(), registryParam.getMemoryUsage(), registryParam.getLoadAverage(), new Date());
        if (ret < 1) {
            jobRegistryMapper.registrySave(registryParam.getRegistryGroup(), registryParam.getRegistryKey(),
                    registryParam.getRegistryValue(), registryParam.getCpuUsage(), registryParam.getMemoryUsage(), registryParam.getLoadAverage(), new Date());

            // fresh
            freshGroupRegistryInfo(registryParam);
        }
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {

        // valid
        if (!StringUtils.hasText(registryParam.getRegistryGroup())
                || !StringUtils.hasText(registryParam.getRegistryKey())
                || !StringUtils.hasText(registryParam.getRegistryValue())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "Illegal Argument.");
        }

        int ret = jobRegistryMapper.registryDelete(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        if (ret > 0) {

            // fresh
            freshGroupRegistryInfo(registryParam);
        }
        return ReturnT.SUCCESS;
    }

    private void freshGroupRegistryInfo(RegistryParam registryParam) {
        // Under consideration, prevent affecting core tables
    }

}
