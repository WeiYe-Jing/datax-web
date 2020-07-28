package com.wugui.datax.executor.core.check;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 检查运行环境
 *
 * @author WangAoQi
 */
@Component
@PropertySource("classpath:application.yml")
public class CheckRunEnvironment {

    @Value("${datax.pypath}")
    private  String dataXPyPath;

    @Value("${datax.executor.pythonpath}")
    private  String pythonHOME;


    public String getPythonHOME() {
        return pythonHOME;
    }


    public String getDataXPyPath() {
        return dataXPyPath;
    }

    /**
     * 检查是否手动配置了PythonHOME
     *
     * @return
     */
    public Map<Boolean, String> checkPythonHOME() {
        Map<Boolean, String> result = new HashMap<Boolean, String>();
        if (pythonHOME == null || pythonHOME.equals("python")) {
            result.put(false, "没有手动配置pythonHOME");
            return result;
        } else {
            result.put(true, "手动配置了pythonHOME为: " + pythonHOME);
            return result;
        }
    }

    /**
     * 检查dataX的配置是否正确
     * 避免用户将PYTHON_PATH设置为python的环境变量路径
     *
     * @return k:true or false
     * v:msg
     */
    public Map<Boolean, String> checkDataXPATH() {
        Map<Boolean, String> result = new HashMap<Boolean, String>();
        if (dataXPyPath == null ||
                !dataXPyPath.endsWith("datax.py")
                || "".equals(dataXPyPath)
        ) {
            result.put(false, String.format("%S %S", "你的datax配置的可能不正确,您配置的DataX的地址为" + dataXPyPath, "应该为:$DATAX_HOME/bin/datax.py"));
            return result;
        }
        result.put(true, "您配置的DataX的地址正确!");
        return result;
    }

    /**
     * 检查python的环境是否是2.X版本
     *
     * @return true 为是2.x版本
     * false 则不是 or 检查失败
     */
    public Map<Boolean, String> checkPyVersionIs2X() {
        Map<Boolean, String> result = new HashMap<Boolean, String>();
        String pythonVersion = getPyVersion();
        //判断python版本
        if (pythonVersion != null && !"".equals(pythonVersion.toString())) {
            if (pythonVersion.toLowerCase().startsWith("python 2.") || pythonVersion.toLowerCase().startsWith("python2.")) {
                result.put(true, "python版本为2.x!");
                return result;
            } else {
                result.put(false, "python版本不是2.x!,您的python版本为: " + pythonVersion.toString());
                return result;
            }
        } else {
            result.put(false, "检查python版本失败!可能系统中无python环境...");
            return result;
        }
    }

    /**
     * 获取python的版本
     *
     * @return
     */
    public String getPyVersion() {
        Process p = null;
        String pythonVersion = null;
        try {
            p = Runtime.getRuntime().exec(new String[]{
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
}
