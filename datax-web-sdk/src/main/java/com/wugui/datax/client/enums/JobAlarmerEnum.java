package com.wugui.datax.client.enums;

/**
 * @author Locki 2020-05-13
 */
public enum JobAlarmerEnum {
	EMAIL("邮件通知"), RPC("RPC通知");

	JobAlarmerEnum(String title) {
		this.title = title;
	}

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static JobAlarmerEnum match(String name, JobAlarmerEnum defaultItem) {
		if (name != null) {
			for (JobAlarmerEnum item : JobAlarmerEnum.values()) {
				if (item.name().equals(name)) {
					return item;
				}
			}
		}
		return defaultItem;
	}
}
