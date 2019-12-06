package com.wugui.datax.rpc.old.registry.impl.util;//package com.xxl.rpc.util;
//
//import org.apache.zookeeper.*;
//import org.apache.zookeeper.data.Stat;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.ReentrantLock;
//
//
///**
// * ZooKeeper cfg client (Watcher + some utils)
// *
// * @author xuxueli 2015-08-26 21:36:43
// */
//public class XxlZkClient {
//	private static Logger logger = LoggerFactory.getLogger(XxlZkClient.class);
//
//
//	private String zkaddress;
//	private String zkpath;
//	private String zkdigest;
//	private Watcher watcher;	// watcher(One-time trigger)
//
//
//	public XxlZkClient(String zkaddress, String zkpath, String zkdigest, Watcher watcher) {
//
//		this.zkaddress = zkaddress;
//		this.zkpath = zkpath;
//		this.zkdigest = zkdigest;
//		this.watcher = watcher;
//
//		// reconnect when expire
//		if (this.watcher == null) {
//			// watcher(One-time trigger)
//			this.watcher = new Watcher() {
//				@Override
//				public void process(WatchedEvent watchedEvent) {
//					logger.info(">>>>>>>>>>> xxl-rpc: watcher:{}", watchedEvent);
//
//					// session expire, close old and create new
//					if (watchedEvent.getState() == Event.KeeperState.Expired) {
//						destroy();
//						getClient();
//					}
//				}
//			};
//		}
//
//		//getClient();		// async coon, support init without conn
//	}
//
//	// ------------------------------ zookeeper client ------------------------------
//	private ZooKeeper zooKeeper;
//	private ReentrantLock INSTANCE_INIT_LOCK = new ReentrantLock(true);
//	public ZooKeeper getClient(){
//		if (zooKeeper==null) {
//			try {
//				if (INSTANCE_INIT_LOCK.tryLock(2, TimeUnit.SECONDS)) {
//
//                    // init new-client
//                    ZooKeeper newZk = null;
//                    try {
//                        if (zooKeeper == null) {		// 二次校验，防止并发创建client
//                            newZk = new ZooKeeper(zkaddress, 10000, watcher);
//                            if (zkdigest!=null && zkdigest.trim().length()>0) {
//                                newZk.addAuthInfo("digest",zkdigest.getBytes());		// like "account:password"
//                            }
//                            newZk.exists(zkpath, false);		// sync wait until succcess conn
//
//                            // set success new-client
//                            zooKeeper = newZk;
//                            logger.info(">>>>>>>>>>> xxl-rpc, XxlZkClient init success.");
//                        }
//                    } catch (Exception e) {
//                        // close fail new-client
//                        if (newZk != null) {
//                            newZk.close();
//                        }
//
//                        logger.error(e.getMessage(), e);
//                    } finally {
//                        INSTANCE_INIT_LOCK.unlock();
//                    }
//
//				}
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//		if (zooKeeper == null) {
//			throw new XxlRpcException("XxlZkClient.zooKeeper is null.");
//		}
//		return zooKeeper;
//	}
//
//	public void destroy(){
//		if (zooKeeper!=null) {
//			try {
//				zooKeeper.close();
//				zooKeeper = null;
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//	}
//
//	// ------------------------------ util ------------------------------
//
//	/**
//	 * create node path with parent path (PERSISTENT)
//	 *
//	 * zk limit parent must exist
//	 *
//	 * @param path
//	 * @param watch
//	 */
//	private Stat createPathWithParent(String path, boolean watch){
//		// valid
//		if (path==null || path.trim().length()==0) {
//			return null;
//		}
//
//		try {
//			Stat stat = getClient().exists(path, watch);
//			if (stat == null) {
//				//  valid parent, createWithParent if not exists
//				if (path.lastIndexOf("/") > 0) {
//					String parentPath = path.substring(0, path.lastIndexOf("/"));
//					Stat parentStat = getClient().exists(parentPath, watch);
//					if (parentStat == null) {
//						createPathWithParent(parentPath, false);
//					}
//				}
//				// create desc node path
//				getClient().create(path, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//			}
//			return getClient().exists(path, true);
//		} catch (Exception e) {
//			throw new XxlRpcException(e);
//		}
//	}
//
//	/**
//	 * delete path (watch)
//	 *
//	 * @param path
//	 * @param watch
//	 */
//	public void deletePath(String path, boolean watch){
//		try {
//			Stat stat = getClient().exists(path, watch);
//			if (stat != null) {
//				getClient().delete(path, stat.getVersion());
//			} else {
//				logger.info(">>>>>>>>>>> zookeeper node path not found :{}", path);
//			}
//		} catch (Exception e) {
//			throw new XxlRpcException(e);
//		}
//	}
//
//	/**
//	 * set data to node (watch)
//	 * @param path
//	 * @param data
//	 * @param watch
//	 * @return
//	 */
//	public Stat setPathData(String path, String data, boolean watch) {
//		try {
//			Stat stat = getClient().exists(path, watch);
//			if (stat == null) {
//				createPathWithParent(path, watch);
//				stat = getClient().exists(path, watch);
//			}
//			return getClient().setData(path, data.getBytes("UTF-8"), stat.getVersion());
//		} catch (Exception e) {
//			throw new XxlRpcException(e);
//		}
//	}
//
//	/**
//	 * get data from node (watch)
//	 *
//	 * @param path
//	 * @param watch
//	 * @return
//	 */
//	public String getPathData(String path, boolean watch){
//		try {
//			String znodeValue = null;
//			Stat stat = getClient().exists(path, watch);
//			if (stat != null) {
//				byte[] resultData = getClient().getData(path, watch, null);
//				if (resultData != null) {
//					znodeValue = new String(resultData, "UTF-8");
//				}
//			} else {
//				logger.info(">>>>>>>>>>> xxl-rpc, path[{}] not found.", path);
//			}
//			return znodeValue;
//		} catch (Exception e) {
//			throw new XxlRpcException(e);
//		}
//	}
//
//
//	// ---------------------- child ----------------------
//
//	/**
//	 * set child pach data (EPHEMERAL)
//	 *
//	 * @param path
//	 * @param childNode
//	 * @param childNodeData
//	 */
//	public void setChildPathData(String path, String childNode, String childNodeData) {
//		try {
//
//			// set path
//			createPathWithParent(path, false);
//
//
//			// set child path
//			String childNodePath = path.concat("/").concat(childNode);
//
//			Stat stat = getClient().exists(childNodePath, false);
//			if (stat!=null) {	// EphemeralOwner=0、PERSISTENT and delete
//				if (stat.getEphemeralOwner()==0) {
//					getClient().delete(childNodePath, stat.getVersion());
//				} else {
//					return;		// EPHEMERAL and pass
//				}
//			}
//
//			getClient().create(childNodePath, childNodeData.getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//		} catch (Exception e) {
//			throw new XxlRpcException(e);
//		}
//	}
//
//	/**
//	 * delete child path
//	 *
//	 * @param path
//	 * @param childNode
//	 */
//	public void deleteChildPath(String path, String childNode) {
//		try {
//			// delete child path
//			String childNodePath = path.concat("/").concat(childNode);
//			deletePath(childNodePath, false);
//		} catch (Exception e) {
//			throw new XxlRpcException(e);
//		}
//	}
//
//	/**
//	 * get child path data
//	 *
//	 * @return
//	 */
//	public Map<String, String> getChildPathData(String path){
//		Map<String, String> allData = new HashMap<String, String>();
//		try {
//			Stat stat = getClient().exists(path, true);
//			if (stat == null) {
//				return allData;	// no such node
//			}
//
//			List<String> childNodes = getClient().getChildren(path, true);
//			if (childNodes!=null && childNodes.size()>0) {
//				for (String childNode : childNodes) {
//
//					// child data
//					String childNodePath = path.concat("/").concat(childNode);
//					String childNodeValue = getPathData(childNodePath, false);
//
//					allData.put(childNode, childNodeValue);
//				}
//			}
//			return allData;
//		} catch (Exception e) {
//			throw new XxlRpcException(e);
//		}
//	}
//
//
//}