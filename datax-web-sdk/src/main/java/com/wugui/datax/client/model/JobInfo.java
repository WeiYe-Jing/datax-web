package com.wugui.datax.client.model;

import com.wugui.datax.client.enums.ExecutorBlockStrategyEnum;
import com.wugui.datax.client.enums.ExecutorRouteStrategyEnum;
import com.wugui.datax.client.enums.GlueTypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * xxl-job info
 *
 * @author xuxueli 2016-1-12 18:25:49
 */
public class JobInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private int id;
	/**
	 * 执行器主键ID
	 */
	private int jobGroup;
	/**
	 * 任务执行CRON表达式
	 */
	private String jobCron;
	/**
	 * 任务描述
	 */
	private String jobDesc;
	/**
	 * 所属项目
	 */
	private String projectId;
	/**
	 * 创建时间
	 */
	private Date addTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 负责人
	 */
	private String author = "system";
	/**
	 * 邮件报警地址
	 */
	private String alarmEmail;
	/**
	 * 执行器路由策略
	 */
	private String executorRouteStrategy = ExecutorRouteStrategyEnum.FIRST.name();
	/**
	 * 执行器，任务Handler名称
	 */
	private String executorHandler;
	/**
	 * 执行器，任务参数
	 */
	@Deprecated
	private String executorParam;
	/**
	 * 阻塞处理策略
	 */
	private String executorBlockStrategy = ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name();
	/**
	 * 任务执行超时时间，单位秒
	 */
	private int executorTimeout;
	/**
	 * 失败重试次数
	 */
	private int executorFailRetryCount;
	/**
	 * GLUE类型
	 */
	private String glueType;
	/**
	 * 调度状态：0-停止，1-运行
	 */
	private int triggerStatus;
	/**
	 * 上次调度时间
	 */
	private long triggerLastTime;
	/**
	 * 下次调度时间
	 */
	private long triggerNextTime;

	/**
	 * 获取任务主键
	 *
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * 设置任务主键
	 *
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 获取执行器id
	 *
	 * @return
	 */
	public int getJobGroup() {
		return jobGroup;
	}

	/**
	 * 设置执行器id，暂不开放此功能；执行器id目前统一在创建JobClient时指定
	 *
	 * @param jobGroup
	 */
	public void setJobGroup(int jobGroup) {
		this.jobGroup = jobGroup;
	}

	/**
	 * 获取任务执行的CRON表达式
	 *
	 * @return
	 */
	public String getJobCron() {
		return jobCron;
	}

	/**
	 * 设置任务执行的CRON表达式
	 *
	 * @param jobCron
	 */
	public void setJobCron(String jobCron) {
		this.jobCron = jobCron;
	}

	/**
	 * 获取任务描述
	 *
	 * @return
	 */
	public String getJobDesc() {
		return jobDesc;
	}

	/**
	 * 设置任务描述
	 *
	 * @param jobDesc
	 */
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	/**
	 * 获取所属项目
	 * @return
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置所属项目
	 * @param projectId
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取任务创建时间
	 *
	 * @return
	 */
	public Date getAddTime() {
		return addTime;
	}

	/**
	 * 获取任务修改时间
	 *
	 * @return
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 获取任务负责人
	 *
	 * @return
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * 设置任务负责人，默认system
	 *
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * 获取报警邮件地址
	 *
	 * @return
	 */
	public String getAlarmEmail() {
		return alarmEmail;
	}

	/**
	 * 设置报警邮件地址
	 *
	 * @param alarmEmail
	 */
	public void setAlarmEmail(String alarmEmail) {
		this.alarmEmail = alarmEmail;
	}

	/**
	 * 获取路由策略
	 *
	 * @return
	 */
	public ExecutorRouteStrategyEnum getExecutorRouteStrategy() {
		return ExecutorRouteStrategyEnum.match(executorRouteStrategy, ExecutorRouteStrategyEnum.FIRST);
	}

	/**
	 * 设置路由策略，默认第一个
	 *
	 * @param executorRouteStrategy
	 */
	public void setExecutorRouteStrategy(ExecutorRouteStrategyEnum executorRouteStrategy) {
		this.executorRouteStrategy = executorRouteStrategy.name();
	}

	/**
	 * 获取BEAN模式运行的JobHandler
	 *
	 * @return
	 */
	public String getExecutorHandler() {
		return executorHandler;
	}

	/**
	 * 设置BEAN模式运行的JobHandler
	 *
	 * @param executorHandler
	 */
	public void setExecutorHandler(String executorHandler) {
		this.executorHandler = executorHandler;
	}

	/**
	 * 获取运行参数
	 *
	 * @return
	 */
	public String getExecutorParam() {
		return executorParam;
	}

	/**
	 * 设置运行参数
	 *
	 * @param executorParam
	 */
	public void setExecutorParam(String executorParam) {
		this.executorParam = executorParam;
	}

	/**
	 * 获取阻塞处理策略
	 *
	 * @return
	 */
	public ExecutorBlockStrategyEnum getExecutorBlockStrategy() {
		return ExecutorBlockStrategyEnum.match(executorBlockStrategy, ExecutorBlockStrategyEnum.SERIAL_EXECUTION);
	}

	/**
	 * 设置阻塞处理策略，默认单机串行
	 *
	 * @param executorBlockStrategy
	 */
	public void setExecutorBlockStrategy(ExecutorBlockStrategyEnum executorBlockStrategy) {
		this.executorBlockStrategy = executorBlockStrategy.name();
	}

	/**
	 * 获取任务超时时间
	 *
	 * @return
	 */
	public int getExecutorTimeout() {
		return executorTimeout;
	}

	/**
	 * 设置任务超时时间，默认0，即不设置超时
	 *
	 * @param executorTimeout
	 */
	public void setExecutorTimeout(int executorTimeout) {
		this.executorTimeout = executorTimeout;
	}

	/**
	 * 获取失败重试次数
	 *
	 * @return
	 */
	public int getExecutorFailRetryCount() {
		return executorFailRetryCount;
	}

	/**
	 * 设置失败重试次数，默认0，即不重试
	 *
	 * @param executorFailRetryCount
	 */
	public void setExecutorFailRetryCount(int executorFailRetryCount) {
		this.executorFailRetryCount = executorFailRetryCount;
	}

	/**
	 * 获取任务运行模式，SDK仅提供BEAN模式
	 *
	 * @return
	 */
	public String getGlueType() {
		return glueType;
	}

	/**
	 * 获取调度状态：0-停止，1-运行
	 *
	 * @return
	 */
	public int getTriggerStatus() {
		return triggerStatus;
	}

	/**
	 * 获取上次调度时间
	 *
	 * @return
	 */
	public long getTriggerLastTime() {
		return triggerLastTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setExecutorRouteStrategy(String executorRouteStrategy) {
		this.executorRouteStrategy = executorRouteStrategy;
	}

	public void setExecutorBlockStrategy(String executorBlockStrategy) {
		this.executorBlockStrategy = executorBlockStrategy;
	}

	public void setGlueType(String glueType) {
		this.glueType = glueType;
	}

	public void setTriggerStatus(int triggerStatus) {
		this.triggerStatus = triggerStatus;
	}

	public void setTriggerLastTime(long triggerLastTime) {
		this.triggerLastTime = triggerLastTime;
	}

	public void setTriggerNextTime(long triggerNextTime) {
		this.triggerNextTime = triggerNextTime;
	}

	/**
	 * 获取下次调度时间
	 *
	 * @return
	 */
	public long getTriggerNextTime() {
		return triggerNextTime;
	}

	@Override
	public String toString() {
		return "JobInfo [id=" + id + ", jobGroup=" + jobGroup + ", jobCron=" + jobCron + ", jobDesc=" + jobDesc
				+ ", addTime=" + addTime + ", updateTime=" + updateTime + ", author=" + author + ", alarmEmail="
				+ alarmEmail + ", executorRouteStrategy=" + executorRouteStrategy + ", executorHandler="
				+ executorHandler + ", executorParam=" + executorParam + ", executorBlockStrategy="
				+ executorBlockStrategy + ", executorTimeout=" + executorTimeout + ", executorFailRetryCount="
				+ executorFailRetryCount + ", glueType=" + glueType + ", triggerStatus=" + triggerStatus
				+ ", triggerLastTime=" + triggerLastTime + ", triggerNextTime=" + triggerNextTime + "]";
	}
}
