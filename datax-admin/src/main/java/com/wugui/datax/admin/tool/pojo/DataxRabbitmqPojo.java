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
public class DataxRabbitmqPojo {
	
	/**
     * 列名
     */
    private List<Map<String, Object>> columns;

    /**
     * 数据源信息
     */
    private JobDatasource jdbcDatasource;

    /**
     * ip地址
     */
    private String host;

    /**
     * 端口
     */
    private String port;
    
    /**
     * 账号
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
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
