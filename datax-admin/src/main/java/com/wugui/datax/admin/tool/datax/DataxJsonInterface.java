package com.wugui.datax.admin.tool.datax;

import java.util.Map;

/**
 * 构建 com.wugui.datax json的基础接口
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DataxJsonHelper
 * @Version 1.0
 * @since 2019/7/30 22:24
 */
public interface DataxJsonInterface {

    Map<String, Object> buildJob();

    Map<String, Object> buildSetting();

    Map<String, Object> buildContent();

    Map<String, Object> buildReader();

    Map<String, Object> buildHiveReader();

    Map<String, Object> buildHiveWriter();

    Map<String, Object> buildWriter();
}
