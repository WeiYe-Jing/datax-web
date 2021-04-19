package com.wugui.datax.admin.tool.datax.writer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.datasource.HBaseDataSource;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.dto.HbaseWriterDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.pojo.DataxHbasePojo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author jingwk
 */
public class HBaseWriter extends BaseWriterPlugin implements DataxWriterInterface {

    @Override
    public String getName() {
        return "hbase11xwriter";
    }

    @Override
    public Map<String, Object> buildWriter(DataXJsonBuildDTO dataxJsonDto, JobDatasource writerDatasource) {
        List<String> writerColumns = dataxJsonDto.getWriterColumns();
        List<String> writerTables = dataxJsonDto.getWriterTables();
        HbaseWriterDTO hbaseWriterDto = dataxJsonDto.getHbaseWriter();

        DataxHbasePojo dataxHbasePojo = new DataxHbasePojo();
        dataxHbasePojo.setJdbcDatasource(writerDatasource);
        List<Map<String, Object>> columns = Lists.newArrayList();
        for (int i = 0; i < writerColumns.size(); i++) {
            Map<String, Object> column = Maps.newLinkedHashMap();
            column.put("index", i + 1);
            column.put("name", writerColumns.get(i));
            column.put("type", "string");
            columns.add(column);
        }
        dataxHbasePojo.setColumns(columns);
        HBaseDataSource HBaseDataSource = JSONUtils.parseObject(writerDatasource.getConnectionParams(), HBaseDataSource.class);
        dataxHbasePojo.setWriterHbaseConfig(HBaseDataSource.getZkAddress());
        String writerTable = !CollectionUtils.isEmpty(writerTables) ? writerTables.get(0) : Constants.STRING_BLANK;
        dataxHbasePojo.setWriterTable(writerTable);
        dataxHbasePojo.setWriterVersionColumn(hbaseWriterDto.getWriterVersionColumn());
        dataxHbasePojo.setWriterRowkeyColumn(hbaseWriterDto.getWriterRowkeyColumn());
        dataxHbasePojo.setWriterMode(hbaseWriterDto.getWriterMode());
        return buildHbase(dataxHbasePojo);
    }

    public Map<String, Object> buildHbase(DataxHbasePojo plugin) {

        Map<String, Object> config = Maps.newLinkedHashMap();
        config.put("hbase.zookeeper.quorum", plugin.getWriterHbaseConfig());

        Map<String, Object> parameter = Maps.newLinkedHashMap();
        parameter.put("hbaseConfig", config);
        parameter.put("table", plugin.getWriterTable());
        parameter.put("mode", plugin.getWriterMode());
        parameter.put("column", plugin.getColumns());
        parameter.put("rowkeyColumn", JSON.parseArray(plugin.getWriterRowkeyColumn()));
        if (StringUtils.isNotBlank(plugin.getWriterVersionColumn().getValue())) {
            parameter.put("versionColumn", plugin.getWriterVersionColumn());
        }

        Map<String, Object> writer = Maps.newLinkedHashMap();
        writer.put("name", getName());
        writer.put("parameter", parameter);
        return writer;
    }
}
