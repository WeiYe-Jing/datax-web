package com.wugui.datax.admin.tool.build;

import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.wugui.datax.admin.dto.HiveWriterDTO;
import com.wugui.datax.admin.dto.RdbmsReaderDTO;
import com.wugui.datax.admin.tool.datax.reader.MysqlReader;

public class DataxBuildHelper implements BaseBuildHelper {
    // 存放数据同步job运行的配置信息
    private Configuration task;

    @Override
    public String build(JSONObject jsonObject) {
        // 创建一个对象存放Job参数
        this.task = Configuration.from("{}");

        buildReader(jsonObject);
        buildWriter(jsonObject);
        buildTransformer(jsonObject);
        buildSpeed(jsonObject);
        buildrrorLimit(jsonObject);

        System.out.println(this.task.beautify());
        return this.task.toJSON();
    }



    ;


    @Override
    public void buildReader(JSONObject jsonObject) {
        this.task.set("job.content[0].reader", jsonObject.get("reader"));

    }

    @Override
    public void buildWriter(JSONObject jsonObject) {

        this.task.set("job.content[0].writer", jsonObject.get("writer"));

    }

    @Override
    public void buildTransformer(JSONObject jsonObject) {

        if (null != jsonObject.get("transformer")) {
            this.task.set("job.content[0].transformer", jsonObject.get("transformer"));

        }
    }

    @Override
    public void buildFilter(JSONObject jsonObject) {

    }

    /**
     * 构建限速参数
     *
     * @param jsonObject
     */
    public void buildSpeed(JSONObject jsonObject) {
        if (null != jsonObject.get("speed")) {
            this.task.set("job.setting.speed", jsonObject.get("speed"));

        }
    }

    /**
     * 构建脏数据参数
     *
     * @param jsonObject
     */
    public void buildrrorLimit(JSONObject jsonObject) {
        if (null != jsonObject.get("errorLimit")) {
            // 这些set path需要提取为常量
            this.task.set("job.setting.errorLimit", jsonObject.get("errorLimit"));
        }

    }


    public static void main(String[] args) {
        DataxBuildHelper helper = new DataxBuildHelper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reader", new RdbmsReaderDTO());
        jsonObject.put("writer", new HiveWriterDTO());
        jsonObject.put("transformer", new JSONObject());
        jsonObject.put("speed", new JSONObject());
        jsonObject.put("errorLimit", new JSONObject());
        String build = helper.build(jsonObject);
        // {"job":{"content":[{"reader":{},"transformer":{},"writer":{}}],"setting":{"errorLimit":{},"speed":{}}}}

    }
}
