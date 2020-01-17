package com.wugui.datax.admin.controller;

import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datax.admin.core.util.I18nUtil;
import com.wugui.datax.admin.entity.JobUser;
import com.wugui.datax.admin.mapper.JobUserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jingwk on 2019/11/17
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户信息接口")
public class UserController {

    @Resource
    private JobUserMapper jobUserMapper;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/pageList")
    @ApiOperation("用户列表")
    public ReturnT<Map<String, Object>> pageList(@RequestParam(required = false, defaultValue = "0") int current,
                                        @RequestParam(required = false, defaultValue = "10") int size,
                                        String username) {

        // page list
        List<JobUser> list = jobUserMapper.pageList((current-1)*size, size, username);
        int list_count = jobUserMapper.pageListCount((current-1)*size, size, username);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);		// 总记录数
        maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
        maps.put("data", list);  					// 分页列表
        return new ReturnT<>(maps);
    }

    @PostMapping("/add")
    @ApiOperation("添加用户")
    public ReturnT<String> add(@RequestBody JobUser jobUser) {

        // valid username
        if (!StringUtils.hasText(jobUser.getUsername())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_please_input")+I18nUtil.getString("user_username") );
        }
        jobUser.setUsername(jobUser.getUsername().trim());
        if (!(jobUser.getUsername().length()>=4 && jobUser.getUsername().length()<=20)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit")+"[4-20]" );
        }
        // valid password
        if (!StringUtils.hasText(jobUser.getPassword())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_please_input")+I18nUtil.getString("user_password") );
        }
        jobUser.setPassword(jobUser.getPassword().trim());
        if (!(jobUser.getPassword().length()>=4 && jobUser.getPassword().length()<=20)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit")+"[4-20]" );
        }
        jobUser.setPassword(bCryptPasswordEncoder.encode(jobUser.getPassword()));

        // check repeat
        JobUser existUser = jobUserMapper.loadByUserName(jobUser.getUsername());
        if (existUser != null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("user_username_repeat") );
        }

        // write
        jobUserMapper.save(jobUser);
        return ReturnT.SUCCESS;
    }

    @PostMapping(value = "/update")
    @ApiOperation("更新用户信息")
    public ReturnT<String> update(@RequestBody JobUser jobUser) {
        // valid password
        if (StringUtils.hasText(jobUser.getPassword())) {
            jobUser.setPassword(jobUser.getPassword().trim());
            if (!(jobUser.getPassword().length()>=4 && jobUser.getPassword().length()<=20)) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit")+"[4-20]" );
            }
            jobUser.setPassword(bCryptPasswordEncoder.encode(jobUser.getPassword()));
        } else {
            jobUser.setPassword(null);
        }
        // write
        jobUserMapper.update(jobUser);
        return ReturnT.SUCCESS;
    }

    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    @ApiOperation("删除用户")
    public ReturnT<String> remove(int id) {
        jobUserMapper.delete(id);
        return ReturnT.SUCCESS;
    }

    @PostMapping(value = "/updatePwd")
    @ApiOperation("修改密码")
    public ReturnT<String> updatePwd(@RequestBody JobUser jobUser){
        String password=jobUser.getPassword();
        // valid password
        if (password==null || password.trim().length()==0){
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码不可为空");
        }
        password = password.trim();
        if (!(password.length()>=4 && password.length()<=20)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit")+"[4-20]" );
        }
        // do write
        JobUser existUser = jobUserMapper.loadByUserName(jobUser.getUsername());
        existUser.setPassword(bCryptPasswordEncoder.encode(password));
        jobUserMapper.update(existUser);
        return ReturnT.SUCCESS;
    }

}
