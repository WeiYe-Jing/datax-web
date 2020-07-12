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

    /**
     * buildJob
     *
     * @return
     */
    Map<String, Object> buildJob();

    /**
     * buildSetting
     *
     * @return
     */
    Map<String, Object> buildSetting();

    /**
     * buildContent
     *
     * @return
     */
    Map<String, Object> buildContent();

    /**
     * buildReader
     *
     * @return
     */
    Map<String, Object> buildReader();

    /**
     * buildHiveReader
     *
     * @return
     */
    Map<String, Object> buildHiveReader();

    /**
     * buildHiveWriter
     *
     * @return
     */
    Map<String, Object> buildHiveWriter();

    /**
     * buildHBaseReader
     *
     * @return
     */
    Map<String, Object> buildHBaseReader();

    /**
     * buildHBaseWriter
     *
     * @return
     */
    Map<String, Object> buildHBaseWriter();

    /**
     * buildMongoDBReader
     *
     * @return
     */
    Map<String, Object> buildMongoDBReader();

    /**
     * buildMongoDBWriter
     *
     * @return
     */
    Map<String, Object> buildMongoDBWriter();

    /**
     * buildWriter
     *
     * @return
     */
    Map<String, Object> buildWriter();
}
