# DataX Web增量配置说明


## 一、根据日期进行增量数据抽取


### 1.页面任务配置


打开菜单任务管理页面，选择添加任务

按下图中5个步骤进行配置

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/img/time-increment.png)

- 1.任务类型选DataX任务
- 2.辅助参数选择时间自增
- 3.增量开始时间选择，即sql中查询时间的开始时间，用户使用此选项方便第一次的全量同步。第一次同步完成后，该时间被更新为上一次的任务触发时间，任务失败不更新。
- 4.增量时间字段,-DlastTime='%s' -DcurrentTime='%s' 先来解析下这段字符串

```
1.-D是DataX参数的标识符，必配
2.-D后面的lastTime和currentTime是DataX json中where条件的时间字段标识符，必须和json中的变量名称保持一致
3.='%s'是项目用来去替换时间的占位符，比配并且格式要完全一致
4.注意-DlastTime='%s'和-DcurrentTime='%s'中间有一个空格，空格必须保留并且是一个空格
```

- 5.时间格式，可以选择自己数据库中时间的格式，也可以通过json中配置sql时间转换函数来处理

注意，注意，注意: 配置一定要仔细看文档（后面我们也会对这块配置进行优化，避免大家犯错）


### 2.JSON配置

datax.json

```json
{
  "job": {
    "setting": {
      "speed": {
        "channel": 16
      }
    },
    "content": [
      {
        "reader": {
          "name": "mysqlreader",
          "parameter": {
            "splitPk": "id",
            "username": "root",
            "password": "root",
            "column": [
              "*"

            ],
            "connection": [
              {
                
                "jdbcUrl": [
                  "jdbc:mysql://localhost:3306/test?characterEncoding=utf8"
                ],
				"querySql": [
        "select * from test_list where operationDate >= FROM_UNIXTIME(${lastTime}) and operationDate < FROM_UNIXTIME(${currentTime})"
                                ]
              }
            ]
          }
        },
        "writer": {
          "name": "mysqlwriter",
          "parameter": {
           
            "username": "root",
            "password": "123456",
            "column": [
              "*"
            ],
            "batchSize": "4096",
            "connection": [
              {
                "jdbcUrl": "jdbc:mysql://localhost:3307/test?characterEncoding=utf8",
                "table": [
                  "test_list"
                ]
              }
            ]
          }
        }
      }
    ]
  }
}
```


#### querySql解析


```
select * from test_list where operationDate >= ${lastTime} and operationDate < ${currentTime}
```

- 1.此处的关键点在${lastTime}，${currentTime}，${}是DataX动态参数的固定格式，lastTime，currentTime就是我们页面配置中
-DlastTime='%s' -DcurrentTime='%s'中的lastTime，currentTime，注意字段一定要一致。
 
- 2.如果任务配置页面，时间类型选择为时间戳但是数据库时间格式不是时间戳，例如是：2019-11-26 11:40:57 此时可以用FROM_UNIXTIME(${lastTime})进行转换。

```
select * from test_list where operationDate >= FROM_UNIXTIME(${lastTime}) and operationDate < FROM_UNIXTIME(${currentTime})
```

## 二、根据自增Id进行增量数据抽取


### 1.页面任务配置


打开菜单任务管理页面，选择添加任务

按下图中4个步骤进行配置

![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/id-increment.jpg)

- 1.任务类型选DataX任务
- 2.辅助参数选择主键自增
- 3.增量主键开始ID选择，即sql中查询ID的开始ID，用户使用此选项方便第一次的全量同步。第一次同步完成后，该ID被更新为上一次的任务触发时最大的ID，任务失败不更新。
- 4.增量时间字段,-DstartId='%s' -DendId='%s' 先来解析下这段字符串

```
1.-D是DataX参数的标识符，必配
2.-D后面的startId和endId是DataX json中where条件的id字段标识符，必须和json中的变量名称保持一致，endId是任务在每次执行时获取当前表maxId，也是下一次任务的startId
3.='%s'是项目用来去替换时间的占位符，比配并且格式要完全一致
4.注意-DstartId='%s'和-DendId='%s' 中间有一个空格，空格必须保留并且是一个空格
5.reader数据源，选择任务同步的读数据源
6.配置reader数据源中需要同步数据的表名及该表的主键
```

注意，注意，注意: 一定要仔细看文档（后续会对这块配置进行优化，避免大家犯错）


### 2.JSON配置

datax.json
```
{
   "job": {
     "setting": {
       "speed": {
         "channel": 3,
         "byte": 1048576
       },
       "errorLimit": {
         "record": 0,
         "percentage": 0.02
       }
     },
     "content": [
       {
         "reader": {
           "name": "mysqlreader",
           "parameter": {
             "username": "yRjwDFuoPKlqya9h9H2Amg==",
             "password": "yRjwDFuoPKlqya9h9H2Amg==",
             "splitPk": "",
             "connection": [
               {
                 "querySql": [
                   "select * from job_log where id>= ${startId} and id< ${endId}"
                 ],
                 "jdbcUrl": [
                   "jdbc:mysql://localhost:3306/datax_web"
                 ]
               }
             ]
           }
         },
         "writer": {
           "name": "mysqlwriter",
           "parameter": {
             "username": "mCFD+p1IMsa0rHicbQohcA==",
             "password": "PhYxJmA/nuBJD1OxKTRzZH8sxuRddOv83hdqDOVR+i0=",
             "column": [
               "`id`",
               "`job_group`",
               "`job_id`",
               "`job_desc`",
               "`executor_address`",
               "`executor_handler`",
               "`executor_param`",
               "`executor_sharding_param`",
               "`executor_fail_retry_count`",
               "`trigger_time`",
               "`trigger_code`",
               "`trigger_msg`",
               "`handle_time`",
               "`handle_code`",
               "`handle_msg`",
               "`alarm_status`",
               "`process_id`",
               "`max_id`"
             ],
             "connection": [
               {
                 "table": [
                   "job_log"
                 ],
                 "jdbcUrl": "jdbc:mysql://47.98.125.243:3306/datax_web"
               }
             ]
           }
         }
       }
     ]
   }
 }
 ```

#### querySql解析


```
select * from job_log where id>= ${startId} and id< ${endId}
```

- 1.此处的关键点在${startId}，${endId}，${}是DataX动态参数的固定格式，startId，endId就是我们页面配置中
-DstartId='%s' -DendId='%s'中的startId，endId，注意字段一定要一致。
 
 
## 三、JVM启动参数配置


此选择为非必选，可以配置DataX启动时JVM的参数，具体配置不做详解。
- JVM启动参数拼接结果为： -j "-Xms2G -Xmx2G"


## 四、常见问题

请查看issue列表或者提issue说明问题，我们会尽快回复。

https://github.com/WeiYe-Jing/datax-web/issues/198



