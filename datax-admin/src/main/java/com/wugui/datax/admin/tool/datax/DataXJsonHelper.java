package com.wugui.datax.admin.tool.datax;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.datax.reader.DataxReaderInterface;
import com.wugui.datax.admin.tool.datax.writer.DataxWriterInterface;
import com.wugui.datax.admin.tool.enums.DbTypePlugin;
import com.wugui.datax.admin.tool.pojo.DataXTransformer;
import com.wugui.datax.admin.util.TransformerUtil;
import lombok.Data;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 构建 com.wugui.datax json的工具类
 *
 * @author jingwk
 * @ClassName DataxJsonHelper
 * @Version 2.1.1
 * @since 2020/03/14 08:24
 */
@Data
public class DataXJsonHelper {

    private Map<String, Object> buildReader;

    private Map<String, Object> buildWriter;

    private DataxReaderInterface readerPlugin;

    private DataxWriterInterface writerPlugin;

    private List<DataXTransformer> transformers = new ArrayList<>();


    //用于保存额外参数
    private Map<String, Object> extraParams = Maps.newHashMap();

    /**
     * 构建reader对象
     *
     * @param dataxJsonDto
     * @param readerDatasource
     * @return
     * @author Locki
     * @date 2020/12/24
     */
    public void initReader(DataXJsonBuildDTO dataxJsonDto, JobDatasource readerDatasource) {
        DbType dbType = readerDatasource.getType();
        List<String> readerColumns = dataxJsonDto.getReaderColumns();
        dataxJsonDto.setReaderColumns(convertKeywordsColumns(dbType, readerColumns));
        readerPlugin = DbTypePlugin.getDbTypePlugin(dbType).getReaderPlugin();
        buildReader = readerPlugin.buildReader(dataxJsonDto, readerDatasource);
    }

    /**
     * 构建writer对象
     *
     * @param dataxJsonDto
     * @param writerDatasource
     * @return
     * @author Locki
     * @date 2020/12/24
     */
    public void initWriter(DataXJsonBuildDTO dataxJsonDto, JobDatasource writerDatasource) {
        DbType dbType = writerDatasource.getType();
        List<String> writerColumns = dataxJsonDto.getWriterColumns();
        dataxJsonDto.setWriterColumns(convertKeywordsColumns(dbType, writerColumns));

        writerPlugin = DbTypePlugin.getDbTypePlugin(dbType).getWriterPlugin();
        buildWriter = writerPlugin.buildWriter(dataxJsonDto,writerDatasource);
    }

    /**
     * 处理数据源关键字
     *
     * @param dbType
     * @param columns
     * @return {@link List< String>}
     * @author Locki
     * @date 2020/12/24
     */
    private List<String> convertKeywordsColumns(DbType dbType, List<String> columns) {
        if (columns == null) {
            return null;
        }

        List<String> toColumns = new ArrayList<>();
        columns.forEach(s -> {
            toColumns.add(doConvertKeywordsColumn(dbType, s));
        });
        return toColumns;
    }

    /**
     * 处理数据源关键字
     *
     * @param dbType
     * @param column
     * @return {@link String}
     * @author Locki
     * @date 2020/12/24
     */
    private String doConvertKeywordsColumn(DbType dbType, String column) {
        if (column == null) {
            return null;
        }

        column = column.trim();
        column = column.replace("[", "");
        column = column.replace("]", "");
        column = column.replace("`", "");
        column = column.replace("\"", "");
        column = column.replace("'", "");

        switch (dbType) {
            case MYSQL:
                return String.format("`%s`", column);
            case SQLSERVER:
                return String.format("[%s]", column);
            case POSTGRESQL:
            case ORACLE:
                return String.format("\"%s\"", column);
            default:
                return column;
        }
    }

    /**
     * 初始化脱敏规则
     * 暂时实现
     * 1.对字段进行MD5脱敏
     * 2.对字段替换换行符
     *
     * @param dataXJsonBuildDto
     */
    public void initTransformer(DataXJsonBuildDTO dataXJsonBuildDto) {
        if (null == dataXJsonBuildDto.getTransformer() || dataXJsonBuildDto.getTransformer().size() == 0) {
            return;
        }
        for (int i = 0; i < dataXJsonBuildDto.getTransformer().size(); i++) {
            if (TextUtils.isBlank(TransformerUtil.getTransformerName(dataXJsonBuildDto.getTransformer().get(i)))) {
                continue;
            }
            DataXTransformer t = new DataXTransformer();
            t.setName(TransformerUtil.getTransformerName(dataXJsonBuildDto.getTransformer().get(i)));
            DataXTransformer.Parameter p = new DataXTransformer.Parameter();
            List<String> paras = new ArrayList<>();
            if ("replaceNewLineSymbol".equals(t.getName())) {
                paras.add("");
                p.setColumnIndex(i);

            } else if ("dx_md5".equals(t.getName())) {
                paras.add("");
                p.setColumnIndex(i);

            }
            p.setParas(paras);
            t.setParameter(p);
            transformers.add(t);
        }
    }

    /**
     * 构建任务Job任务对象
     *
     * @param
     * @return {@link Map< String, Object>}
     * @author Locki
     * @date 2020/12/24
     */
    public Map<String, Object> buildJob() {
        Map<String, Object> res = Maps.newLinkedHashMap();
        Map<String, Object> jobMap = Maps.newLinkedHashMap();
        jobMap.put("core",buildCore());
        jobMap.put("setting", buildSetting());
        jobMap.put("content", ImmutableList.of(buildContent()));
        res.put("job", jobMap);
        return res;
    }

    /**
     * 构建任务core对象
     *
     * @param
     * @return {@link Map< String, Object>}
     * @author Locki
     * @date 2020/12/24
     */
    public Map<String, Object> buildCore() {
        Map<String, Object> result = Maps.newLinkedHashMap();
        Map<String, Object> transportMap = Maps.newLinkedHashMap();
        Map<String, Object> channelMap = Maps.newLinkedHashMap();
        Map<String, Object> speedMap = Maps.newLinkedHashMap();
        speedMap.put("byte",1048576/3);
        channelMap.put("speed",speedMap);
        transportMap.put("channel",channelMap);
        result.put("transport",transportMap);
        return result;
    }

    /**
     * 构建任务setting对象
     *
     * @param
     * @return {@link Map< String, Object>}
     * @author Locki
     * @date 2020/12/24
     */
    public Map<String, Object> buildSetting() {
        Map<String, Object> res = Maps.newLinkedHashMap();
        Map<String, Object> speedMap = Maps.newLinkedHashMap();
        Map<String, Object> errorLimitMap = Maps.newLinkedHashMap();
        speedMap.putAll(ImmutableMap.of("channel", 3, "byte", 1048576));
        errorLimitMap.putAll(ImmutableMap.of("record", 0, "percentage", 0.02));
        res.put("speed", speedMap);
        res.put("errorLimit", errorLimitMap);
        return res;
    }

    /**
     * 构建任务content对象（reader/writer）
     *
     * @param
     * @return {@link Map< String, Object>}
     * @author Locki
     * @date 2020/12/24
     */
    public Map<String, Object> buildContent() {
        Map<String, Object> res = Maps.newLinkedHashMap();
        res.put("reader", this.buildReader);
        res.put("writer", this.buildWriter);
        if (transformers.size() > 0) {
            res.put("transformer", transformers);
        }
        return res;
    }
}