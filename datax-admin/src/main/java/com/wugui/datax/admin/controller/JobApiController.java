package com.wugui.datax.admin.controller;

import com.wugui.datatx.core.biz.AdminBiz;
import com.wugui.datatx.core.biz.model.HandleCallbackParam;
import com.wugui.datatx.core.biz.model.HandleProcessCallbackParam;
import com.wugui.datatx.core.biz.model.RegistryParam;
import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.util.JobRemotingUtil;
import com.wugui.datax.admin.core.conf.JobAdminConfig;
import com.wugui.datax.admin.core.util.JacksonUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xuxueli on 17/5/10.
 */
@RestController
@RequestMapping("/api")
public class JobApiController {

    @Resource
    private AdminBiz adminBiz;

    /**
     * callback
     *
     * @param data
     * @return
     */
    @RequestMapping("/callback")
    public ReturnT<String> callback(HttpServletRequest request, @RequestBody(required = false) String data) {
        // valid
        if (JobAdminConfig.getAdminConfig().getAccessToken()!=null
                && JobAdminConfig.getAdminConfig().getAccessToken().trim().length()>0
                && !JobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(JobRemotingUtil.XXL_RPC_ACCESS_TOKEN))) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }

        // param
        List<HandleCallbackParam> callbackParamList = null;
        try {
            callbackParamList = JacksonUtil.readValue(data, List.class, HandleCallbackParam.class);
        } catch (Exception e) { }
        if (callbackParamList==null || callbackParamList.size()==0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The request data invalid.");
        }

        // invoke
        return adminBiz.callback(callbackParamList);
    }

    /**
     * callback
     *
     * @param data
     * @return
     */
    @RequestMapping("/processCallback")
    public ReturnT<String> processCallback(HttpServletRequest request, @RequestBody(required = false) String data) {
        // valid
        if (JobAdminConfig.getAdminConfig().getAccessToken()!=null
                && JobAdminConfig.getAdminConfig().getAccessToken().trim().length()>0
                && !JobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(JobRemotingUtil.XXL_RPC_ACCESS_TOKEN))) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }

        // param
        List<HandleProcessCallbackParam> callbackParamList = null;
        try {
            callbackParamList = JacksonUtil.readValue(data, List.class, HandleProcessCallbackParam.class);
        } catch (Exception e) { }
        if (callbackParamList==null || callbackParamList.size()==0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The request data invalid.");
        }

        // invoke
        return adminBiz.processCallback(callbackParamList);
    }



    /**
     * registry
     *
     * @param data
     * @return
     */
    @RequestMapping("/registry")
    public ReturnT<String> registry(HttpServletRequest request, @RequestBody(required = false) String data) {
        // valid
        if (JobAdminConfig.getAdminConfig().getAccessToken()!=null
                && JobAdminConfig.getAdminConfig().getAccessToken().trim().length()>0
                && !JobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(JobRemotingUtil.XXL_RPC_ACCESS_TOKEN))) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }

        // param
        RegistryParam registryParam = null;
        try {
            registryParam = JacksonUtil.readValue(data, RegistryParam.class);
        } catch (Exception e) {}
        if (registryParam == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "The request data invalid.");
        }

        // invoke
        return adminBiz.registry(registryParam);
    }

    /**
     * registry remove
     *
     * @param data
     * @return
     */
    @RequestMapping("/registryRemove")
    public ReturnT<String> registryRemove(HttpServletRequest request, @RequestBody(required = false) String data) {
        // valid
        if (JobAdminConfig.getAdminConfig().getAccessToken()!=null
                && JobAdminConfig.getAdminConfig().getAccessToken().trim().length()>0
                && !JobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(JobRemotingUtil.XXL_RPC_ACCESS_TOKEN))) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }

        // param
        RegistryParam registryParam = null;
        try {
            registryParam = JacksonUtil.readValue(data, RegistryParam.class);
        } catch (Exception e) {}
        if (registryParam == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The request data invalid.");
        }

        // invoke
        return adminBiz.registryRemove(registryParam);
    }


}
