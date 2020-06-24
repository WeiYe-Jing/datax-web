package com.wugui.datax.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * xxl-job info
 *
 * @author jingwk  2019-11-17 14:25:49
 */
@Data
public class JobInfo {

	@ApiModelProperty("主键ID")
	private int id;

	@ApiModelProperty("执行器主键ID")
	private int jobGroup;

	@ApiModelProperty("任务执行CRON表达式")
	private String jobCron;

	@ApiModelProperty("排序")
	private String jobDesc;

	private Date addTime;

	private Date updateTime;

	@ApiModelProperty("修改用户")
	private int userId;

	@ApiModelProperty("报警邮件")
	private String alarmEmail;

	@ApiModelProperty("执行器路由策略")
	private String executorRouteStrategy;

	@ApiModelProperty("执行器，任务Handler名称")
	private String executorHandler;

	@ApiModelProperty("执行器，任务参数")
	private String executorParam;

	@ApiModelProperty("阻塞处理策略")
	private String executorBlockStrategy;

	@ApiModelProperty("任务执行超时时间，单位秒")
	private int executorTimeout;

	@ApiModelProperty("失败重试次数")
	private int executorFailRetryCount;

	@ApiModelProperty("GLUE类型\t#com.wugui.datatx.core.glue.GlueTypeEnum")
	private String glueType;

	@ApiModelProperty("GLUE源代码")
	private String glueSource;

	@ApiModelProperty("GLUE备注")
	private String glueRemark;

	@ApiModelProperty("GLUE更新时间")
	private Date glueUpdatetime;

	@ApiModelProperty("子任务ID")
	private String childJobId;

	@ApiModelProperty("调度状态：0-停止，1-运行")
	private int triggerStatus;

	@ApiModelProperty("上次调度时间")
	private long triggerLastTime;

	@ApiModelProperty("下次调度时间")
	private long triggerNextTime;

	@ApiModelProperty("datax运行json")
	private String jobJson;

	@ApiModelProperty("脚本动态参数")
	private String replaceParam;

	@ApiModelProperty("增量日期格式")
	private String replaceParamType;

	@ApiModelProperty("jvm参数")
	private String jvmParam;

	@ApiModelProperty("增量初始时间")
	private Date incStartTime;

	@ApiModelProperty("分区信息")
	private String partitionInfo;

	@ApiModelProperty("最近一次执行状态")
	private int lastHandleCode;

	@ApiModelProperty("所属项目Id")
	private int projectId;

	@ApiModelProperty("主键字段")
	private String primaryKey;

	@ApiModelProperty("增量初始id")
	private Long incStartId;

	@ApiModelProperty("增量方式")
	private int incrementType;

	@ApiModelProperty("datax的读表")
	private  String readerTable;

	@ApiModelProperty("数据源id")
	private int datasourceId;

	@TableField(exist=false)
	private String projectName;

	@TableField(exist=false)
	private String userName;
}
