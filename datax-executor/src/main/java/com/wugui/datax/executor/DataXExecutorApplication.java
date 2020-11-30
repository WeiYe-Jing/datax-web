package com.wugui.datax.executor;

import com.wugui.datax.executor.util.ClassPathResourceReader;
import com.wugui.datax.executor.util.PythonUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.Arrays;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
public class DataXExecutorApplication {

	public static void main(String[] args) {

		final ApplicationContext ctx  =SpringApplication.run(DataXExecutorApplication.class, args);
		Environment environment = ctx.getEnvironment();
		String path = environment.getProperty("datax.pypath");
		String python = environment.getProperty("python");
		initCofig(path);
		String pythonVersion = PythonUtils.getPythonVersion(path + python);
		if(pythonVersion.startsWith("3")){
			ClassPathResourceReader.copyDirectoryFile("datax/datax-python3",path+"/bin",Arrays.asList(),true);
		}else{
			ClassPathResourceReader.copyDirectoryFile("datax/datax-python2",path+"/bin",Arrays.asList(),true);
		}
	}
	public static void initCofig(String path){
		String confPath=path+"/datax";
		mkDir(confPath);
		ClassPathResourceReader.copyDirectoryFile("datax",path,Arrays.asList());
	}
	public static void mkDir(String confPath) {
		File file=new File(confPath);
		if(!file.exists()){
			file.mkdirs();
		}
	}
}