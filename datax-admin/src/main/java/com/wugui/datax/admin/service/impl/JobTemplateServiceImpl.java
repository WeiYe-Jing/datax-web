package com.wugui.datax.admin.service.impl;

import com.google.common.collect.Maps;
import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.enums.ExecutorBlockStrategyEnum;
import com.wugui.datatx.core.glue.GlueTypeEnum;
import com.wugui.datax.admin.core.cron.CronExpression;
import com.wugui.datax.admin.core.route.ExecutorRouteStrategyEnum;
import com.wugui.datax.admin.core.util.I18nUtil;
import com.wugui.datax.admin.entity.JobGroup;
import com.wugui.datax.admin.entity.JobTemplate;
import com.wugui.datax.admin.mapper.*;
import com.wugui.datax.admin.service.JobTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * core job action for xxl-job
 * @author xuxueli 2016-5-28 15:30:33
 */
@Service
public class JobTemplateServiceImpl implements JobTemplateService {
	private static Logger logger = LoggerFactory.getLogger(JobTemplateServiceImpl.class);

	@Resource
	private JobGroupMapper jobGroupMapper;
	@Resource
	private JobTemplateMapper jobTemplateMapper;
	@Resource
	private JobLogMapper jobLogMapper;
	@Resource
	private JobLogGlueMapper jobLogGlueMapper;
	@Resource
	private JobLogReportMapper jobLogReportMapper;

	private final static ConcurrentMap<String, String> jobTmpFiles = Maps.newConcurrentMap();
	
	@Override
	public Map<String, Object> pageList(int start, int length, int jobGroup, String jobDesc, String executorHandler, String author) {

		// page list
		List<JobTemplate> list = jobTemplateMapper.pageList(start, length, jobGroup, jobDesc, executorHandler, author);
		int list_count = jobTemplateMapper.pageListCount(start, length, jobGroup, jobDesc, executorHandler, author);
		
		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
	    maps.put("recordsTotal", list_count);		// 总记录数
	    maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
	    maps.put("data", list);  					// 分页列表
		return maps;
	}

	@Override
	public ReturnT<String> add(JobTemplate jobTemplate) {
		// valid
		JobGroup group = jobGroupMapper.load(jobTemplate.getJobGroup());
		if (group == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_choose")+I18nUtil.getString("jobinfo_field_jobgroup")) );
		}
		if (!CronExpression.isValidExpression(jobTemplate.getJobCron())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid") );
		}
		if (jobTemplate.getJobDesc()==null || jobTemplate.getJobDesc().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_jobdesc")) );
		}
		if (jobTemplate.getAuthor()==null || jobTemplate.getAuthor().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_author")) );
		}
		if (ExecutorRouteStrategyEnum.match(jobTemplate.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (ExecutorBlockStrategyEnum.match(jobTemplate.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (GlueTypeEnum.match(jobTemplate.getGlueType()) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_gluetype")+I18nUtil.getString("system_unvalid")) );
		}
		if (GlueTypeEnum.BEAN==GlueTypeEnum.match(jobTemplate.getGlueType()) && (jobTemplate.getExecutorHandler()==null || jobTemplate.getExecutorHandler().trim().length()==0) ) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+"JobHandler") );
		}

		// fix "\r" in shell
		if (GlueTypeEnum.GLUE_SHELL==GlueTypeEnum.match(jobTemplate.getGlueType()) && jobTemplate.getGlueSource()!=null) {
			jobTemplate.setGlueSource(jobTemplate.getGlueSource().replaceAll("\r", ""));
		}

		// ChildJobId valid
        if (jobTemplate.getChildJobId()!=null && jobTemplate.getChildJobId().trim().length()>0) {
			String[] childJobIds = jobTemplate.getChildJobId().split(",");
			for (String childJobIdItem: childJobIds) {
				if (childJobIdItem!=null && childJobIdItem.trim().length()>0 && isNumeric(childJobIdItem)) {
					JobTemplate childJobTemplate = jobTemplateMapper.loadById(Integer.parseInt(childJobIdItem));
					if (childJobTemplate==null) {
						return new ReturnT<String>(ReturnT.FAIL_CODE,
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_not_found")), childJobIdItem));
					}
				} else {
					return new ReturnT<String>(ReturnT.FAIL_CODE,
							MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_unvalid")), childJobIdItem));
				}
			}

			// join , avoid "xxx,,"
			String temp = "";
			for (String item:childJobIds) {
				temp += item + ",";
			}
			temp = temp.substring(0, temp.length()-1);

			jobTemplate.setChildJobId(temp);
		}

		// add in db
		jobTemplate.setAddTime(new Date());
		jobTemplate.setUpdateTime(new Date());
		jobTemplate.setGlueUpdatetime(new Date());
		jobTemplateMapper.save(jobTemplate);
		if (jobTemplate.getId() < 1) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_add")+I18nUtil.getString("system_fail")) );
		}

		return new ReturnT<String>(String.valueOf(jobTemplate.getId()));
	}

	private boolean isNumeric(String str){
		try {
			int result = Integer.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public ReturnT<String> update(JobTemplate jobTemplate) {

		// valid
		if (!CronExpression.isValidExpression(jobTemplate.getJobCron())) {
			return new ReturnT<>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid"));
		}
		if (jobTemplate.getJobDesc()==null || jobTemplate.getJobDesc().trim().length()==0) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_jobdesc")));
		}
		if (jobTemplate.getAuthor()==null || jobTemplate.getAuthor().trim().length()==0) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_author")));
		}
		if (ExecutorRouteStrategyEnum.match(jobTemplate.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy") + I18nUtil.getString("system_unvalid")));
		}
		if (ExecutorBlockStrategyEnum.match(jobTemplate.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy") + I18nUtil.getString("system_unvalid")));
		}

		// ChildJobId valid
        if (jobTemplate.getChildJobId()!=null && jobTemplate.getChildJobId().trim().length()>0) {
			String[] childJobIds = jobTemplate.getChildJobId().split(",");
			for (String childJobIdItem: childJobIds) {
				if (childJobIdItem!=null && childJobIdItem.trim().length()>0 && isNumeric(childJobIdItem)) {
					JobTemplate childJobTemplate = jobTemplateMapper.loadById(Integer.parseInt(childJobIdItem));
					if (childJobTemplate==null) {
						return new ReturnT<String>(ReturnT.FAIL_CODE,
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_not_found")), childJobIdItem));
					}
				} else {
					return new ReturnT<String>(ReturnT.FAIL_CODE,
							MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_unvalid")), childJobIdItem));
				}
			}

			// join , avoid "xxx,,"
			String temp = "";
			for (String item:childJobIds) {
				temp += item + ",";
			}
			temp = temp.substring(0, temp.length()-1);

			jobTemplate.setChildJobId(temp);
		}

		// group valid
		JobGroup jobGroup = jobGroupMapper.load(jobTemplate.getJobGroup());
		if (jobGroup == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_jobgroup")+I18nUtil.getString("system_unvalid")) );
		}

		// stage job info
		JobTemplate exists_jobTemplate = jobTemplateMapper.loadById(jobTemplate.getId());
		if (exists_jobTemplate == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_id")+I18nUtil.getString("system_not_found")) );
		}

		// next trigger time (5s后生效，避开预读周期)
		long nextTriggerTime = exists_jobTemplate.getTriggerNextTime();

		exists_jobTemplate.setJobGroup(jobTemplate.getJobGroup());
		exists_jobTemplate.setJobCron(jobTemplate.getJobCron());
		exists_jobTemplate.setJobDesc(jobTemplate.getJobDesc());
		exists_jobTemplate.setAuthor(jobTemplate.getAuthor());
		exists_jobTemplate.setAlarmEmail(jobTemplate.getAlarmEmail());
		exists_jobTemplate.setExecutorRouteStrategy(jobTemplate.getExecutorRouteStrategy());
		exists_jobTemplate.setExecutorHandler(jobTemplate.getExecutorHandler());
		exists_jobTemplate.setExecutorParam(jobTemplate.getExecutorParam());
		exists_jobTemplate.setExecutorBlockStrategy(jobTemplate.getExecutorBlockStrategy());
		exists_jobTemplate.setExecutorTimeout(jobTemplate.getExecutorTimeout());
		exists_jobTemplate.setExecutorFailRetryCount(jobTemplate.getExecutorFailRetryCount());
		exists_jobTemplate.setChildJobId(jobTemplate.getChildJobId());
		exists_jobTemplate.setTriggerNextTime(nextTriggerTime);
		exists_jobTemplate.setJobJson(jobTemplate.getJobJson());
		exists_jobTemplate.setReplaceParam(jobTemplate.getReplaceParam());
		exists_jobTemplate.setJvmParam(jobTemplate.getJvmParam());
		exists_jobTemplate.setIncStartTime(jobTemplate.getIncStartTime());
		exists_jobTemplate.setPartitionInfo(jobTemplate.getPartitionInfo());
		exists_jobTemplate.setUpdateTime(new Date());
		jobTemplateMapper.update(exists_jobTemplate);


		return ReturnT.SUCCESS;
	}

	@Override
	public ReturnT<String> remove(int id) {
		JobTemplate xxlJobTemplate = jobTemplateMapper.loadById(id);
		if (xxlJobTemplate == null) {
			return ReturnT.SUCCESS;
		}

		jobTemplateMapper.delete(id);
		jobLogMapper.delete(id);
		jobLogGlueMapper.deleteByJobId(id);
		return ReturnT.SUCCESS;
	}

}
