package com.wugui.util;

import com.sun.jna.Platform;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * ProcessUtil
 *
 * @author jingwk
 * @version 1.0
 * @since 2019/11/09
 */

@Slf4j
public class ProcessUtil {

    public static String getProcessId(Process process) {
        long pid = -1;
        Field field = null;
        if (Platform.isWindows()) {
            try {
                field = process.getClass().getDeclaredField("handle");
                field.setAccessible(true);
                pid = Kernel32.INSTANCE.GetProcessId((Long) field.get(process));
            } catch (Exception ex) {
                log.error("get process id for windows error {0}", ex);
            }
        } else if (Platform.isLinux() || Platform.isAIX()) {
            try {
                Class<?> clazz = Class.forName("java.lang.UNIXProcess");
                field = clazz.getDeclaredField("pid");
                field.setAccessible(true);
                pid = (Integer) field.get(process);
            } catch (Throwable e) {
                log.error("get process id for unix error {0}", e);
            }
        }
        return String.valueOf(pid);
    }

    /**
     * 关闭Linux进程
     *
     * @param pid 进程的PID
     */
    public static boolean killProcessByPid(String pid) {
        if (StringUtils.isEmpty(pid) || "-1".equals(pid)) {
            throw new RuntimeException("Pid ==" + pid);
        }
        Process process = null;
        BufferedReader reader = null;
        String command = "";
        boolean result = false;
        if (Platform.isWindows()) {
            command = "cmd.exe /c taskkill /PID " + pid + " /F /T ";
        } else if (Platform.isLinux() || Platform.isAIX()) {
            command = "kill -9 " + pid;
        }
        try {
            //杀掉进程
            process = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = reader.readLine()) != null) {
                log.info("kill pid return info:{}", line);
            }
            result = true;
        } catch (Exception e) {
            log.error("kill pid error {0}", e);
            result = false;
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("reader close error {0}", e);
                }
            }
        }
        return result;
    }

}
