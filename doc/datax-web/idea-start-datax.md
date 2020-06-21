![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/idea_start_datax.png)

## vm option
需要写上你用maven打包后生成的target目录
``` 
 -Ddatax.home=/Users/huzekang/openSource/DataX/target/datax/datax
```
## program args
```
-mode standalone -jobid -1 -job /Users/huzekang/openSource/DataX/job-sample/oracle2mysql.json
```