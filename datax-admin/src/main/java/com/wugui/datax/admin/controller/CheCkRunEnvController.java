package com.wugui.datax.admin.controller;


import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datax.admin.service.CheckEnvRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 检查运行环境接口
 *
 * @author WangAoQi
 */
@RestController
@RequestMapping("/api")
public class CheCkRunEnvController {

    @Autowired
    CheckEnvRunService checkEnvRunService;


    /**
     * 检查环境api
     *
     * @return
     */
    @GetMapping("/checkEnv")
    public ReturnT<Map<Boolean, String>> dataXPath(@RequestParam(required = true) String envName) {
        return checkEnvRunService.checkRunEnv(envName);
    }
}
