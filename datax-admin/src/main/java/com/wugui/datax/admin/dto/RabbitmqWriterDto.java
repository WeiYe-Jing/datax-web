package com.wugui.datax.admin.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 构建rabbitmq write dto
 * @author jiangdw
 *
 */
@Data
public class RabbitmqWriterDto implements Serializable {

	/**
     * 列名
     */
    private List<String> columns;

    /**
     * 端口
     */
    private String port;
    
    /**
     * exchange
     */
    private String exchange;
    
    /**
     * vhost
     */
    private String vhost;
    
    /**
     * 队列名称
     */
    private String queue;
    
    /**
     * routing key
     */
    private String routingKey;
    
    /**
     * 批量push到mq的条数
     */
    private Integer batchSize;
    
    /**
     * 是否将字段按照间隔符号拼接成字符串push到mq
     */
    private Boolean jointColumn;
    
    /**
     * 拼接后字符串的前缀，配合jointColumn使用
     */
    private String messagePrefix;
    
    /**
     * 拼接后字符串的后缀，配合jointColumn使用
     */
    private String messageSuffix;
    
    /**
     * 拼接后字符串的间隔符号，配合jointColumn使用
     */
    private String fieldDelimiter;
	
}
