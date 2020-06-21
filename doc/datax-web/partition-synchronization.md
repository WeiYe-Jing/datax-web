# DataX分区同步

## 一、DataX Json配置（样例）

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
          "name": "hdfsreader",
          "parameter": {
                        "hadoopConfig": {
                            "dfs.nameservices": "nameservice1",
                            "dfs.ha.namenodes.nameservice1": "cdh201.qq.org,cdh202.qq.org",
                            "dfs.namenode.rpc-address.nameservice1.cdh201.qq.org": "cdh201.qq.org:8020",
                            "dfs.namenode.rpc-address.nameservice1.cdh202.qq.org": "cdh202.qq.org:8020",
                            "dfs.client.failover.proxy.provider.nameservice1": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"
                        },
            "path": "/user/gsbdc/dbdatas/olsd/bns/gsods_rpt_qq/poi/p_data_day=2018-05-14/*",
                        "haveKerberos": "true",
                        "kerberosPrincipal": "bi@qq.ORG",
            "defaultFS": "hdfs://nameservice1",
                        "kerberosKeytabFilePath": "/app/soft/datax/job/bi.keytab",
            "fileType": "text",
            "fieldDelimiter": "\u0001",
            "column": [
              {
                "index": "0",
                "type": "string"
              },
              {
                "index": "1",
                "type": "string"
              },
              {
                "index": "2",
                "type": "string"
              },
              {
                "index": "3",
                "type": "string"
              },
              {
                "index": "4",
                "type": "string"
              },
              {
                "value": "${p_data_day}",
                "type": "string"
              }				  
            ]
          }
        },
        "writer": {
          "name": "clickhousewriter",
          "parameter": {
            "username": "s",
            "password": "s",
            "column": [
              "id",
              "address",
              "p_name",
              "c_name",
              "d_name",
              "p_data_day"
            ],
            "connection": [
              {
                "table": [
                  "poi"
                ],
                "jdbcUrl": "jdbc:clickhouse://192.168.1.1:18123/test"
              }
            ]
          }
        }
      }
    ]
  }
}

```
## 二、reader分区信息的配置

- DataX hdfsreader无法获取分区信息，我们可以通过动态参数指定分区信息，reader中分区信息的配置如下：

```
{
  "value": "${p_data_day}",
   "type": "string"
}	
```

## 三、python 执行命令

```
python /app/soft/datax/bin/datax.py -p "-Dp_data_day=2020-06-20"  /app/soft/datax/job/hive2clickhouse.json
```

- 注意：命令中的p_data_day分区字段要和reader中配置的value变量名称一致。

## 四、DataX Web中配置动态传参

- 机制：配置定时任务，任务执行时获取当前时间及用户选择的当前时间+—天数计算得到动态参数的值。
- 示例：
![](https://datax-web.oss-cn-hangzhou.aliyuncs.com/doc/partition.png)




