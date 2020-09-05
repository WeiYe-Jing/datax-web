package com.wugui.datax.admin.tool.build;

import com.alibaba.fastjson.JSONObject;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.entity.JobDatasource;

public class WaterdropBuildHelper extends BaseBuildHelper {


    @Override
    String build(JobDatasource readerDatasource, JobDatasource writerDatasource, DataXJsonBuildDTO dataXJsonBuildDto) {
        return null;
    }

    @Override
    public void buildReader(JSONObject jsonObject) {

    }

    @Override
    public void buildWriter(JSONObject jsonObject) {

    }

    @Override
    public void buildTransformer(JSONObject jsonObject) {

    }

    @Override
    public void buildFilter(JSONObject jsonObject) {

    }
}
