package com.wugui.datax.admin.tool.datax;

import java.util.Map;

/**
 * 构建 com.wugui.datax json的基础接口
 *
 * @author jingwk
 * @ClassName DataxJsonHelper
 * @Version 2.1.1
 * @since 2020/03/14 12:24
 */
public interface DataxJsonInterface {

    Map<String, Object> buildJob();

    Map<String, Object> buildSetting();

    Map<String, Object> buildContent();

    Map<String, Object> buildReader();

    Map<String, Object> buildHiveReader();

    Map<String, Object> buildHiveWriter();

    Map<String, Object> buildHBaseReader();

    Map<String, Object> buildHBaseWriter();

    Map<String, Object> buildMongoDBReader();

    Map<String, Object> buildMongoDBWriter();

    Map<String, Object> buildWriter();
}
