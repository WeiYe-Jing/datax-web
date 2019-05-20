package com.wugui.dataxweb.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * datax插件信息表实体类
 * @author huzekang@gz-yibo.com
 * @since 2019-05-20
 * @version v1.0
 */
@SuppressWarnings("serial")
public class DataxPlugin extends Model<DataxPlugin> {
    
    private Integer id;
    //插件类型，reader writer
    private String pluginType;
    //插件名，用作主键
    private String pluginName;
    //json模板
    private String templateJson;
    //注释
    private String comments;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPluginType() {
        return pluginType;
    }

    public void setPluginType(String pluginType) {
        this.pluginType = pluginType;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getTemplateJson() {
        return templateJson;
    }

    public void setTemplateJson(String templateJson) {
        this.templateJson = templateJson;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    }