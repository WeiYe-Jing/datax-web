package com.wugui.tool.datax;

import java.util.Map;

/**
 * 插件基础接口
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DataxPluginInterface
 * @Version 1.0
 * @since 2019/7/30 22:59
 */
public interface DataxPluginInterface {
    /**
     * 获取reader插件名称
     *
     * @return
     */
    String getName();

    /**
     * 构建
     *
     * @return
     */
    Map<String, Object> build();

    /**
     * 获取示例
     *
     * @return
     */
    Map<String, Object> sample();
}
