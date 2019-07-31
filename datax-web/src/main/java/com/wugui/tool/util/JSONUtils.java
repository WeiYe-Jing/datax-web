package com.wugui.tool.util;

import com.alibaba.fastjson.JSON;

/**
 * TODO
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName JSONUtils
 * @Version 1.0
 * @since 2019/7/31 14:54
 */
public class JSONUtils {

    /**
     * 返回格式化的json
     *
     * @param object
     * @return
     */
    public static String formatJson(Object object) {
        return JSON.toJSONString(object, true);
    }
}
