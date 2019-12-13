package com.wugui.datax.admin.core.kill;

import com.wugui.datatx.core.biz.ExecutorBiz;
import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.biz.model.TriggerParam;
import com.wugui.datatx.core.enums.ExecutorBlockStrategyEnum;
import com.wugui.datax.admin.core.conf.JobAdminConfig;
import com.wugui.datax.admin.core.route.ExecutorRouteStrategyEnum;
import com.wugui.datax.admin.core.scheduler.JobScheduler;
import com.wugui.datax.admin.core.trigger.TriggerTypeEnum;
import com.wugui.datax.admin.core.util.I18nUtil;
import com.wugui.datax.admin.entity.JobGroup;
import com.wugui.datax.admin.entity.JobInfo;
import com.wugui.datax.admin.entity.JobLog;
import com.wugui.datax.rpc.util.IpUtil;
import com.wugui.datax.rpc.util.ThrowableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * datax-job trigger
 * Created by xuxueli on 17/7/13.
 */
public class KillJob {
    private static Logger logger = LoggerFactory.getLogger(KillJob.class);

    /**
     * trigger job
     *
     * @param jobId
     * @param triggerType
     * @param failRetryCount
     * 			>=0: use this param
     * 			<0: use param from job info config
     * @param executorShardingParam
     * @param executorParam
     *          null: use job param
     *          not null: cover job param
     */
    public static void kill(int logId, String address,String processId) {
        //todo
        ReturnT<String> triggerResult = null;
        if (address != null) {
            triggerResult = runExecutor(logId, address,processId,"killJobHandler");
        } else {
            triggerResult = new ReturnT<String>(ReturnT.FAIL_CODE, null);
        }

    }


    /**
     * run executor
     * @param triggerParam
     * @param address
     * @return
     */
    public static ReturnT<String> runExecutor(int logId, String address,String processId,String executorHandler){
        ReturnT<String> runResult = null;
        try {
            ExecutorBiz executorBiz = JobScheduler.getExecutorBiz(address);
            runResult = executorBiz.run(logId,processId,executorHandler);
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> xxl-job trigger error, please check if the executor[{}] is running.", address, e);
            runResult = new ReturnT<String>(ReturnT.FAIL_CODE, ThrowableUtil.toString(e));
        }

        StringBuffer runResultSB = new StringBuffer(I18nUtil.getString("jobconf_trigger_run") + "：");
        runResultSB.append("<br>address：").append(address);
        runResultSB.append("<br>code：").append(runResult.getCode());
        runResultSB.append("<br>msg：").append(runResult.getMsg());

        runResult.setMsg(runResultSB.toString());
        return runResult;
    }

}
