package com.wugui.datax.admin.controller;

import cn.hutool.core.util.StrUtil;
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
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wugui.datatx.core.biz.model.ReturnT.FAIL_CODE;

/**
 * @author jingwk on 2019/11/17
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户信息接口")
public class UserController extends BaseController {

    @Resource
    private JobUserMapper jobUserMapper;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/pageList")
    @ApiOperation("用户列表")
    public ReturnT<Map<String, Object>> pageList(@RequestParam(required = false, defaultValue = "1") int current,
                                                 @RequestParam(required = false, defaultValue = "10") int size,
                                                 String username) {

        List<JobUser> list = jobUserMapper.pageList((current - 1) * size, size, username);
        int recordsTotal = jobUserMapper.pageListCount((current - 1) * size, size, username);

        Map<String, Object> maps = new HashMap<>(3);
        maps.put("recordsTotal", recordsTotal);
        maps.put("recordsFiltered", recordsTotal);
        maps.put("data", list);
        return new ReturnT<>(maps);
    }

    @GetMapping("/list")
    @ApiOperation("用户列表")
    public ReturnT<List<JobUser>> list(String username) {
        return new ReturnT<>(jobUserMapper.findAll(username));
    }

    @GetMapping("/getUserById")
    @ApiOperation(value = "根据id获取用户")
    public ReturnT<JobUser> selectById(@RequestParam("userId") Integer userId) {
        return new ReturnT<>(jobUserMapper.getUserById(userId));
    }

    @PostMapping("/add")
    @ApiOperation("添加用户")
    public ReturnT<String> add(HttpServletRequest request, @RequestBody JobUser jobUser) {

        int userId = getCurrentUserId(request);
        JobUser user = jobUserMapper.getUserById(userId);
        String admin = "ADMIN";
        if (admin.equals(user.getRole())) {
            return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_permission_limit"));
        }
        // valid username
        if (!StringUtils.hasText(jobUser.getUsername())) {
            return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_please_input") + I18nUtil.getString("user_username"));
        }
        jobUser.setUsername(jobUser.getUsername().trim());
        if (!(jobUser.getUsername().length() >= 4 && jobUser.getUsername().length() <= 20)) {
            return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_length_limit") + "[4-20]");
        }
        // valid password
        if (!StringUtils.hasText(jobUser.getPassword())) {
            return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_please_input") + I18nUtil.getString("user_password"));
        }
        jobUser.setPassword(jobUser.getPassword().trim());
        if (!(jobUser.getPassword().length() >= 4 && jobUser.getPassword().length() <= 20)) {
            return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_length_limit") + "[4-20]");
        }
        jobUser.setPassword(bCryptPasswordEncoder.encode(jobUser.getPassword()));


        // check repeat
        JobUser existUser = jobUserMapper.loadByUserName(jobUser.getUsername());
        if (existUser != null) {
            return new ReturnT<>(FAIL_CODE, I18nUtil.getString("user_username_repeat"));
        }

        // write
        jobUserMapper.save(jobUser);
        return ReturnT.SUCCESS;
    }

    @PostMapping(value = "/update")
    @ApiOperation("更新用户信息")
    public ReturnT<String> update(HttpServletRequest request, @RequestBody JobUser jobUser) {
        int userId = getCurrentUserId(request);
        JobUser user = jobUserMapper.getUserById(userId);
        if ("ADMIN".equals(user.getRole())) {
            return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_permission_limit"));
        }
        if (StringUtils.hasText(jobUser.getPassword())) {
            String pwd = jobUser.getPassword().trim();
            if (StrUtil.isBlank(pwd)) {
                return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_no_blank") + "密码");
            }

            if (!(pwd.length() >= 4 && pwd.length() <= 20)) {
                return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_length_limit") + "[4-20]");
            }
            jobUser.setPassword(bCryptPasswordEncoder.encode(pwd));
        } else {
            return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_no_blank") + "密码");
        }
        // write
        jobUserMapper.update(jobUser);
        return ReturnT.SUCCESS;
    }

    @PostMapping("/remove")
    @ApiOperation("删除用户")
    public ReturnT<String> remove(int id) {
        int result = jobUserMapper.delete(id);
        return result != 1 ? ReturnT.FAIL : ReturnT.SUCCESS;
    }

    @PostMapping(value = "/updatePwd")
    @ApiOperation("修改密码")
    public ReturnT<String> updatePwd(@RequestBody JobUser jobUser) {
        String password = jobUser.getPassword();
        if (password == null || password.trim().length() == 0) {
            return new ReturnT<>(ReturnT.FAIL.getCode(), "密码不可为空");
        }
        password = password.trim();
        int pwdShortLen = 4, pwdLongLen = 20;
        if (!(password.length() >= pwdShortLen && password.length() <= pwdLongLen)) {
            return new ReturnT<>(FAIL_CODE, I18nUtil.getString("system_length_limit") + "[4-20]");
        }
        // do write
        JobUser existUser = jobUserMapper.loadByUserName(jobUser.getUsername());
        existUser.setPassword(bCryptPasswordEncoder.encode(password));
        jobUserMapper.update(existUser);
        return ReturnT.SUCCESS;
    }

}
