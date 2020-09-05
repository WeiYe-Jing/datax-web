package com.wugui.datax.admin.tool.build;

import com.google.common.collect.Maps;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.datax.reader.*;
import com.wugui.datax.admin.util.DataXException;
import com.wugui.datax.admin.util.ErrorCode;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.wugui.datax.admin.util.JdbcConstants.*;
import static com.wugui.datax.admin.util.JdbcConstants.MONGODB;

public class BuildFlinkxReaderParameter extends BuildReaderParameter {
    protected static final String FLINKX_MYSQL_READER_NAME="mysqlreader";
    protected static final String FLINKX_ORACLE_READER_NAME="oraclereader";
    protected static final String FLINKX_SQLSERVER_READER_NAME="sqlserverreader";
    protected static final String FLINKX_POSTGRESQL_READER_NAME="postgresqlreader";
    protected static final String FLINKX_CLICKHOUSE_READER_NAME="clickhousereader";
    protected static final String FLINKX_HIVE_READER_NAME="hdfsreader";

    /**
     * 构建读取jdbc数据源的params
     * @param dataXJsonBuildDto
     * @param readerDatasource
     * @return
     */
    @Override
    public Map<String, Object> getJdbcReaderParameters(DataXJsonBuildDTO dataXJsonBuildDto, JobDatasource readerDatasource) throws Exception {
        LinkedHashMap<Object, Object> reader = Maps.newLinkedHashMap();

        String readerPluginName = this.getReaderPluginName(readerDatasource);
        reader.put("name",readerPluginName);

        return null;
    }















    

    /**
     * 获取reader插件的名称
     * @param readerDatasource
     * @return
     * @throws Exception
     */
    private String getReaderPluginName(JobDatasource readerDatasource) throws Exception {
        String datasource = readerDatasource.getDatasource();
        if (MYSQL.equals(datasource)) {
           return FLINKX_MYSQL_READER_NAME;
        } else if (ORACLE.equals(datasource)) {
            return FLINKX_ORACLE_READER_NAME;
        } else if (SQL_SERVER.equals(datasource)) {
           return FLINKX_SQLSERVER_READER_NAME;
        } else if (POSTGRESQL.equals(datasource)) {
           return FLINKX_POSTGRESQL_READER_NAME;
        } else if (CLICKHOUSE.equals(datasource)) {
            return FLINKX_CLICKHOUSE_READER_NAME;
        } else if (HIVE.equals(datasource)) {
           return FLINKX_HIVE_READER_NAME;
        } else{
            throw new Exception("暂时不支持Reader数据源："+datasource);
        }
    }
}
