package com.wugui.tool.util;

import com.wugui.dataxweb.entity.JobJdbcDatasource;
import com.wugui.tool.database.TableInfo;
import com.wugui.tool.query.BaseQueryTool;
import com.wugui.tool.query.QueryToolFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库操作相关的工具类
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DasUtil
 * @Version 1.0
 * @since 2019/7/17 13:42
 */
@Slf4j
public class DasUtil {
    private BaseQueryTool queryTool;

    private JobJdbcDatasource jobJdbcDatasource;

    public DasUtil(JobJdbcDatasource jobJdbcDatasource) {
        //根据dbType获取不同的queryToolInterface
        this.queryTool = QueryToolFactory.getByDbType(jobJdbcDatasource);
        this.jobJdbcDatasource = jobJdbcDatasource;
    }

    /**
     * 根据表名获取 TableInfo实体
     *
     * @param tableName
     * @return
     */
    public TableInfo buildTableInfo(String tableName) {
        return queryTool.buildTableInfo(tableName);
    }

//    /**
//     * 生成datax reader配置信息，
//     * @param tableName
//     * @return
//     */
//    public String genDataxJsonReader(String tableName){
//        return queryTool.genDataxJsonReader(tableName);
//    }


}
