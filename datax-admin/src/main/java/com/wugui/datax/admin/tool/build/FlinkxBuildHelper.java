package com.wugui.datax.admin.tool.build;

import com.alibaba.fastjson.JSONObject;
import com.wugui.datax.admin.dto.HiveWriterDTO;
import com.wugui.datax.admin.dto.RdbmsReaderDTO;

/**
 * 继承DataxBuildHelper，增加独有的方法。
 * 可以重写DataxBuildHelper的逻辑
 */
public class FlinkxBuildHelper extends DataxBuildHelper {



    public static void main(String[] args) {
        DataxBuildHelper helper = new DataxBuildHelper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reader",new RdbmsReaderDTO());
        jsonObject.put("writer",new HiveWriterDTO());
        jsonObject.put("transformer",new JSONObject());
        jsonObject.put("speed",new JSONObject());
        jsonObject.put("errorLimit",new JSONObject());
        String build = helper.build(jsonObject);
        // {"job":{"content":[{"reader":{},"transformer":{},"writer":{}}],"setting":{"errorLimit":{},"speed":{}}}}


    }
}
