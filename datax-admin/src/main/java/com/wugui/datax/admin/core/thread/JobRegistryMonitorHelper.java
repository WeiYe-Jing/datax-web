package com.wugui.datax.admin.core.thread;

import com.wugui.datatx.core.enums.RegistryConfig;
import com.wugui.datax.admin.core.conf.XxlJobAdminConfig;
import com.wugui.datax.admin.core.model.XxlJobGroup;
import com.wugui.datax.admin.core.model.XxlJobRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * job registry instance
 * @author xuxueli 2016-10-02 19:10:24
 */
public class JobRegistryMonitorHelper {
	private static Logger logger = LoggerFactory.getLogger(JobRegistryMonitorHelper.class);

	private static JobRegistryMonitorHelper instance = new JobRegistryMonitorHelper();
	public static JobRegistryMonitorHelper getInstance(){
		return instance;
	}

	private Thread registryThread;
	private volatile boolean toStop = false;
	public void start(){
		registryThread = new Thread(() -> {
			while (!toStop) {
				try {
					// auto registry group
					List<XxlJobGroup> groupList = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupMapper().findByAddressType(0);
					if (groupList!=null && !groupList.isEmpty()) {

						// remove dead address (admin/executor)
						List<Integer> ids = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryMapper().findDead(RegistryConfig.DEAD_TIMEOUT);
						if (ids!=null && ids.size()>0) {
							XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryMapper().removeDead(ids);
						}

						// fresh online address (admin/executor)
						HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
						List<XxlJobRegistry> list = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryMapper().findAll(RegistryConfig.DEAD_TIMEOUT);
						if (list != null) {
							for (XxlJobRegistry item: list) {
								if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
									String appName = item.getRegistryKey();
									List<String> registryList = appAddressMap.get(appName);
									if (registryList == null) {
										registryList = new ArrayList<String>();
									}

									if (!registryList.contains(item.getRegistryValue())) {
										registryList.add(item.getRegistryValue());
									}
									appAddressMap.put(appName, registryList);
								}
							}
						}

						// fresh group address
						for (XxlJobGroup group: groupList) {
							List<String> registryList = appAddressMap.get(group.getAppName());
							String addressListStr = null;
							if (registryList!=null && !registryList.isEmpty()) {
								Collections.sort(registryList);
								addressListStr = "";
								for (String item:registryList) {
									addressListStr += item + ",";
								}
								addressListStr = addressListStr.substring(0, addressListStr.length()-1);
							}
							group.setAddressList(addressListStr);
							XxlJobAdminConfig.getAdminConfig().getXxlJobGroupMapper().update(group);
						}
					}
				} catch (Exception e) {
					if (!toStop) {
						logger.error(">>>>>>>>>>> xxl-job, job registry monitor thread error:{}", e);
					}
				}
				try {
					TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
				} catch (InterruptedException e) {
					if (!toStop) {
						logger.error(">>>>>>>>>>> xxl-job, job registry monitor thread error:{}", e);
					}
				}
			}
			logger.info(">>>>>>>>>>> xxl-job, job registry monitor thread stop");
		});
		registryThread.setDaemon(true);
		registryThread.setName("xxl-job, admin JobRegistryMonitorHelper");
		registryThread.start();
	}

	public void toStop(){
		toStop = true;
		// interrupt and wait
		registryThread.interrupt();
		try {
			registryThread.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
