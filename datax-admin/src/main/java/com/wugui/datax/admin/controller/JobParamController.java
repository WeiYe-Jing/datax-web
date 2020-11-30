package com.wugui.datax.admin.controller;

import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.enums.RegistryConfig;
import com.wugui.datax.admin.core.util.I18nUtil;
import com.wugui.datax.admin.entity.JobGroup;
import com.wugui.datax.admin.entity.JobParam;
import com.wugui.datax.admin.entity.JobRegistry;
import com.wugui.datax.admin.mapper.JobGroupMapper;
import com.wugui.datax.admin.mapper.JobInfoMapper;
import com.wugui.datax.admin.mapper.JobParamMapper;
import com.wugui.datax.admin.mapper.JobRegistryMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jingwk on 2019/11/17
 */
@RestController
@RequestMapping("/api/param")
@Api(tags = "执行器管理接口")
public class JobParamController {

    @Resource
    public JobParamMapper jobParamMapper;

    @RequestMapping(value = "/loadParamById", method = RequestMethod.POST)
    @ApiOperation("根据id获取执行器")
    public ReturnT<List<JobParam>> loadParamById(int id) {
        List<JobParam> jobParams = jobParamMapper.selectByJobId(id);
        return jobParams != null ? new ReturnT<>(jobParams) : new ReturnT<>(ReturnT.FAIL_CODE, null);
    }

}
