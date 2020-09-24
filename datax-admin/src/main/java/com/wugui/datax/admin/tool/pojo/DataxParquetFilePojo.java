package com.wugui.datax.admin.tool.pojo;

import java.util.List;
import java.util.Map;

import com.wugui.datax.admin.entity.JobDatasource;

import lombok.Data;

/**
 * 用于传参，构建json
 * @author jiangdw
 *
 */
@Data
public class DataxParquetFilePojo {
	
	/**
     * 列名
     */
    private List<Map<String, Object>> columns;

    /**
     * 数据源信息
     */
    private JobDatasource jdbcDatasource;

    /**
     * 文件路径
     */
    private String path;
    
    /**
     * 编码
     */
    private String encoding;
    
}
