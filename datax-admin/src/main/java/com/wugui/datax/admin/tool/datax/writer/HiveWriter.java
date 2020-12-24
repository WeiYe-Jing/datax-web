package com.wugui.datax.admin.tool.datax.writer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.dto.HiveWriterDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.pojo.DataxHivePojo;
import com.wugui.datax.admin.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * hive writer构建类
 *
 * @author jingwk
 * @version 2.0
 * @since 2020/01/05
 */
public class HiveWriter extends BaseWriterPlugin implements DataxWriterInterface {

    @Override
    public String getName() {
        return "hdfswriter";
    }

    @Override
    public Map<String, Object> buildWriter(DataXJsonBuildDTO dataxJsonDto, JobDatasource writerDatasource) {
        List<String> writerColumns = dataxJsonDto.getWriterColumns();
        HiveWriterDTO hiveWriterDto = dataxJsonDto.getHiveWriter();

        DataxHivePojo dataxHivePojo = new DataxHivePojo();
        dataxHivePojo.setJdbcDatasource(writerDatasource);
        List<Map<String, Object>> columns = Lists.newArrayList();
        writerColumns.forEach(c -> {
            Map<String, Object> column = Maps.newLinkedHashMap();
            column.put("name", c.split(Constants.SPLIT_SCOLON)[1]);
            column.put("type", c.split(Constants.SPLIT_SCOLON)[2]);
            columns.add(column);
        });
        dataxHivePojo.setColumns(columns);
        dataxHivePojo.setWriterDefaultFS(hiveWriterDto.getWriterDefaultFS());
        dataxHivePojo.setWriteFieldDelimiter(StringUtil.unicode2String(hiveWriterDto.getWriteFieldDelimiter()));
        dataxHivePojo.setWriterFileType(hiveWriterDto.getWriterFileType());
        dataxHivePojo.setWriterPath(hiveWriterDto.getWriterPath());
        dataxHivePojo.setWriteMode(hiveWriterDto.getWriteMode());
        dataxHivePojo.setWriterFileName(hiveWriterDto.getWriterFileName());
        return buildHive(dataxHivePojo);
    }

    public Map<String, Object> buildHive(DataxHivePojo plugin) {

        Map<String, Object> parameter = Maps.newLinkedHashMap();
        parameter.put("defaultFS", plugin.getWriterDefaultFS());
        parameter.put("fileType", plugin.getWriterFileType());
        parameter.put("path", plugin.getWriterPath());
        parameter.put("fileName", plugin.getWriterFileName());
        parameter.put("writeMode", plugin.getWriteMode());
        parameter.put("fieldDelimiter", plugin.getWriteFieldDelimiter());
        parameter.put("column", plugin.getColumns());

        Map<String, Object> writer = Maps.newLinkedHashMap();
        writer.put("name", getName());
        writer.put("parameter", parameter);
        return writer;
    }
}
