package com.wugui.datax.client.enums;

/**
 * Created by xuxueli on 17/3/10.
 */
public enum ExecutorRouteStrategyEnum {
	/**
	 * 第一个
	 */
	FIRST("第一个"),
	/**
	 * 最后一个
	 */
	LAST("最后一个"),
	/**
	 * 轮询
	 */
	ROUND("轮询"),
	/**
	 * 随机
	 */
	RANDOM("随机"),
	/**
	 * 一致性HASH
	 */
	CONSISTENT_HASH("一致性HASH"),
	/**
	 * 最不经常使用
	 */
	LEAST_FREQUENTLY_USED("最不经常使用"),
	/**
	 * 最近最久未使用
	 */
	LEAST_RECENTLY_USED("最近最久未使用"),
	/**
	 * 故障转移
	 */
	FAILOVER("故障转移"),
	/**
	 * 忙碌转移
	 */
	BUSYOVER("忙碌转移"),
	/**
	 * 分片广播
	 */
	SHARDING_BROADCAST("分片广播");

	ExecutorRouteStrategyEnum(String title) {
		this.title = title;
	}

	private String title;

	public String getTitle() {
		return title;
	}

	public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem) {
		if (name != null) {
			for (ExecutorRouteStrategyEnum item : ExecutorRouteStrategyEnum.values()) {
				if (item.name().equals(name)) {
					return item;
				}
			}
		}
		return defaultItem;
	}
}
