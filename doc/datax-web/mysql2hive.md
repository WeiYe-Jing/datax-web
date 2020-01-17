# mysql2hive抽取

## datax作业配置文件

datax.json

```json
{
  "job": {
    "content": [
      {
        "reader": {
          "name": "mysqlreader",
          "parameter": {
            "connection": [
              {
                "jdbcUrl": [
                  "jdbc:mysql://127.0.0.1:3306/manual_order?useUnicode=true&characterEncoding=utf-8&useSSL=false&rewriteBatchedStatements=true"
                ],
                "querySql": [
                  "select no,name,code,status,province,city,industy,score from test where id < 1000000"
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
            "path": "/user/hive/warehouse/offline.db/fgw_company_evaluate_gg/datety=2019-12-22",
            "fileName": "test",
            "column": [
              {
                "name": "no",
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
              },
              {
                "name": "industy",
                "type": "string"
              },
              {
                "name": "score",
                "type": "double"
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

- 需要提前创建好分区，分区拼接在path中
- windows环境下datax执行hdfswriter，hdfs://localhost:9000/user/hive/warehouse/offline.db后面拼接路径符的时候会根据操作系统拼\导致临时文件的为hdfs://localhost:9000/user/hive/warehouse/db\...
删除的时候\后面不识别，会删库


