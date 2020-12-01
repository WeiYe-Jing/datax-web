package com.wugui.datax.executor.service.jobhandler;

import com.wugui.datatx.core.log.JobLogger;
import com.wugui.datatx.core.util.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class CheckEnv {

    /**
     * 检查运行环境
     * 打印到日志中
     */
    public static void checkEnv(String dataXPyPath,String pythonPath) {
        String checkPythonHOME = checkPythonPath(pythonPath);
        JobLogger.log("------------------" + checkPythonHOME);
        String checkDataXPATH = checkDataXPATH(dataXPyPath);
        JobLogger.log("------------------" + checkDataXPATH);
        String checkPyVersionIs2X = checkPyVersionIs2X();
        JobLogger.log("------------------" + (checkPyVersionIs2X));
    }

    /**
     * 检查dataX的配置是否正确
     * 避免用户将PYTHON_PATH设置为python的环境变量路径
     *
     * @return k:true or false
     * v:msg
     */
    public static String checkDataXPATH(String dataXPyPath) {
        //兼容只配置 DataX_HOME 情况
        if (!dataXPyPath.endsWith("datax.py") || StringUtils.isBlank(dataXPyPath)) {
            return String.format("%S %S", "datax执行文件配置可能不正确,配置的DataX的地址为" + dataXPyPath, "应该为:$DATAX_HOME/bin/datax.py");
        }
        return Constants.STRING_BLANK;
    }

    /**
     * 检查python的环境是否是2.X版本
     *
     * @return true 为是2.x版本
     * false 则不是 or 检查失败
     */
    public static String checkPyVersionIs2X() {
        String pythonVersion = getPyVersion();
        //判断python版本
        if (StringUtils.isNotBlank(pythonVersion)) {
            if (pythonVersion.toLowerCase().startsWith("python 2.") || pythonVersion.toLowerCase().startsWith("python2.")) {
                return "python版本为2.x!";
            } else {
                return "python版本不是2.x!,您的python版本为: " + pythonVersion;
            }
        } else {
            return "检查python版本失败!可能系统中无python环境...";
        }
    }

    /**
     * 获取python的版本
     *
     * @return
     */
    public static String getPyVersion() {
        String pythonVersion = null;
        try {
            Process p = Runtime.getRuntime().exec(new String[]{
                    "python", "--version"
            });
            try (
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            ) {
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                while ((line = br2.readLine()) != null) {
                    sb.append(line);
                }
                pythonVersion = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pythonVersion;
    }


    /**
     * 检查是否手动配置了pythonPath
     *
     * @return
     */
    public static String checkPythonPath(String pythonPath) {
        if (pythonPath == null || pythonPath.equals("python")) {
            return "没有手动配置pythonPath";
        } else {
            return "手动配置了pythonPath为: " + pythonPath;
        }
    }
}
