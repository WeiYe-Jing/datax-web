package com.wugui.datax.admin.tool.datax.reader;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.datasource.BaseDataSource;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.dto.RdbmsReaderDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.datax.BaseDataXPlugin;
import com.wugui.datax.admin.tool.pojo.DataxRdbmsPojo;
import com.wugui.datax.admin.tool.query.DriverConnectionFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Reader
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName BaseReaderPlugin
 * @Version 1.0
 * @since 2019/8/2 16:27
 */
public abstract class BaseReaderPlugin extends BaseDataXPlugin implements DataxReaderInterface {

    /**
     * 构建reader对象通用实现
     *
     * @param dataxJsonDto
     * @param readerDatasource
     * @return {@link Map< String, Object>}
     * @author Locki
     * @date 2020/12/24
     */
    @Override
    public Map<String, Object> buildReader(DataXJsonBuildDTO dataxJsonDto, JobDatasource readerDatasource) {
        List<String> readerTables = dataxJsonDto.getReaderTables();
        List<String> readerColumns = dataxJsonDto.getReaderColumns();
        RdbmsReaderDTO rdbmsReaderDto = dataxJsonDto.getRdbmsReader();

        DataxRdbmsPojo dataxPluginPojo = new DataxRdbmsPojo();
        dataxPluginPojo.setJobDatasource(readerDatasource);
        dataxPluginPojo.setTables(readerTables);
        dataxPluginPojo.setRdbmsColumns(readerColumns);
        dataxPluginPojo.setSplitPk(rdbmsReaderDto.getReaderSplitPk());
        if (StringUtils.isNotBlank(rdbmsReaderDto.getQuerySql())) {
            dataxPluginPojo.setQuerySql(rdbmsReaderDto.getQuerySql());
        }
        //where
        if (StringUtils.isNotBlank(rdbmsReaderDto.getWhereParams())) {
            dataxPluginPojo.setWhereParam(rdbmsReaderDto.getWhereParams());
        }
        return build(dataxPluginPojo);
    }

    public Map<String, Object> build(DataxRdbmsPojo plugin) {
        //构建
        Map<String, Object> parameter = Maps.newLinkedHashMap();
        Map<String, Object> connection = Maps.newLinkedHashMap();

        JobDatasource jobDatasource = plugin.getJobDatasource();
        BaseDataSource baseDataSource = DriverConnectionFactory.getBaseDataSource(jobDatasource.getType(), jobDatasource.getConnectionParams());
        //判断是否是 querySql
        if (StrUtil.isNotBlank(plugin.getQuerySql())) {
            connection.put("querySql", ImmutableList.of(plugin.getQuerySql()));
        } else {
            parameter.put("column", plugin.getRdbmsColumns());
            //判断是否有where
            if (StringUtils.isNotBlank(plugin.getWhereParam())) {
                parameter.put("where", plugin.getWhereParam());
            }
            connection.put("table", plugin.getTables());
        }
        connection.put("jdbcUrl", ImmutableList.of(baseDataSource.getJdbcUrl()));
        parameter.put("username", baseDataSource.getUser());
        parameter.put("password", baseDataSource.getPassword());
        parameter.put("splitPk", plugin.getSplitPk());
        parameter.put("connection", ImmutableList.of(connection));

        Map<String, Object> reader = Maps.newLinkedHashMap();
        reader.put("name", getName());
        reader.put("parameter", parameter);
        return reader;
    }
}
