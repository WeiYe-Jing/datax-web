package com.wugui.datax.admin.tool.build;

import com.alibaba.fastjson.JSONObject;


public interface BaseBuildHelper {

    /**
     * 构建任务参数，并返回构建的json或者yaml
     * @param jsonObject
     * @return 返回构建的json或者yaml
     */
    String build(JSONObject jsonObject);



    /**
     * 构建任务参数的Reader部分
     * @param jsonObject
     */
    void buildReader(JSONObject jsonObject);

    /**
     * 构建任务参数的Writer部分
     * @param jsonObject
     */
    void buildWriter(JSONObject jsonObject);


    /**
     * 构建任务参数的Transformer部分
     * @param jsonObject
     */
    void buildTransformer(JSONObject jsonObject);


    /**
     * 构建任务参数的Filter部分
     * @param jsonObject
     */
    void buildFilter(JSONObject jsonObject);




}
