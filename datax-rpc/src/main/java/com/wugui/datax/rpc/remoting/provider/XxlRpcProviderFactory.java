package com.wugui.datax.rpc.remoting.provider;

import com.wugui.datax.rpc.registry.AbstractServiceRegistry;
import com.wugui.datax.rpc.remoting.net.AbstractServer;
import com.wugui.datax.rpc.remoting.net.impl.netty.server.NettyServer;
import com.wugui.datax.rpc.remoting.net.params.BaseCallback;
import com.wugui.datax.rpc.remoting.net.params.XxlRpcRequest;
import com.wugui.datax.rpc.remoting.net.params.XxlRpcResponse;
import com.wugui.datax.rpc.serialize.AbstractSerializer;
import com.wugui.datax.rpc.serialize.impl.HessianSerializer;
import com.wugui.datax.rpc.util.IpUtil;
import com.wugui.datax.rpc.util.NetUtil;
import com.wugui.datax.rpc.util.ThrowableUtil;
import com.wugui.datax.rpc.util.XxlRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * provider
 *
 * @author xuxueli 2015-10-31 22:54:27
 */
public class XxlRpcProviderFactory {
    private static final Logger logger = LoggerFactory.getLogger(XxlRpcProviderFactory.class);

    // ---------------------- config ----------------------

    private Class<? extends AbstractServer> server = NettyServer.class;
    private Class<? extends AbstractSerializer> serializer = HessianSerializer.class;

    private int corePoolSize = 60;
    private int maxPoolSize = 300;

    private String ip = null;
    private int port = 7080;
    private String accessToken = null;

    private Class<? extends AbstractServiceRegistry> serviceRegistry = null;
    private Map<String, String> serviceRegistryParam = null;

    // set
    public void setServer(Class<? extends AbstractServer> server) {
        this.server = server;
    }

    public void setSerializer(Class<? extends AbstractSerializer> serializer) {
        this.serializer = serializer;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
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

    public void setServiceRegistry(Class<? extends AbstractServiceRegistry> serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public void setServiceRegistryParam(Map<String, String> serviceRegistryParam) {
        this.serviceRegistryParam = serviceRegistryParam;
    }

    // get
    public AbstractSerializer getSerializerInstance() {
        return serializerInstance;
    }

    public int getPort() {
        return port;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    // ---------------------- start / stop ----------------------

    private AbstractServer serverInstance;
    private AbstractSerializer serializerInstance;
    private AbstractServiceRegistry serviceRegistryInstance;
    private String serviceAddress;

    public void start() throws Exception {

        // valid
        if (this.server == null) {
            throw new XxlRpcException("xxl-rpc provider server missing.");
        }
        if (this.serializer == null) {
            throw new XxlRpcException("xxl-rpc provider serializer missing.");
        }
        if (!(this.corePoolSize > 0 && this.maxPoolSize > 0 && this.maxPoolSize >= this.corePoolSize)) {
            this.corePoolSize = 60;
            this.maxPoolSize = 300;
        }
        if (this.ip == null) {
            this.ip = IpUtil.getIp();
        }
        if (this.port <= 0) {
            this.port = 7080;
        }
        if (NetUtil.isPortUsed(this.port)) {
            throw new XxlRpcException("xxl-rpc provider port[" + this.port + "] is used.");
        }

        // init serializerInstance
        this.serializerInstance = serializer.newInstance();

        // start server
        serviceAddress = IpUtil.getIpPort(this.ip, port);
        serverInstance = server.newInstance();
        serverInstance.setStartedCallback(new BaseCallback() {
            @Override
            public void run() throws Exception {
                // start registry
                if (serviceRegistry != null) {
                    serviceRegistryInstance = serviceRegistry.newInstance();
                    serviceRegistryInstance.start(serviceRegistryParam);
                    if (serviceData.size() > 0) {
                        serviceRegistryInstance.registry(serviceData.keySet(), serviceAddress);
                    }
                }
            }
        });
        // serviceRegistry stoped
        serverInstance.setStopedCallback(new BaseCallback() {
            @Override
            public void run() {
                // stop registry
                if (serviceRegistryInstance != null) {
                    if (serviceData.size() > 0) {
                        serviceRegistryInstance.remove(serviceData.keySet(), serviceAddress);
                    }
                    serviceRegistryInstance.stop();
                    serviceRegistryInstance = null;
                }
            }
        });
        serverInstance.start(this);
    }

    public void stop() throws Exception {
        serverInstance.stop();
    }


    // ---------------------- server invoke ----------------------

    /**
     * init local rpc service map
     */
    private final Map<String, Object> serviceData = new HashMap<>();

    public Map<String, Object> getServiceData() {
        return serviceData;
    }

    /**
     * make service key
     *
     * @param iFace   String
     * @param version String
     * @return String
     */
    public static String makeServiceKey(String iFace, String version) {
        String serviceKey = iFace;
        if (version != null && version.trim().length() > 0) {
            serviceKey += "#".concat(version);
        }
        return serviceKey;
    }

    /**
     * add service
     *
     * @param iface       String
     * @param version     String
     * @param serviceBean Object
     */
    public void addService(String iface, String version, Object serviceBean) {
        String serviceKey = makeServiceKey(iface, version);
        serviceData.put(serviceKey, serviceBean);

        logger.info(">>>>>>>>>>> xxl-rpc, provider factory add service success. serviceKey = {}, serviceBean = {}", serviceKey, serviceBean.getClass());
    }

    /**
     * invoke service
     *
     * @param xxlRpcRequest XxlRpcRequest
     * @return XxlRpcResponse
     */
    public XxlRpcResponse invokeService(XxlRpcRequest xxlRpcRequest) throws Throwable {

        //  make response
        XxlRpcResponse xxlRpcResponse = new XxlRpcResponse();
        xxlRpcResponse.setRequestId(xxlRpcRequest.getRequestId());

        // match service bean
        String serviceKey = makeServiceKey(xxlRpcRequest.getClassName(), xxlRpcRequest.getVersion());
        Object serviceBean = serviceData.get(serviceKey);

        // valid
        if (serviceBean == null) {
            xxlRpcResponse.setErrorMsg("The serviceKey[" + serviceKey + "] not found.");
            return xxlRpcResponse;
        }

        if (System.currentTimeMillis() - xxlRpcRequest.getCreateMillisTime() > 3 * 60 * 1000) {
            xxlRpcResponse.setErrorMsg("The timestamp difference between admin and executor exceeds the limit.");
            return xxlRpcResponse;
        }
        if (accessToken != null && accessToken.trim().length() > 0 && !accessToken.trim().equals(xxlRpcRequest.getAccessToken())) {
            xxlRpcResponse.setErrorMsg("The access token[" + xxlRpcRequest.getAccessToken() + "] is wrong.");
            return xxlRpcResponse;
        }

        // invoke
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = xxlRpcRequest.getMethodName();
        Class<?>[] parameterTypes = xxlRpcRequest.getParameterTypes();
        Object[] parameters = xxlRpcRequest.getParameters();

        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        Object result = method.invoke(serviceBean, parameters);
        xxlRpcResponse.setResult(result);
        return xxlRpcResponse;
    }

}
