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
 * jdbc数据源配置实体类(job_jdbc_datasource)
 *
 * @author zhouhongfa@gz-yibo.com
 * @version v1.0
 * @since 2019-07-30
 */

@Data
@ApiModel
@TableName("job_jdbc_datasource")
public class JobJdbcDatasource extends Model<JobJdbcDatasource> {

    /**
     * 自增主键
     */
    @TableId
    @ApiModelProperty(value = "自增主键")
    private Long id;

    /**
     * 数据源名称
     */
    @ApiModelProperty(value = "数据源名称")
    private String datasourceName;

    /**
     * 数据源分组
     */
    @ApiModelProperty(value = "数据源分组")
    private String datasourceGroup;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String jdbcUsername;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String jdbcPassword;

    /**
     * jdbc url
     */
    @ApiModelProperty(value = "jdbc url")
    private String jdbcUrl;

    /**
     * jdbc驱动类
     */
    @ApiModelProperty(value = "jdbc驱动类")
    private String jdbcDriverClass;

    /**
     * 状态：0删除 1启用 2禁用
     */
    @TableLogic
    @ApiModelProperty(value = "状态：0删除 1启用 2禁用")
    private Integer status;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy/MM/dd")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createDate;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy/MM/dd")
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateDate;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", hidden = true)
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