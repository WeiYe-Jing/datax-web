package com.wugui.datax.admin.tool.datax.reader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.datasource.HBaseDataSource;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.dto.HbaseReaderDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.pojo.DataxHbasePojo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class HBaseReader extends BaseReaderPlugin implements DataxReaderInterface {

    @Override
    public String getName() {
        return "hbase11xreader";
    }

    @Override
    public Map<String, Object> buildReader(DataXJsonBuildDTO dataxJsonDto, JobDatasource readerDatasource) {
        List<String> readerTables = dataxJsonDto.getReaderTables();
        List<String> readerColumns = dataxJsonDto.getReaderColumns();
        HbaseReaderDTO hbaseReaderDto = dataxJsonDto.getHbaseReader();

        DataxHbasePojo dataxHbasePojo = new DataxHbasePojo();
        dataxHbasePojo.setJdbcDatasource(readerDatasource);
        List<Map<String, Object>> columns = Lists.newArrayList();
        for (int i = 0; i < readerColumns.size(); i++) {
            Map<String, Object> column = Maps.newLinkedHashMap();
            column.put("name", readerColumns.get(i));
            column.put("type", "string");
            columns.add(column);
        }
        dataxHbasePojo.setColumns(columns);
        HBaseDataSource HBaseDataSource = JSONUtils.parseObject(readerDatasource.getConnectionParams(), HBaseDataSource.class);
        dataxHbasePojo.setReaderHbaseConfig(HBaseDataSource.getZkAddress());
        String readerTable = !CollectionUtils.isEmpty(readerTables) ? readerTables.get(0) : Constants.STRING_BLANK;
        dataxHbasePojo.setReaderTable(readerTable);
        dataxHbasePojo.setReaderMode(hbaseReaderDto.getReaderMode());
        dataxHbasePojo.setReaderRange(hbaseReaderDto.getReaderRange());
        return buildHbase(dataxHbasePojo);
    }

    public Map<String, Object> buildHbase(DataxHbasePojo plugin) {

        Map<String, Object> config = Maps.newLinkedHashMap();
        config.put("hbase.zookeeper.quorum", plugin.getReaderHbaseConfig());
        Map<String, Object> parameter = Maps.newLinkedHashMap();
        parameter.put("hbaseConfig", config);
        parameter.put("table", plugin.getReaderTable());
        parameter.put("mode", plugin.getReaderMode());
        parameter.put("column", plugin.getColumns());
        if (StringUtils.isNotBlank(plugin.getReaderRange().getStartRowkey()) && StringUtils.isNotBlank(plugin.getReaderRange().getEndRowkey())) {
            parameter.put("range", plugin.getReaderRange());
        }
        parameter.put("maxVersion", plugin.getReaderMaxVersion());

        Map<String, Object> reader = Maps.newLinkedHashMap();
        reader.put("name", getName());
        reader.put("parameter", parameter);
        return reader;
    }
}
