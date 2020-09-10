package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.enums.QueryToolEnum;

import java.lang.reflect.Constructor;

/**
 * 工具类，获取单例实体
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName QueryToolFactory
 * @Version 1.0
 * @since 2019/7/18 9:36
 */
public class QueryToolFactory {

    public static BaseQueryTool getByDbType(JobDatasource jobDatasource) {
        //获取dbType
        String datasource = jobDatasource.getDatasource();
        QueryToolEnum queryToolEnum = QueryToolEnum.getQueryToolEnum(datasource);
        try {
            Constructor<BaseQueryTool> c = queryToolEnum.getClazz().getConstructor(JobDatasource.class);
            BaseQueryTool queryTool = c.newInstance(jobDatasource);
            return queryTool;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
