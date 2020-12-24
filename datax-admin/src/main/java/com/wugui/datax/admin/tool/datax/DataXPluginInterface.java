package com.wugui.datax.admin.tool.datax;

import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.pojo.DataxHbasePojo;
import com.wugui.datax.admin.tool.pojo.DataxHivePojo;
import com.wugui.datax.admin.tool.pojo.DataxMongoDBPojo;
import com.wugui.datax.admin.tool.pojo.DataxRdbmsPojo;

import java.util.Map;

/**
 * 插件基础接口
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DataxPluginInterface
 * @Version 1.0
 * @since 2019/7/30 22:59
 */
public interface DataXPluginInterface {
    /**
     * 获取reader插件名称
     *
     * @return
     */
    String getName();
}
