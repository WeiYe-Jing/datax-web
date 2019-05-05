## prepare
only  Java环境

## introduction
- 使用springboot启动datax，不再需要用python启动。
- 以restful接口启动datax作业

## how to run
###1.   在父工程目录下使用maven打包
```
 mvn -U clean package assembly:assembly -Dmaven.test.skip=true 
```

###2. 在打包完成的target目录下进入datax-web，可以看到datax-web-0.0.1-SNAPSHOT
```
cd  datax/datax/plugin/web
```

###3.  运行启动命令
```
 java  -Ddatax.home=/Users/huzekang/openSource/DataX/target/datax/datax  -jar datax-web-0.0.1-SNAPSHOT.jar
```
需要配上环境变量-Ddatax.home，此处参照上述配置mvn打包后的目录即可