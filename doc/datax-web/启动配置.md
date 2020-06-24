## 启动命令模板
可以参考datax.py文件
```
ENGINE_COMMAND = "java -server ${jvm} %s -classpath %s  ${params} com.alibaba.datax.core.Engine -mode ${mode} -jobid ${jobid} -job ${job}" % (
    DEFAULT_PROPERTY_CONF, CLASS_PATH)
```
## bash启动命令
```
java -server -Xms2g -Xmx2g -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/huzekang/openSource/DataX/target/datax/datax/log  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/huzekang/openSource/DataX/target/datax/datax/log -Dloglevel=info -Dfile.encoding=UTF-8 -Dlogback.statusListenerClass=ch.qos.logback.core.status.NopStatusListener -Djava.security.egd=file:///dev/urandom -Ddatax.home=/Users/huzekang/openSource/DataX/target/datax/datax -Dlogback.configurationFile=/Users/huzekang/openSource/DataX/target/datax/datax/conf/logback.xml -classpath /Users/huzekang/openSource/DataX/target/datax/datax/lib/*:.  -Dlog.file.name=le_oracle2mysql_json com.alibaba.datax.core.Engine -mode standalone -jobid -1 -job /Users/huzekang/openSource/DataX/job-sample/oracle2mysql.json

```

## maven编译打包
```
mvn -U clean package assembly:assembly -Dmaven.test.skip=true
```

## oracle转到MongoDB的配置
```$json
{
  "job": {
  "setting": {
      "speed": {
          "channel": 5
      }
  },
  "content": [
      {
          "reader": {
              "name": "oraclereader",
              "parameter": {
                  "username": "GZFUYI20190301",
                  "password": "yibo123",
                  "column": [
                      "REPORT_NUMBER","REPORT_DATE_SERIAL","EXAM_ITEM_NAME","EXAM_RESULT"
                  ],
                  "connection": [
                            {
                                "table": [
                                    "TB_LIS_INDICATORS"
                                ],
                                "jdbcUrl": [
     "jdbc:oracle:thin:@192.168.1.130:1521:gzfy"
                                ]
                            }
                        ]
              }
          },
          "writer": {
              "name": "mongodbwriter",
              "parameter": {
                  "address": [
                      "192.168.1.226:27017"
                  ],
                  "userName": "",
                  "userPassword": "",
                  "dbName": "datax_gzfy",
                  "collectionName": "indicator",
                  "column":   [
  { "name" : "reportNumber"         , "type" : "string"},
  { "name" : "reportDateSerial"    , "type" : "string"},
  { "name" : "examItemName"        , "type" : "string"},
  { "name" : "examResult"           , "type" : "string"}]
              }
          }
      }
  ]
  }
  }
```
## oracle到mysql
```
{
    "job": {
        "setting": {
            "speed": {
                "channel": 5
            }
        },
        "content": [
            {
                "reader": {
                    "name": "oraclereader",
                    "parameter": {
                        "username": "GZFUYI20190301",
                        "password": "yibo123",
                        "column": [
                            "*",
                            
                        ],
                        "connection": [
                            {
                                "table": [
                                    "TB_LIS_INDICATORS"
                                ],
                                "jdbcUrl": [
                                    "jdbc:oracle:thin:@192.168.1.130:1521:gzfy"
                                ]
                            }
                        ]
                    }
                },
                "writer": {
                    "name": "mysqlwriter",
                    "parameter": {
                        "writeMode": "insert",
                        "username": "root",
                        "password": "root",
                        "column": [
                            "*"
                        ],
                        "session": [
                            "set session sql_mode='ANSI'"
                        ],
                        "connection": [
                            {
                                "jdbcUrl": "jdbc:mysql://192.168.1.150:3306/test?useUnicode=true&characterEncoding=gbk",
                                "table": [
                                    "TB_LIS_INDICATORS"
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