package com.wugui.dataxweb.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作业配置表实体类(job_config)
 *
 * @author zhouhongfa@gz-yibo.com
 * @version v1.0
 * @since 2019-06-17
 */

@Data
@ApiModel
@TableName("job_config")
public class JobConfig extends Model<JobConfig> {

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer userId;

    /**
     * 作业名
     */
    @ApiModelProperty(value = "作业名")
    private String name;

    /**
     * 分组
     */
    @ApiModelProperty(value = "分组")
    private String jobGroup;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private String configJson;

    /**
     * 作业描述信息
     */
    @ApiModelProperty(value = "作业描述信息")
    private String description;

    /**
     * 标签（读插件、写插件)
     */
    @ApiModelProperty(value = "标签（读插件、写插件)")
    private String label;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy/MM/dd")
    @ApiModelProperty(value = "", hidden = true)
    private Date updateDate;

    /**
     *
     */
    @TableLogic
    @ApiModelProperty(value = "", hidden = true)
    private Integer status;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer createBy;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy/MM/dd")
    @ApiModelProperty(value = "", hidden = true)
    private Date createDate;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer updateBy;


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