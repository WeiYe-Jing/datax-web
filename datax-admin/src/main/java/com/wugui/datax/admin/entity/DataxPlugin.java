package com.wugui.datax.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * datax插件信息表实体类
 *
 * @author huzekang@gz-yibo.com
 * @version v1.0
 * @since 2019-05-20
 */
@TableName(value = "datax_plugin")
@Data
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