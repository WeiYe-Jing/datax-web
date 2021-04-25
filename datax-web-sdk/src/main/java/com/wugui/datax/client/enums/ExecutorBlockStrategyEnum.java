package com.wugui.datax.client.enums;

/**
 * Created by xuxueli on 17/5/9.
 */
public enum ExecutorBlockStrategyEnum {
	/**
	 * 单机串行
	 */
    SERIAL_EXECUTION("单机串行"),
    /*CONCURRENT_EXECUTION("并行"),*/
    /**
     * 丢弃后续调度
     */
    DISCARD_LATER("丢弃后续调度"),
    /**
     * 覆盖之前调度
     */
    COVER_EARLY("覆盖之前调度");

    private String title;
    private ExecutorBlockStrategyEnum (String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public static ExecutorBlockStrategyEnum match(String name, ExecutorBlockStrategyEnum defaultItem) {
        if (name != null) {
            for (ExecutorBlockStrategyEnum item:ExecutorBlockStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }
}
