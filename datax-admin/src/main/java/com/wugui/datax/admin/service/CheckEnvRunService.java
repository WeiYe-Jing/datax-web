package com.wugui.datax.admin.service;

import com.wugui.datatx.core.biz.model.ReturnT;

import java.util.Map;

/**
 * 检查运行环境的service
 * @author WangAoQi
 */

public interface CheckEnvRunService {
    /**
     * 根据需要检查环境的名称  给出检查环境结果
     * @param envName
     * @return
     */
    ReturnT<Map<Boolean, String>> checkRunEnv(String envName);
}
