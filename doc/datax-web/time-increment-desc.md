# 根据操作日期进行增量抽取

## datax作业配置文件

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

- 增量时间字段：${lastTime}增量开始时间, 注意：定时任务启动之后，第一次的开始时间为页面输入时间，当任务执行成功后，该时间被更新为上一次的任务触发时间，任务失败不更新。${currentTime}任务的触发时间
- 拼接结果： -p "-DlastTime=1577009172 -DcurrentTime=1577023572"
- JVM启动参数拼接结果为： -j "-Xms2G -Xmx2G"
## Demo
![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/add_job_param.png)
## Datax启动命令

```shell
python datax.py -j "-Xms2G -Xmx2G" -p "-DlastTime=1577009172 -DcurrentTime=1577023572" datax.json
```

