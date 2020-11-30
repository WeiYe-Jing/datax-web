package com.wugui.datax.executor.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PythonUtils {

    /**
     * @param versionPath
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getPythonVersion(String versionPath)  {
        String str = null;
        try {
            String exe = "python";
            String command = versionPath;
            String[] cmdArr = new String[] {exe, command};
            Process process = Runtime.getRuntime().exec(cmdArr);
            InputStream is = process.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            str = dis.readLine();
            process.waitFor();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
