package com.wugui.datax.admin.service.impl;

import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datax.admin.service.CheckEnvRunService;
import com.wugui.datax.executor.core.check.CheckRunEnvironment;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author WangAoQi
 */
@Service
public class CheckEnvServiceImpl implements CheckEnvRunService {

    @Override
    public ReturnT<Map<Boolean, String>> checkRunEnv(String envName) {
        ReturnT<Map<Boolean, String>> mapReturnT = new ReturnT<>();
        if (envName == null || "".equals(envName)) {
            mapReturnT.setCode(-1);
            return mapReturnT;
        }
        CheckRunEnvironment checkRunEnvironment = new CheckRunEnvironment();
        if ("pythonVersion".equals(envName)) {
            mapReturnT.setMsg(checkRunEnvironment.getPyVersion().toString());
            mapReturnT.setContent(checkRunEnvironment.checkPyVersionIs2X());
            return mapReturnT;
        }
        if ("dataxPath".equals(envName)) {
            mapReturnT.setMsg(checkRunEnvironment.getDataXPyPath());
            System.out.println(checkRunEnvironment.getDataXPyPath()+"--------");
            mapReturnT.setContent(checkRunEnvironment.checkDataXPATH());
            return mapReturnT;
        }
        if ("pythonHOME".equals(envName)) {
            mapReturnT.setMsg(checkRunEnvironment.getPythonHOME());
            mapReturnT.setContent(checkRunEnvironment.checkPythonHOME());
            return mapReturnT;
        }
        mapReturnT.setCode(-1);
        return mapReturnT;
    }
}
