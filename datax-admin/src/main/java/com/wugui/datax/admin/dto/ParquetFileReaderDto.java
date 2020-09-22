package com.wugui.datax.admin.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 构建parquet file write dto
 * @author jiangdw
 *
 */
@Data
public class ParquetFileReaderDto implements Serializable {

	/**
     * 文件路径
     */
    private String path;
    
    /**
     * 编码
     */
    private String encoding;
    
    /**
     * 分隔符
     */
    private String fieldDelimiter;
}
