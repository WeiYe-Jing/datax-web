# DataX增量动态参数抽取数据到动态参数分区

## DataX Web配置
![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/partition.png)

## DataX Json配置

datax.json

```
{
  "job": {
    "content": [
      {
        "reader": {
          "name": "mysqlreader",
          "parameter": {
            "column": [
              "*"
            ],
            "connection": [
              {
                "jdbcUrl": [
                  "jdbc:mysql://localhost:3306/order?useUnicode=true&characterEncoding=utf-8&useSSL=false&rewriteBatchedStatements=true"
                ],
                "querySql": [
                  "select * from test_order where updateTime >= FROM_UNIXTIME(${lastTime}) and operationDate < FROM_UNIXTIME(${currentTime})"
                ]
              }
            ],
            "password": "root",
            "username": "root"
          }
        },
        "writer": {
          "name": "hdfswriter",
          "parameter": {
            "defaultFS": "hdfs://localhost:9000",
            "fileType": "text",
            "path": "/user/hive/warehouse/offline.db/test_order/${partition}",
            "fileName": "test_order",
            "column": [
              {
                "name": "keyno",
                "type": "string"
              },
              {
                "name": "name",
                "type": "string"
              },
              {
                "name": "code",
                "type": "string"
              },
              {
                "name": "status",
                "type": "string"
              },
              {
                "name": "province",
                "type": "string"
              },
              {
                "name": "city",
                "type": "string"
              }
            ],
            "writeMode": "append",
            "fieldDelimiter": ","
          }
        }
      }
    ],
    "setting": {
      "speed": {
        "channel": 2
      }
    }
  }
}
```

- 增量时间字段：${lastTime}增量开始时间, 注意：定时任务启动之后，第一次的开始时间为页面输入时间，当任务执行成功后，该时间被更新为上一次的任务触发时间，任务失败不更新。${currentTime}任务的触发时间。
- 分区字段：${partition}为固定格式，不能自定义。
- 拼接结果： -p"-DlastTime=1572537600 -DcurrentTime=1579317145 -Dpartition=datety=2020-01-18"。
- JVM启动参数拼接结果为： -j "-Xms2G -Xmx2G"。

## Datax启动命令

```shell
python datax.py -j "-Xms2G -Xmx2G" -p "-DlastTime=1577009172 -DcurrentTime=1579317145 -Dpartition=datety=2020-01-18" datax.json
```

