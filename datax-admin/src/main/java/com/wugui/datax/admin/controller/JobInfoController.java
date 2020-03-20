package com.wugui.datax.admin.controller;


import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.util.DateUtil;
import com.wugui.datax.admin.core.cron.CronExpression;
import com.wugui.datax.admin.core.thread.JobTriggerPoolHelper;
import com.wugui.datax.admin.core.trigger.TriggerTypeEnum;
import com.wugui.datax.admin.core.util.I18nUtil;
import com.wugui.datax.admin.dto.TriggerJobDto;
import com.wugui.datax.admin.entity.JobInfo;
import com.wugui.datax.admin.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Api(tags = "任务配置接口")
@RestController
@RequestMapping("/api/job")
public class JobInfoController {

    @Resource
    private JobService jobService;


    @GetMapping("/pageList")
    @ApiOperation("任务列表")
    public ReturnT<Map<String, Object>> pageList(@RequestParam(required = false, defaultValue = "0") int current,
                                        @RequestParam(required = false, defaultValue = "10") int size,
                                        int jobGroup, int triggerStatus, String jobDesc, String glueType, String author) {

        return new ReturnT<>(jobService.pageList((current-1)*size, size, jobGroup, triggerStatus, jobDesc, glueType, author));
    }

    @PostMapping("/add")
    @ApiOperation("添加任务")
    public ReturnT<String> add(@RequestBody JobInfo jobInfo) {
        return jobService.add(jobInfo);
    }

    @PostMapping("/update")
    @ApiOperation("更新任务")
    public ReturnT<String> update(@RequestBody JobInfo jobInfo) {
        return jobService.update(jobInfo);
    }

    @PostMapping(value = "/remove/{id}")
    @ApiOperation("移除任务")
    public ReturnT<String> remove(@PathVariable(value = "id") int id) {
        return jobService.remove(id);
    }

    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    @ApiOperation("停止任务")
    public ReturnT<String> pause(int id) {
        return jobService.stop(id);
    }

    @RequestMapping(value = "/start",method = RequestMethod.POST)
    @ApiOperation("开启任务")
    public ReturnT<String> start(int id) {
        return jobService.start(id);
    }

    @PostMapping(value = "/trigger")
    @ApiOperation("触发任务")
    public ReturnT<String> triggerJob(@RequestBody TriggerJobDto dto) {
        // force cover job param
        String executorParam=dto.getExecutorParam();
        if (executorParam == null) {
            executorParam = "";
        }
        JobTriggerPoolHelper.trigger(dto.getJobId(), TriggerTypeEnum.MANUAL, -1, null, executorParam);
        return ReturnT.SUCCESS;
    }

    @GetMapping("/nextTriggerTime")
    @ApiOperation("获取近5次触发时间")
    public ReturnT<List<String>> nextTriggerTime(String cron) {
        List<String> result = new ArrayList<>();
        try {
            CronExpression cronExpression = new CronExpression(cron);
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = cronExpression.getNextValidTimeAfter(lastTime);
                if (lastTime != null) {
                    result.add(DateUtil.formatDateTime(lastTime));
                } else {
                    break;
                }
            }
        } catch (ParseException e) {
            return new ReturnT<>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid"));
        }
        return new ReturnT<>(result);
    }
}
