package com.wugui.datax.executor.util;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ClassPathResourceReader {
    public static final String CLASSPATH = "classpath:";
    private static Logger logger = LoggerFactory.getLogger(ClassPathResourceReader.class);
    /**
     * path:文件路径
     *
     * @since JDK 1.8
     */
    private final String path;

    /**
     * content:文件内容
     *
     * @since JDK 1.6
     */
    private String content;

    public ClassPathResourceReader(String path) {
        this.path = path;
    }

    public String getContent() {
        if (content == null) {
            try {
                ClassPathResource resource = new ClassPathResource(path);
                BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                content = reader.lines().collect(Collectors.joining("\n"));
                reader.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return content;
    }

    public static void copyFile(String oldFilePath, String newFilePath) {
        //生成目标文件
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(oldFilePath);
            int lastIndex = oldFilePath.lastIndexOf("/");
            String fileName = oldFilePath.substring(lastIndex);
            inputStream = classPathResource.getInputStream();
            File somethingFile = new File(newFilePath + File.separator + fileName);
            FileUtils.copyInputStreamToFile(inputStream, somethingFile);
        } catch (Exception e) {
            logger.error("出现异常，", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static void copyDirectoryFile(String oldDirectory, String targetDirectory, List<String> filterDirectory,boolean flg) {
        List<String> fileNameList = new ArrayList<>();
        try {
            getFileListame(oldDirectory, fileNameList, filterDirectory);
            for (String filePath : fileNameList) {
                copyFileByPath(filePath, targetDirectory + File.separator + filePath,flg);
            }
        } catch (FileNotFoundException e) {
            logger.error("出现异常,", e);
        }
    }

    public static void copyDirectoryFile(String oldDirectory, String targetDirectory, List<String> filterDirectory) {
        List<String> fileNameList = new ArrayList<>();
        try {
            getFileListame(oldDirectory, fileNameList, filterDirectory);
            for (String filePath : fileNameList) {
                copyFileByPath(filePath, targetDirectory + File.separator + filePath);
            }
        } catch (FileNotFoundException e) {
            logger.error("出现异常,", e);
        }
    }

    public static List<String> getFileListame(String strPath, List<String> fileNameList, List<String> filterDirectory) throws FileNotFoundException {
        String path = ResourceUtils.getURL(CLASSPATH + strPath).getPath();
        logger.info("ResourceUtils.getURL="+path);
        File dir = new File(path);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                File file = files[i];

                // 判断是文件还是文件夹
                String newPath = strPath + File.separator + file.getName();
                if (file.isDirectory()) {
                    // 获取文件绝对路径
                    if (CollectionUtils.isEmpty(filterDirectory) || !filterDirectory.contains(file.getName())) {
                        getFileListame(newPath, fileNameList, filterDirectory);
                    }
                } else {
                    fileNameList.add(newPath);
                }

            }

        }
        return fileNameList;
    }
    public static void copyFileByPath(String oldFilePath, String newFilePath,boolean flg) {
        //生成目标文件
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(oldFilePath);
            inputStream = classPathResource.getInputStream();
            File somethingFile = new File(newFilePath);
            if(!somethingFile.exists()||flg){
                logger.info("oldFilePath:{},newFilePath:{}",oldFilePath,newFilePath);
                FileUtils.copyInputStreamToFile(inputStream, somethingFile);
            }
        } catch (Exception e) {
            logger.error("出现异常，", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
    public static void copyFileByPath(String oldFilePath, String newFilePath) {
        //生成目标文件
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(oldFilePath);
            inputStream = classPathResource.getInputStream();
            File somethingFile = new File(newFilePath);
            if(!somethingFile.exists()){
                logger.info("oldFilePath:{},newFilePath:{}",oldFilePath,newFilePath);
                FileUtils.copyInputStreamToFile(inputStream, somethingFile);
            }
        } catch (Exception e) {
            logger.error("出现异常，", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        copyDirectoryFile("datax", "/Users/longkai/data/service/xyy-datax-plugins/datax", list);
    }


}

