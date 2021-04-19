package com.wugui.datax.admin.tool.datax.reader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.dto.HiveReaderDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.pojo.DataxHivePojo;
import com.wugui.datax.admin.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * hive reader 构建类
 *
 * @author jingwk
 * @version 2.0
 * @since 2020/01/05
 */
public class HiveReader extends BaseReaderPlugin implements DataxReaderInterface {

    @Override
    public String getName() {
        return "hdfsreader";
    }

    @Override
    public Map<String, Object> buildReader(DataXJsonBuildDTO dataxJsonDto, JobDatasource readerDatasource) {
        List<String> readerColumns = dataxJsonDto.getReaderColumns();
        HiveReaderDTO hiveReaderDto = dataxJsonDto.getHiveReader();

        DataxHivePojo dataxHivePojo = new DataxHivePojo();
        dataxHivePojo.setJdbcDatasource(readerDatasource);
        List<Map<String, Object>> columns = Lists.newArrayList();
        readerColumns.forEach(c -> {
            Map<String, Object> column = Maps.newLinkedHashMap();
            column.put("index", c.split(Constants.SPLIT_SCOLON)[0]);
            column.put("type", c.split(Constants.SPLIT_SCOLON)[2]);
            columns.add(column);
        });
        dataxHivePojo.setColumns(columns);
        dataxHivePojo.setReaderDefaultFS(hiveReaderDto.getReaderDefaultFS());
        dataxHivePojo.setReaderFieldDelimiter(StringUtil.unicode2String(hiveReaderDto.getReaderFieldDelimiter()));
        dataxHivePojo.setReaderFileType(hiveReaderDto.getReaderFileType());
        dataxHivePojo.setReaderPath(hiveReaderDto.getReaderPath());
        dataxHivePojo.setSkipHeader(hiveReaderDto.getReaderSkipHeader());
        return buildHive(dataxHivePojo);
    }

    public Map<String, Object> buildHive(DataxHivePojo plugin) {


        Map<String, Object> parameter = Maps.newLinkedHashMap();
        parameter.put("path", plugin.getReaderPath());
        parameter.put("defaultFS", plugin.getReaderDefaultFS());
        parameter.put("fileType", plugin.getReaderFileType());
        parameter.put("fieldDelimiter", plugin.getReaderFieldDelimiter());
        parameter.put("skipHeader", plugin.getSkipHeader());
        parameter.put("column", plugin.getColumns());

        Map<String, Object> reader = Maps.newLinkedHashMap();
        reader.put("name", getName());
        reader.put("parameter", parameter);
        return reader;
    }
}
