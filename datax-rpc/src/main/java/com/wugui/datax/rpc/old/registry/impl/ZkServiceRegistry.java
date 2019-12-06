package com.wugui.datax.rpc.old.registry.impl;//package com.xxl.rpc.registry.impl;
//
//import com.xxl.rpc.registry.ServiceRegistry;
//import com.xxl.rpc.util.XxlRpcException;
//import com.xxl.rpc.util.XxlZkClient;
//import org.apache.zookeeper.WatchedEvent;
//import org.apache.zookeeper.Watcher;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.TimeUnit;
//
///**
// * service registry for "zookeeper"
// *
// *  /xxl-rpc/dev/
// *              - key01(service01)
// *                  - value01 (ip:port01)
// *                  - value02 (ip:port02)
// *
// *
// *
// *        <!-- zookeeper (provided) -->
// *        <dependency>
// *            <groupId>org.apache.zookeeper</groupId>
// *            <artifactId>zookeeper</artifactId>
// *            <version>${zookeeper.version}</version>
// *            <scope>provided</scope>
// *        </dependency>
// *
// *
// * @author xuxueli 2018-10-17
// */
//public class ZkServiceRegistry extends ServiceRegistry {
//    private static Logger logger = LoggerFactory.getLogger(ZkServiceRegistry.class);
//
//    // param
//    public static final String ENV = "env";                       // zk env
//    public static final String ZK_ADDRESS = "zkaddress";        // zk registry address, like "ip1:port,ip2:port,ip3:port"
//    public static final String ZK_DIGEST = "zkdigest";          // zk registry digest
//
//
//    // ------------------------------ zk conf ------------------------------
//
//    // config
//    private static final String zkBasePath = "/xxl-rpc";
//    private String zkEnvPath;
//    private XxlZkClient xxlZkClient = null;
//
//    private Thread refreshThread;
//    private volatile boolean refreshThreadStop = false;
//
//    private volatile ConcurrentMap<String, TreeSet<String>> registryData = new ConcurrentHashMap<String, TreeSet<String>>();
//    private volatile ConcurrentMap<String, TreeSet<String>> discoveryData = new ConcurrentHashMap<String, TreeSet<String>>();
//
//
//    /**
//     * key 2 path
//     * @param   nodeKey
//     * @return  znodePath
//     */
//    public String keyToPath(String nodeKey){
//        return zkEnvPath + "/" + nodeKey;
//    }
//
//    /**
//     * path 2 key
//     * @param   nodePath
//     * @return  nodeKey
//     */
//    public String pathToKey(String nodePath){
//        if (nodePath==null || nodePath.length() <= zkEnvPath.length() || !nodePath.startsWith(zkEnvPath)) {
//            return null;
//        }
//        return nodePath.substring(zkEnvPath.length()+1, nodePath.length());
//    }
//
//    // ------------------------------ util ------------------------------
//
//    /**
//     * @param param
//     *      Environment.ZK_ADDRESS  ：zk address
//     *      Environment.ZK_DIGEST   ：zk didest
//     *      Environment.ENV         ：env
//     */
//    @Override
//    public void start(Map<String, String> param) {
//        String zkaddress = param.get(ZK_ADDRESS);
//        String zkdigest = param.get(ZK_DIGEST);
//        String env = param.get(ENV);
//
//        // valid
//        if (zkaddress==null || zkaddress.trim().length()==0) {
//            throw new XxlRpcException("xxl-rpc zkaddress can not be empty");
//        }
//
//        // init zkpath
//        if (env==null || env.trim().length()==0) {
//            throw new XxlRpcException("xxl-rpc env can not be empty");
//        }
//
//        zkEnvPath = zkBasePath.concat("/").concat(env);
//
//        // init
//        xxlZkClient = new XxlZkClient(zkaddress, zkEnvPath, zkdigest, new Watcher() {
//            @Override
//            public void process(WatchedEvent watchedEvent) {
//                try {
//                    logger.debug(">>>>>>>>>>> xxl-rpc: watcher:{}", watchedEvent);
//
//                    // session expire, close old and create new
//                    if (watchedEvent.getState() == Event.KeeperState.Expired) {
//                        xxlZkClient.destroy();
//                        xxlZkClient.getClient();
//
//                        // refreshDiscoveryData (all)：expire retry
//                        refreshDiscoveryData(null);
//
//                        logger.info(">>>>>>>>>>> xxl-rpc, zk re-connect reloadAll success.");
//                    }
//
//                    // watch + refresh
//                    String path = watchedEvent.getPath();
//                    String key = pathToKey(path);
//                    if (key != null) {
//                        // keep watch conf key：add One-time trigger
//                        xxlZkClient.getClient().exists(path, true);
//
//                        // refresh
//                        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
//                            // refreshDiscoveryData (one)：one change
//                            refreshDiscoveryData(key);
//                        } else if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
//                            logger.info("reload all 111");
//                        }
//                    }
//
//                } catch (Exception e) {
//                    logger.error(e.getMessage(), e);
//                }
//            }
//        });
//
//        // init client      // TODO, support init without conn, and can use mirror data
//        xxlZkClient.getClient();
//
//
//        // refresh thread
//        refreshThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (!refreshThreadStop) {
//                    try {
//                        TimeUnit.SECONDS.sleep(60);
//
//                        // refreshDiscoveryData (all)：cycle check
//                        refreshDiscoveryData(null);
//
//                        // refresh RegistryData
//                        refreshRegistryData();
//                    } catch (Exception e) {
//                        if (!refreshThreadStop) {
//                            logger.error(">>>>>>>>>>> xxl-rpc, refresh thread error.", e);
//                        }
//                    }
//                }
//                logger.info(">>>>>>>>>>> xxl-rpc, refresh thread stoped.");
//            }
//        });
//        refreshThread.setName("xxl-rpc, ZkServiceRegistry refresh thread.");
//        refreshThread.setDaemon(true);
//        refreshThread.start();
//
//        logger.info(">>>>>>>>>>> xxl-rpc, ZkServiceRegistry init success. [env={}]", env);
//    }
//
//    @Override
//    public void stop() {
//        if (xxlZkClient!=null) {
//            xxlZkClient.destroy();
//        }
//        if (refreshThread != null) {
//            refreshThreadStop = true;
//            refreshThread.interrupt();
//        }
//    }
//
//    /**
//     * refresh discovery data, and cache
//     *
//     * @param key
//     */
//    private void refreshDiscoveryData(String key){
//
//        Set<String> keys = new HashSet<String>();
//        if (key!=null && key.trim().length()>0) {
//            keys.add(key);
//        } else {
//            if (discoveryData.size() > 0) {
//                keys.addAll(discoveryData.keySet());
//            }
//        }
//
//        if (keys.size() > 0) {
//            for (String keyItem: keys) {
//
//                // add-values
//                String path = keyToPath(keyItem);
//                Map<String, String> childPathData = xxlZkClient.getChildPathData(path);
//
//                // exist-values
//                TreeSet<String> existValues = discoveryData.get(keyItem);
//                if (existValues == null) {
//                    existValues = new TreeSet<String>();
//                    discoveryData.put(keyItem, existValues);
//                }
//
//                if (childPathData.size() > 0) {
//                	existValues.clear();
//                    existValues.addAll(childPathData.keySet());
//                }
//            }
//            logger.info(">>>>>>>>>>> xxl-rpc, refresh discovery data success, discoveryData = {}", discoveryData);
//        }
//    }
//
//    /**
//     * refresh registry data
//     */
//    private void refreshRegistryData(){
//        if (registryData.size() > 0) {
//            for (Map.Entry<String, TreeSet<String>> item: registryData.entrySet()) {
//                String key = item.getKey();
//                for (String value:item.getValue()) {
//                    // make path, child path
//                    String path = keyToPath(key);
//                    xxlZkClient.setChildPathData(path, value, "");
//                }
//            }
//            logger.info(">>>>>>>>>>> xxl-rpc, refresh registry data success, registryData = {}", registryData);
//        }
//    }
//
//    @Override
//    public boolean registry(Set<String> keys, String value) {
//        for (String key : keys) {
//            // local cache
//            TreeSet<String> values = registryData.get(key);
//            if (values == null) {
//                values = new TreeSet<>();
//                registryData.put(key, values);
//            }
//            values.add(value);
//
//            // make path, child path
//            String path = keyToPath(key);
//            xxlZkClient.setChildPathData(path, value, "");
//        }
//        logger.info(">>>>>>>>>>> xxl-rpc, registry success, keys = {}, value = {}", keys, value);
//        return true;
//    }
//
//    @Override
//    public boolean remove(Set<String> keys, String value) {
//        for (String key : keys) {
//            TreeSet<String> values = discoveryData.get(key);
//            if (values != null) {
//                values.remove(value);
//            }
//            String path = keyToPath(key);
//            xxlZkClient.deleteChildPath(path, value);
//        }
//        logger.info(">>>>>>>>>>> xxl-rpc, remove success, keys = {}, value = {}", keys, value);
//        return true;
//    }
//
//    @Override
//    public Map<String, TreeSet<String>> discovery(Set<String> keys) {
//        if (keys==null || keys.size()==0) {
//            return null;
//        }
//        Map<String, TreeSet<String>> registryDataTmp = new HashMap<String, TreeSet<String>>();
//        for (String key : keys) {
//            TreeSet<String> valueSetTmp = discovery(key);
//            if (valueSetTmp != null) {
//                registryDataTmp.put(key, valueSetTmp);
//            }
//        }
//        return registryDataTmp;
//    }
//
//    @Override
//    public TreeSet<String> discovery(String key) {
//
//        // local cache
//        TreeSet<String> values = discoveryData.get(key);
//        if (values == null) {
//
//            // refreshDiscoveryData (one)：first use
//            refreshDiscoveryData(key);
//
//            values = discoveryData.get(key);
//        }
//
//        return values;
//    }
//
//}
