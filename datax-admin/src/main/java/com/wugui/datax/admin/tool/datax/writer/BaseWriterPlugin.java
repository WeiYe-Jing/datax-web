package com.wugui.datax.admin.tool.datax.writer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.datasource.BaseDataSource;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datax.admin.dto.*;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.datax.BaseDataXPlugin;
import com.wugui.datax.admin.tool.pojo.DataxRdbmsPojo;
import com.wugui.datax.admin.tool.query.DriverConnectionFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * datax writer base
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName BaseWriterPlugin
 * @Version 1.0
 * @since 2019/8/2 16:28
 */
public abstract class BaseWriterPlugin extends BaseDataXPlugin implements DataxWriterInterface {

    private static Pattern p = Pattern.compile("\r\n|\r|\n|\n\r");

    /**
     * 构建writer对象通用实现
     *
     * @param dataxJsonDto
     * @param writerDatasource
     * @return {@link Map< String, Object>}
     * @author Locki
     * @date 2020/12/24
     */
    @Override
    public Map<String, Object> buildWriter(DataXJsonBuildDTO dataxJsonDto, JobDatasource writerDatasource) {
        List<String> writerTables = dataxJsonDto.getWriterTables();
        List<String> writerColumns = dataxJsonDto.getWriterColumns();
        RdbmsWriterDTO rdbmsWriterDto = dataxJsonDto.getRdbmsWriter();

        DataxRdbmsPojo dataxPluginPojo = new DataxRdbmsPojo();
        dataxPluginPojo.setJobDatasource(writerDatasource);
        dataxPluginPojo.setTables(writerTables);
        dataxPluginPojo.setRdbmsColumns(writerColumns);
        dataxPluginPojo.setPreSql(rdbmsWriterDto.getPreSql());
        dataxPluginPojo.setPostSql(rdbmsWriterDto.getPostSql());
        return build(dataxPluginPojo);
    }

    public Map<String, Object> build(DataxRdbmsPojo plugin) {
        Map<String, Object> parameter = Maps.newLinkedHashMap();
        JobDatasource jobDatasource = plugin.getJobDatasource();
        BaseDataSource baseDataSource = DriverConnectionFactory.getBaseDataSource(jobDatasource.getType(), jobDatasource.getConnectionParams());

        Map<String, Object> connectionObj = Maps.newLinkedHashMap();
        connectionObj.put("table", plugin.getTables());
        connectionObj.put("jdbcUrl", baseDataSource.getJdbcUrl());
        parameter.put("connection", ImmutableList.of(connectionObj));
        parameter.put("username", baseDataSource.getUser());
        parameter.put("password", baseDataSource.getPassword());
        parameter.put("column", plugin.getRdbmsColumns());
        parameter.put("preSql", splitSql(plugin.getPreSql()));
        parameter.put("postSql", splitSql(plugin.getPostSql()));

        Map<String, Object> writer = Maps.newLinkedHashMap();
        writer.put("name", getName());
        writer.put("parameter", parameter);
        return writer;
    }

    private String[] splitSql(String sql) {
        String[] sqlArr = null;
        if (StringUtils.isNotBlank(sql)) {
            Matcher m = p.matcher(sql);
            String sqlStr = m.replaceAll(Constants.STRING_BLANK);
            sqlArr = sqlStr.split(Constants.SPLIT_COLON);
        }
        return sqlArr;
    }
}
