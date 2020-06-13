package com.wugui.datatx.core.executor;

import com.wugui.datatx.core.biz.AdminBiz;
import com.wugui.datatx.core.biz.ExecutorBiz;
import com.wugui.datatx.core.biz.client.AdminBizClient;
import com.wugui.datatx.core.biz.impl.ExecutorBizImpl;
import com.wugui.datatx.core.handler.IJobHandler;
import com.wugui.datatx.core.log.JobFileAppender;
import com.wugui.datatx.core.thread.*;
import com.wugui.datax.rpc.registry.ServiceRegistry;
import com.wugui.datax.rpc.remoting.net.impl.netty_http.server.NettyHttpServer;
import com.wugui.datax.rpc.remoting.provider.XxlRpcProviderFactory;
import com.wugui.datax.rpc.serialize.Serializer;
import com.wugui.datax.rpc.serialize.impl.HessianSerializer;
import com.wugui.datax.rpc.util.IpUtil;
import com.wugui.datax.rpc.util.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xuxueli on 2016/3/2 21:14.
 */
public class JobExecutor {
    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    // ---------------------- param ----------------------
    private String adminAddresses;
    private String appName;
    private String ip;
    private int port;
    private String accessToken;
    private String logPath;
    private int logRetentionDays;

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public void setLogRetentionDays(int logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }


    // ---------------------- start + stop ----------------------
    public void start() throws Exception {

        // init logpath
        JobFileAppender.initLogPath(logPath);

        // init invoker, admin-client
        initAdminBizList(adminAddresses, accessToken);


        // init JobLogFileCleanThread
        JobLogFileCleanThread.getInstance().start(logRetentionDays);

        // init TriggerCallbackThread
        TriggerCallbackThread.getInstance().start();

        // init ProcessCallbackThread
        ProcessCallbackThread.getInstance().start();

        // init executor-server
        port = port > 0 ? port : NetUtil.findAvailablePort(9999);
        ip = (ip != null && ip.trim().length() > 0) ? ip : IpUtil.getIp();
        initRpcProvider(ip, port, appName, accessToken);
    }

    public void destroy() {

        // destory executor-server
        stopRpcProvider();

        // destory jobThreadRepository
        if (jobThreadRepository.size() > 0) {
            for (Map.Entry<Integer, JobThread> item : jobThreadRepository.entrySet()) {
                removeJobThread(item.getKey(), "web container destroy and kill the job.");
                JobThread oldJobThread = removeJobThread(item.getKey(), "web container destroy and kill the job.");
                // wait for job thread push result to callback queue
                if (oldJobThread != null) {
                    try {
                        oldJobThread.join();
                    } catch (InterruptedException e) {
                        logger.error(">>>>>>>>>>> datax-web, JobThread destroy(join) error, jobId:{}", item.getKey(), e);
                    }
                }
            }
            jobThreadRepository.clear();
        }
        jobHandlerRepository.clear();

        // destory JobLogFileCleanThread
        JobLogFileCleanThread.getInstance().toStop();

        // destory TriggerCallbackThread
        TriggerCallbackThread.getInstance().toStop();

        // destory ProcessCallbackThread
        ProcessCallbackThread.getInstance().toStop();

    }


    // ---------------------- admin-client (rpc invoker) ----------------------
    private static List<AdminBiz> adminBizList;
    private static Serializer serializer = new HessianSerializer();

    private void initAdminBizList(String adminAddresses, String accessToken) throws Exception {
        if (adminAddresses != null && adminAddresses.trim().length() > 0) {
            for (String address : adminAddresses.trim().split(",")) {
                if (address != null && address.trim().length() > 0) {
                    //实例化AdminBizClient
                    AdminBiz adminBiz = new AdminBizClient(address.trim(), accessToken);

                    if (adminBizList == null) {
                        adminBizList = new ArrayList<>();
                    }
                    adminBizList.add(adminBiz);
                }
            }
        }
    }

    public static List<AdminBiz> getAdminBizList() {
        return adminBizList;
    }

    public static Serializer getSerializer() {
        return serializer;
    }


    // ---------------------- executor-server (rpc provider) ----------------------
    private XxlRpcProviderFactory xxlRpcProviderFactory = null;

    private void initRpcProvider(String ip, int port, String appName, String accessToken) throws Exception {

        // init, provider factory
        String address = IpUtil.getIpPort(ip, port);
        Map<String, String> serviceRegistryParam = new HashMap<>();
        serviceRegistryParam.put("appName", appName);
        serviceRegistryParam.put("address", address);

        xxlRpcProviderFactory = new XxlRpcProviderFactory();

        xxlRpcProviderFactory.setServer(NettyHttpServer.class);
        xxlRpcProviderFactory.setSerializer(HessianSerializer.class);
        xxlRpcProviderFactory.setCorePoolSize(20);
        xxlRpcProviderFactory.setMaxPoolSize(200);
        xxlRpcProviderFactory.setIp(ip);
        xxlRpcProviderFactory.setPort(port);
        xxlRpcProviderFactory.setAccessToken(accessToken);
        xxlRpcProviderFactory.setServiceRegistry(ExecutorServiceRegistry.class);
        xxlRpcProviderFactory.setServiceRegistryParam(serviceRegistryParam);

        // add services
        xxlRpcProviderFactory.addService(ExecutorBiz.class.getName(), null, new ExecutorBizImpl());

        // start
        xxlRpcProviderFactory.start();

    }

    public static class ExecutorServiceRegistry extends ServiceRegistry {

        @Override
        public void start(Map<String, String> param) {
            // start registry
            ExecutorRegistryThread.getInstance().start(param.get("appName"), param.get("address"));
        }

        @Override
        public void stop() {
            // stop registry
            ExecutorRegistryThread.getInstance().toStop();
        }

        @Override
        public boolean registry(Set<String> keys, String value) {
            return false;
        }

        @Override
        public boolean remove(Set<String> keys, String value) {
            return false;
        }

        @Override
        public Map<String, TreeSet<String>> discovery(Set<String> keys) {
            return null;
        }

        @Override
        public TreeSet<String> discovery(String key) {
            return null;
        }

    }

    private void stopRpcProvider() {
        // stop provider factory
        try {
            xxlRpcProviderFactory.stop();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    // ---------------------- job handler repository ----------------------
    private static ConcurrentMap<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<String, IJobHandler>();

    public static IJobHandler registJobHandler(String name, IJobHandler jobHandler) {
        logger.info(">>>>>>>>>>> datax-web register jobhandler success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }

    public static IJobHandler loadJobHandler(String name) {
        return jobHandlerRepository.get(name);
    }


    // ---------------------- job thread repository ----------------------
    private static ConcurrentMap<Integer, JobThread> jobThreadRepository = new ConcurrentHashMap<Integer, JobThread>();

    public static JobThread registJobThread(int jobId, IJobHandler handler, String removeOldReason) {
        JobThread newJobThread = new JobThread(jobId, handler);
        newJobThread.start();
        logger.info(">>>>>>>>>>> datax-web regist JobThread success, jobId:{}, handler:{}", new Object[]{jobId, handler});

        JobThread oldJobThread = jobThreadRepository.put(jobId, newJobThread);    // putIfAbsent | oh my god, map's put method return the old value!!!
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }

        return newJobThread;
    }

    public static JobThread removeJobThread(int jobId, String removeOldReason) {
        JobThread oldJobThread = jobThreadRepository.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
            return oldJobThread;
        }
        return null;
    }

    public static JobThread loadJobThread(int jobId) {
        JobThread jobThread = jobThreadRepository.get(jobId);
        return jobThread;
    }

}
