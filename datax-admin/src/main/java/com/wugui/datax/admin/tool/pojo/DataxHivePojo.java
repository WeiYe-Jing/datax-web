package com.wugui.datax.admin.tool.pojo;

import com.wugui.datax.admin.entity.JobDatasource;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 用于传参，构建json
 *
 * @author jingwk
 * @ClassName DataxHivePojo
 * @Version 2.0
 * @since 2020/01/11 17:15
 */
@Data
public class DataxHivePojo {

    /**
     * hive列名
     */
    private List<Map<String,Object>> columns;

    /**
     * 数据源信息
     */
    private JobDatasource jdbcDatasource;

    private String readerPath;

    private String readerDefaultFS;

    private String readerFileType;

    private String readerFieldDelimiter;

    private String writerDefaultFS;

    private String writerFileType;

    private String writerPath;

    private String writerFileName;

    private String writeMode;

    private String writeFieldDelimiter;

    private Boolean skipHeader;
}
