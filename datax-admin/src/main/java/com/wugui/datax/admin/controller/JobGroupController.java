package com.wugui.datax.admin.controller;

import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.enums.RegistryConfig;
import com.wugui.datax.admin.core.util.I18nUtil;
import com.wugui.datax.admin.entity.JobGroup;
import com.wugui.datax.admin.entity.JobRegistry;
import com.wugui.datax.admin.mapper.JobGroupMapper;
import com.wugui.datax.admin.mapper.JobInfoMapper;
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
@RequestMapping("/api/jobGroup")
@Api(tags = "执行器管理接口")
public class JobGroupController {

    @Resource
    public JobInfoMapper jobInfoMapper;
    @Resource
    public JobGroupMapper jobGroupMapper;
    @Resource
    private JobRegistryMapper jobRegistryMapper;

    @GetMapping("/list")
    @ApiOperation("执行器列表")
    public ReturnT<List<JobGroup>> getExecutorList() {
        return new ReturnT<>(jobGroupMapper.findAll());
    }

    @PostMapping("/save")
    @ApiOperation("新建执行器")
    public ReturnT<String> save(@RequestBody JobGroup jobGroup) {

        // valid
        if (jobGroup.getAppName() == null || jobGroup.getAppName().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (jobGroup.getAppName().length() < 4 || jobGroup.getAppName().length() > 64) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appName_length"));
        }
        if (jobGroup.getTitle() == null || jobGroup.getTitle().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (jobGroup.getAddressType() != 0) {
            if (jobGroup.getAddressList() == null || jobGroup.getAddressList().trim().length() == 0) {
                return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresses = jobGroup.getAddressList().split(",");
            for (String item : addresses) {
                if (item == null || item.trim().length() == 0) {
                    return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_invalid"));
                }
            }
        }

        int ret = jobGroupMapper.save(jobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @PostMapping("/update")
    @ApiOperation("更新执行器")
    public ReturnT<String> update(@RequestBody JobGroup jobGroup) {
        // valid
        if (jobGroup.getAppName() == null || jobGroup.getAppName().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (jobGroup.getAppName().length() < 4 || jobGroup.getAppName().length() > 64) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appName_length"));
        }
        if (jobGroup.getTitle() == null || jobGroup.getTitle().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (jobGroup.getAddressType() == 0) {
            // 0=自动注册
            List<String> registryList = findRegistryByAppName(jobGroup.getAppName());
            String addressListStr = null;
            if (registryList != null && !registryList.isEmpty()) {
                Collections.sort(registryList);
                addressListStr = "";
                for (String item : registryList) {
                    addressListStr += item + ",";
                }
                addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
            }
            jobGroup.setAddressList(addressListStr);
        } else {
            // 1=手动录入
            if (jobGroup.getAddressList() == null || jobGroup.getAddressList().trim().length() == 0) {
                return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresses = jobGroup.getAddressList().split(",");
            for (String item : addresses) {
                if (item == null || item.trim().length() == 0) {
                    return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_invalid"));
                }
            }
        }

        int ret = jobGroupMapper.update(jobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    private List<String> findRegistryByAppName(String appNameParam) {
        HashMap<String, List<String>> appAddressMap = new HashMap<>();
        List<JobRegistry> list = jobRegistryMapper.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
        if (list != null) {
            for (JobRegistry item : list) {
                if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                    String appName = item.getRegistryKey();
                    List<String> registryList = appAddressMap.get(appName);
                    if (registryList == null) {
                        registryList = new ArrayList<>();
                    }

                    if (!registryList.contains(item.getRegistryValue())) {
                        registryList.add(item.getRegistryValue());
                    }
                    appAddressMap.put(appName, registryList);
                }
            }
        }
        return appAddressMap.get(appNameParam);
    }

    @PostMapping("/remove")
    @ApiOperation("移除执行器")
    public ReturnT<String> remove(int id) {

        // valid
        int count = jobInfoMapper.pageListCount(0, 10, id, -1, null, null, 0,null);
        if (count > 0) {
            return new ReturnT<>(500, I18nUtil.getString("jobgroup_del_limit_0"));
        }

        List<JobGroup> allList = jobGroupMapper.findAll();
        if (allList.size() == 1) {
            return new ReturnT<>(500, I18nUtil.getString("jobgroup_del_limit_1"));
        }

        int ret = jobGroupMapper.remove(id);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping(value = "/loadById", method = RequestMethod.POST)
    @ApiOperation("根据id获取执行器")
    public ReturnT<JobGroup> loadById(int id) {
        JobGroup jobGroup = jobGroupMapper.load(id);
        return jobGroup != null ? new ReturnT<>(jobGroup) : new ReturnT<>(ReturnT.FAIL_CODE, null);
    }

    @GetMapping("/query")
    @ApiOperation("查询执行器")
    public ReturnT<List<JobGroup>> get(@ApiParam(value = "执行器AppName")
                                       @RequestParam(value = "appName", required = false) String appName,
                                       @ApiParam(value = "执行器名称")
                                       @RequestParam(value = "title", required = false) String title,
                                       @ApiParam(value = "执行器地址列表")
                                       @RequestParam(value = "addressList", required = false) String addressList) {
        return new ReturnT<>(jobGroupMapper.find(appName, title, addressList));
    }

}
