package com.wugui.datax.impl;

import com.wugui.datatx.core.biz.ExecutorBiz;
import com.wugui.datatx.core.biz.model.LogResult;
import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.biz.model.TriggerParam;
import com.wugui.datatx.core.enums.ExecutorBlockStrategyEnum;
import com.wugui.datatx.core.executor.JobExecutor;
import com.wugui.datatx.core.glue.GlueTypeEnum;
import com.wugui.datax.rpc.remoting.invoker.call.CallType;
import com.wugui.datax.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.wugui.datax.rpc.remoting.invoker.route.LoadBalance;
import com.wugui.datax.rpc.remoting.net.impl.netty_http.client.NettyHttpClient;
import com.wugui.datax.rpc.serialize.impl.HessianSerializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;


public class ExecutorBizImplTest {

    public JobExecutor jobExecutor = null;
    public ExecutorBiz executorBiz = null;

    @Before
    public void before() throws Exception {

        // init executor
        jobExecutor = new JobExecutor();
        jobExecutor.setAdminAddresses(null);
        jobExecutor.setAppName("datax-executor");
        jobExecutor.setIp(null);
        jobExecutor.setPort(9999);
        jobExecutor.setAccessToken(null);
        jobExecutor.setLogPath("/data/applogs/executor/jobhandler");
        jobExecutor.setLogRetentionDays(-1);

        // start executor
        jobExecutor.start();

        TimeUnit.SECONDS.sleep(3);

        // init executor biz proxy
        XxlRpcReferenceBean referenceBean = new XxlRpcReferenceBean();
        referenceBean.setClient(NettyHttpClient.class);
        referenceBean.setSerializer(HessianSerializer.class);
        referenceBean.setCallType(CallType.SYNC);
        referenceBean.setLoadBalance(LoadBalance.ROUND);
        referenceBean.setIface(ExecutorBiz.class);
        referenceBean.setVersion(null);
        referenceBean.setTimeout(3000);
        referenceBean.setAddress("127.0.0.1:9999");
        referenceBean.setAccessToken(null);
        referenceBean.setInvokeCallback(null);
        referenceBean.setInvokerFactory(null);

        executorBiz = (ExecutorBiz) referenceBean.getObject();
    }

    @After
    public void after(){
        if (jobExecutor != null) {
            jobExecutor.destroy();
        }
    }


    @Test
    public void beat() {
        // Act
        final ReturnT<String> retval = executorBiz.beat();

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(((ReturnT<String>) retval).getContent());
        Assert.assertEquals(200, retval.getCode());
        Assert.assertNull(retval.getMsg());
    }

    @Test
    public void idleBeat(){
        final int jobId = 0;

        // Act
        final ReturnT<String> retval = executorBiz.idleBeat(jobId);

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(((ReturnT<String>) retval).getContent());
        Assert.assertEquals(500, retval.getCode());
        Assert.assertEquals("job thread is running or has trigger queue.", retval.getMsg());
    }

    @Test
    public void kill(){
        final int jobId = 0;

        // Act
        final ReturnT<String> retval = executorBiz.kill(jobId);

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(((ReturnT<String>) retval).getContent());
        Assert.assertEquals(200, retval.getCode());
        Assert.assertNull(retval.getMsg());
    }

    @Test
    public void log(){
        final long logDateTim = 0L;
        final long logId = 0;
        final int fromLineNum = 0;

        // Act
        final ReturnT<LogResult> retval = executorBiz.log(logDateTim, logId, fromLineNum);

        // Assert result
        Assert.assertNotNull(retval);
    }

    @Test
    public void run(){
        // trigger data
        final TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(1);
        triggerParam.setExecutorHandler("demoJobHandler");
        triggerParam.setExecutorParams(null);
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.COVER_EARLY.name());
        triggerParam.setGlueType(GlueTypeEnum.BEAN.name());
        triggerParam.setGlueSource(null);
        triggerParam.setGlueUpdatetime(System.currentTimeMillis());
        triggerParam.setLogId(1);
        triggerParam.setLogDateTime(System.currentTimeMillis());

        // Act
        final ReturnT<String> retval = executorBiz.run(triggerParam);

        // Assert result
        Assert.assertNotNull(retval);
    }

}
