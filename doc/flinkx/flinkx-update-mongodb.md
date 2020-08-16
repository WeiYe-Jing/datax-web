# FLinkX的Mongodb插件存在的问题以及对源码进行优化
PR地址[https://github.com/DTStack/flinkx/pull/263]

## 一、目前FlinkX的mongodb插件存在的问题  
### 1、mongodbreader在查询[*]的时候，并不能适应真正的nosql非结构化数据  
### 2、查询mongodb的ObjectId时，数据类型没有处理，插入mysql时，会报错  
### 3、查询Array和Object时，结果数据的数据结构不正确

## 二、mongodbreader查询[*]  
### 1)、mongodb中存在的测试数据  
```
{ "_id" : ObjectId("5f38ba948592e21c30acf06e"), "username" : "jack", "age" : "30" }
{ "_id" : ObjectId("5f38ba948592e21c30acf06f"), "age" : "30", "username" : "jack" }
{ "_id" : ObjectId("5f38bd9aa2f2b04d9f9125a7"), "username" : "jack", "age" : "30", "installDate" : ISODate("2020-08-16T05:01:14.098Z") }
{ "_id" : ObjectId("5f38bd9aa2f2b04d9f9125a8"), "age" : "30", "username" : "jack", "installDate" : ISODate("2020-08-16T05:01:14.098Z") }
{ "_id" : ObjectId("5f38bdd5b356190f2752cdb3") }
{ "_id" : ObjectId("5f38bde622d2ab66755ea950"), "username" : "jack", "list" : [ { "username" : "jack", "age" : "30", "installDate" : ISODate("2020-08-16T05:02:30.323Z") }, { "age" : "30", "username" : "jack", "installDate" : ISODate("2020-08-16T05:02:30.323Z") } ] }
{ "_id" : ObjectId("5f38be06635b721e5ff16183"), "username" : "jack", "doc" : { "age" : "30", "username" : "jack", "installDate" : ISODate("2020-08-16T05:03:02.212Z") } }
```
### 2)、查询的job配置  
```
{
    "job":{
        "content":[
            {
                "reader":{
                    "parameter":{
                        "url":"mongodb://username:password@localhost:27017/test",
                        "fetchSize":100,
                        "collectionName":"mycol2",
                        "filter":"{}",
                        "column":[
                            "*"
                        ]
                    },
                    "name":"mongodbreader"
                },
                "writer":{
                    "parameter":{
                        "path":"/Users/jack/Documents/jack-project/flinkx/data",
                        "protocol":"sftp",
                        "port":22,
                        "writeMode":"overwrite",
                        "host":"localhost",
                        "column":[
                            {
                                "name":"_id",
                                "type":"string"
                            },
                            {
                                "name":"username",
                                "type":"string"
                            },
                            {
                                "name":"age",
                                "type":"string"
                            },
                            {
                                "name":"installDate",
                                "type":"datetime"
                            },
                            {
                                "name":"list",
                                "type":"string"
                            },
                            {
                                "name":"doc",
                                "type":"string"
                            }
                        ],
                        "password":"",
                        "fieldDelimiter":",",
                        "encoding":"utf-8",
                        "username":"jack"
                    },
                    "name":"ftpwriter"
                }
            }
        ],
        "setting":{
            "speed":{
                "channel":1,
                "bytes":0
            },
            "errorLimit":{
                "record":100
            },
            "restore":{
                "maxRowNumForCheckpoint":0,
                "isRestore":false,
                "restoreColumnName":"",
                "restoreColumnIndex":0
            },
            "log":{
                "isLogger":true,
                "level":"info",
                "path":"",
                "pattern":""
            }
        }
    }
}
```
### 3)、查询结果  
```
5f38ba948592e21c30acf06e,jack,30
5f38ba948592e21c30acf06f,30,jack
5f38bd9aa2f2b04d9f9125a7,jack,30,2020-08-16 13:01:14
5f38bd9aa2f2b04d9f9125a8,30,jack,2020-08-16 13:01:14
5f38bdd5b356190f2752cdb3
5f38bde622d2ab66755ea950,jack,[Document{{username=jack, age=30, installDate=Sun Aug 16 13:02:30 CST 2020}}, Document{{age=30, username=jack, installDate=Sun Aug 16 13:02:30 CST 2020}}]
5f38be06635b721e5ff16183,jack,Document{{age=30, username=jack, installDate=Sun Aug 16 13:03:02 CST 2020}}
```
### 4)、分析存在的问题  
可以看出username字段和age字段在第一行和第二行，字段位置错位了。查看代码发现原因：
```
if(metaColumns.size() == 1 && ConstantValue.STAR_SYMBOL.equals(metaColumns.get(0).getName())){
           row = new Row(doc.size());
            // 遍历mongodb的doc中的key，插入顺序取决于doc中key的顺序，而key顺序是无序的，因此会导致读取乱序
            String[] names = doc.keySet().toArray(new String[0]);
            for (int i = 0; i < names.length; i++) {
                row.setField(i,doc.get(names[i]));
            }
}
```
### 5)、改进思路  
在查询[*]时，将查询Document对象保存在一个字段中，而不是遍历Document的所有字段
```
 if(metaColumns.size() == 1 && ConstantValue.STAR_SYMBOL.equals(metaColumns.get(0).getName())){
            row = new Row(1);
            doc = Document.parse(doc.toJson(settings));
            row.setField(0,doc.toJson());
          
}
```
### 6)、改进后测试job  
```
{
    "job":{
        "content":[
            {
                "reader":{
                    "parameter":{
                        "url":"mongodb://username:password@localhost:27017/test",
                        "fetchSize":100,
                        "collectionName":"mycol2",
                        "filter":"{}",
                        "column":[
                            "*"
                        ]
                    },
                    "name":"mongodbreader"
                },
                "writer":{
                    "parameter":{
                        "path":"/Users/jack/Documents/jack-project/flinkx/data",
                        "protocol":"sftp",
                        "port":22,
                        "writeMode":"overwrite",
                        "host":"localhost",
                        "column":[
                            {
                                "name":"_id",
                                "type":"string"
                            }
                        ],
                        "password":"xxxx",
                        "fieldDelimiter":",",
                        "encoding":"utf-8",
                        "username":"jack"
                    },
                    "name":"ftpwriter"
                }
            }
        ],
        "setting":{
            "speed":{
                "channel":1,
                "bytes":0
            },
            "errorLimit":{
                "record":100
            },
            "restore":{
                "maxRowNumForCheckpoint":0,
                "isRestore":false,
                "restoreColumnName":"",
                "restoreColumnIndex":0
            },
            "log":{
                "isLogger":true,
                "level":"info",
                "path":"",
                "pattern":""
            }
        }
    }
}
```
### 7)、查看导出到本地的结果数据    
```
{"_id": "5f38ba948592e21c30acf06e", "username": "jack", "age": "30"}
{"_id": "5f38ba948592e21c30acf06f", "age": "30", "username": "jack"}
{"_id": "5f38bd9aa2f2b04d9f9125a7", "username": "jack", "age": "30", "installDate": "1597554074098"}
{"_id": "5f38bd9aa2f2b04d9f9125a8", "age": "30", "username": "jack", "installDate": "1597554074098"}
{"_id": "5f38bdd5b356190f2752cdb3"}
{"_id": "5f38bde622d2ab66755ea950", "username": "jack", "list": [{"username": "jack", "age": "30", "installDate": "1597554150323"}, {"age": "30", "username": "jack", "installDate": "1597554150323"}]}
{"_id": "5f38be06635b721e5ff16183", "username": "jack", "doc": {"age": "30", "username": "jack", "installDate": "1597554182212"}}
```
### 8)、结论  
可以看出所有的字段都导出来了，而且放入了一个字段，用户在writer配置时，不需要配置具体的字段名称了。能动态的将Collection中所有字段导出来，真正地满足了nosql非结构化数据导出。

## 三、查询mongodb的ObjectId时，数据类型没有处理，插入mysql时，会报错   
### 1)、配置job文件  
查询_id、name插入到mysql,将提前将日志的level设置为all，或者按照此pr修改日志级别[https://github.com/DTStack/flinkx/pull/260]
```
{
  "job": {
    "content": [
      {
"reader" : {
        "parameter" : {
          "url": "mongodb://username:password@localhost:27017/test",
          "fetchSize": 100,
          "collectionName" : "mycol2",
          "filter" : "{}",
          "column" : ["_id","name"]
        },
        "name" : "mongodbreader"
      },
        "writer": {
          "name": "mysqlwriter",
          "parameter": {
            "username": "root",
            "password": "password",
            "connection": [
              {
                "jdbcUrl": "jdbc:mysql://localhost:3306/test2?useSSL=false&characterEncoding=utf-8",
                "table": ["bb"]
              }
            ],
            "writeMode": "insert",
            "column": ["aa","bb"],
            "batchSize": 1024
          }
        }
      }
    ],
    "setting": {
      "speed": {
        "channel": 1,
        "bytes": 0
      },
      "errorLimit": {
        "record": 100
      },
      "restore": {
        "maxRowNumForCheckpoint": 0,
        "isRestore": false,
        "restoreColumnName": "",
        "restoreColumnIndex": 0
      },
      "log" : {
        "isLogger": true,
        "level" : "info",
        "path" : "",
        "pattern":""
      }
    }
  }
}
``` 
### 2)、运行job文件，发现插入异常  
```
16:13:42.124 [Source: mongodbreader -> Sink: mysqlwriter (1/1)] ERROR com.dtstack.flinkx.mysql.format.MysqlOutputFormat - write error row, row = 5f38be06635b721e5ff16183,null, e = com.dtstack.flinkx.exception.WriteRecordException: Incorrect string value: '\xAC\xED\x00\x05sr...' for column 'aa' at row 1
java.sql.SQLException: Incorrect string value: '\xAC\xED\x00\x05sr...' for column 'aa' at row 1
	at com.dtstack.flinkx.rdb.outputformat.JdbcOutputFormat.processWriteException(JdbcOutputFormat.java:224)
	at com.dtstack.flinkx.rdb.outputformat.JdbcOutputFormat.writeSingleRecordInternal(JdbcOutputFormat.java:210)
	at com.dtstack.flinkx.outputformat.BaseRichOutputFormat.writeSingleRecord(BaseRichOutputFormat.java:337)
	at java.util.ArrayList.forEach(ArrayList.java:1257)
	at com.dtstack.flinkx.outputformat.BaseRichOutputFormat.writeRecordInternal(BaseRichOutputFormat.java:426)
	at com.dtstack.flinkx.outputformat.BaseRichOutputFormat.close(BaseRichOutputFormat.java:464)
	at com.dtstack.flinkx.streaming.api.functions.sink.DtOutputFormatSinkFunction.close(DtOutputFormatSinkFunction.java:120)
	at org.apache.flink.api.common.functions.util.FunctionUtils.closeFunction(FunctionUtils.java:43)
	at org.apache.flink.streaming.api.operators.AbstractUdfStreamOperator.close(AbstractUdfStreamOperator.java:109)
	at org.apache.flink.streaming.runtime.tasks.StreamTask.closeAllOperators(StreamTask.java:635)
	at org.apache.flink.streaming.runtime.tasks.StreamTask.lambda$afterInvoke$1(StreamTask.java:515)
	at org.apache.flink.streaming.runtime.tasks.StreamTaskActionExecutor$SynchronizedStreamTaskActionExecutor.runThrowing(StreamTaskActionExecutor.java:94)
	at org.apache.flink.streaming.runtime.tasks.StreamTask.afterInvoke(StreamTask.java:513)
	at org.apache.flink.streaming.runtime.tasks.StreamTask.invoke(StreamTask.java:478)
	at org.apache.flink.runtime.taskmanager.Task.doRun(Task.java:707)
	at org.apache.flink.runtime.taskmanager.Task.run(Task.java:532)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.sql.SQLException: Incorrect string value: '\xAC\xED\x00\x05sr...' for column 'aa' at row 1
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:965)
	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3976)
	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3912)
	at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2530)
	at com.mysql.jdbc.ServerPreparedStatement.serverExecute(ServerPreparedStatement.java:1283)
	at com.mysql.jdbc.ServerPreparedStatement.executeInternal(ServerPreparedStatement.java:783)
	at com.mysql.jdbc.PreparedStatement.execute(PreparedStatement.java:1197)
	at com.dtstack.flinkx.rdb.outputformat.JdbcOutputFormat.writeSingleRecordInternal(JdbcOutputFormat.java:208)
	... 15 more
```
### 3)、异常原因分析  
在JdbcOutputFormat的writeSingleRecordInternal方法打debug断点，然后进行debug查看。可以查看_id字段的getField()方法的返回结果数据类型为ObjectId,
而Mysql的Jdbc协议不支持ObjectId数据类型，因此会报错
![](../pic/flinkx/flinkx-update-mongodb/debug.jpg "")
### 4)、对Mongodb的_id查询进行优化  
```
public Row nextRecordInternal(Row row) throws IOException {
        Document doc = cursor.next();
        // 添加bson的反序列化参数设置
        doc = Document.parse(doc.toJson(settings));
        if(metaColumns.size() == 1 && ConstantValue.STAR_SYMBOL.equals(metaColumns.get(0).getName())){
            row = new Row(1);
            row.setField(0,doc.toJson());

        } else {
            row = new Row(metaColumns.size());
            for (int i = 0; i < metaColumns.size(); i++) {
                MetaColumn metaColumn = metaColumns.get(i);

                Object value = null;
                if(metaColumn.getName() != null){
                    value = doc.get(metaColumn.getName());
                    if(value == null && metaColumn.getValue() != null){
                        value = metaColumn.getValue();
                    }
                } else if(metaColumn.getValue() != null){
                    value = metaColumn.getValue();
                }

                if(value instanceof String){
                    value = StringUtil.string2col(String.valueOf(value),metaColumn.getType(),metaColumn.getTimeFormat());
                }

                row.setField(i,value);
            }
        }

        return row;
    }
```
### 5)、运行并查看执行结果，可以看出_id成功插入到mysql数据库了
```
flinkx -mode local  -job /Users/jack/Documents/jack-project/flinkx/flinkconf/mongodb2mysql.json   -pluginRoot /Users/jack/Documents/jack-project/flinkx/syncplugins   -confProp "{\"flink.checkpoint.interval\":60000}"
```

```
---------------------------------
numWrite                  |  7
last_write_num_0          |  0
conversionErrors          |  0
writeDuration             |  20631
duplicateErrors           |  0
numRead                   |  7
snapshotWrite             |  0
otherErrors               |  0
readDuration              |  310
byteRead                  |  217
last_write_location_0     |  0
byteWrite                 |  217
nullErrors                |  0
nErrors                   |  0
```
![](../pic/flinkx/flinkx-update-mongodb/result.jpg "")

## 四、查询Mongodb的Array和Object时，数据结构不正确   
导出结果并不符合JsonObject或者JsonArray的数据结构规范     


### 1)、配置job文件  
```

{
  "job": {
    "content": [
      {
"reader" : {
        "parameter" : {
          "url": "mongodb://username:password@localhost:27017/test",
          "fetchSize": 100,
          "collectionName" : "mycol2",
          "filter" : "{}",
          "column" : ["list","doc"]
        },
        "name" : "mongodbreader"
      },
       "writer": {
                    "parameter": {
                        "path": "/Users/jack/Documents/jack-project/flinkx/data",
                        "protocol": "sftp",
                        "port": 22,
                        "writeMode": "overwrite",
                        "host": "localhost",
                        "column": [
                         {
"name": "_id",
"type": "string"
}
                        ],
                        "password": "xxxx",
                        "fieldDelimiter": ",",
                        "encoding": "utf-8",
                        "username": "jack"
                    },
                    "name": "ftpwriter"
        }
      }
    ],
    "setting": {
      "speed": {
        "channel": 1,
        "bytes": 0
      },
      "errorLimit": {
        "record": 100
      },
      "restore": {
        "maxRowNumForCheckpoint": 0,
        "isRestore": false,
        "restoreColumnName": "",
        "restoreColumnIndex": 0
      },
      "log" : {
        "isLogger": true,
        "level" : "info",
        "path" : "",
        "pattern":""
      }
    }
  }
}
```
### 2)、查看导出结果，可以发现doc字段没有成功导出，list对象格式不符合JsonArray规范  
```
[Document{{username=jack, age=30, installDate=Sun Aug 16 13:02:30 CST 2020}}, Document{{age=30, username=jack, installDate=Sun Aug 16 13:02:30 CST 2020}}],
```
### 3)、优化代码  
```
public Row nextRecordInternal(Row row) throws IOException {
        Document doc = cursor.next();
        // 添加bson的反序列化参数设置
        doc = Document.parse(doc.toJson(settings));
        if(metaColumns.size() == 1 && ConstantValue.STAR_SYMBOL.equals(metaColumns.get(0).getName())){
            row = new Row(1);
            row.setField(0,doc.toJson());

        } else {
            row = new Row(metaColumns.size());
            for (int i = 0; i < metaColumns.size(); i++) {
                MetaColumn metaColumn = metaColumns.get(i);

                Object value = null;
                if(metaColumn.getName() != null){
                    value = doc.get(metaColumn.getName());
                    if(value == null && metaColumn.getValue() != null){
                        value = metaColumn.getValue();
                    }
                } else if(metaColumn.getValue() != null){
                    value = metaColumn.getValue();
                }

                if(value instanceof String){
                    value = StringUtil.string2col(String.valueOf(value),metaColumn.getType(),metaColumn.getTimeFormat());
                }

                row.setField(i,value);
            }
        }

        return row;
    }
```  
### 4)、查看执行结果,可以看出Mongodb的Object和Array对象已经符合json格式了
```
---------------------------------
numWrite                  |  7
last_write_num_0          |  0
conversionErrors          |  0
writeDuration             |  20736
duplicateErrors           |  0
numRead                   |  7
snapshotWrite             |  0
otherErrors               |  0
readDuration              |  429
byteRead                  |  607
last_write_location_0     |  0
byteWrite                 |  607
nullErrors                |  0
nErrors                   |  0
---------------------------------
```
```
mysql> select * from bb;
+-------------------------------------------------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| aa                                                                | bb                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
+-------------------------------------------------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| NULL                                                              | NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| NULL                                                              | NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| NULL                                                              | NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| NULL                                                              | NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| NULL                                                              | NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| NULL                                                              | ["{\"_id\": \"5f38bde622d2ab66755ea950\", \"username\": \"jack\", \"list\": [{\"username\": \"jack\", \"age\": \"30\", \"installDate\": \"1597554150323\"}, {\"age\": \"30\", \"username\": \"jack\", \"installDate\": \"1597554150323\"}]}","{\"_id\": \"5f38bde622d2ab66755ea950\", \"username\": \"jack\", \"list\": [{\"username\": \"jack\", \"age\": \"30\", \"installDate\": \"1597554150323\"}, {\"age\": \"30\", \"username\": \"jack\", \"installDate\": \"1597554150323\"}]}"] |
| {"age": "30", "username": "jack", "installDate": "1597554182212"} | NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
+-------------------------------------------------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
```




   