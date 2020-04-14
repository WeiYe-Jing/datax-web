package com.wugui.datax.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.DatasourceQueryService;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.database.ColumnInfo;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import com.wugui.datax.admin.tool.query.HBaseQueryTool;
import com.wugui.datax.admin.tool.query.MongoDBQueryTool;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import com.wugui.datax.admin.util.ClickHouseConstant;
import com.wugui.datax.admin.util.JdbcConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Types;
import java.util.List;

/**
 * datasource query
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName JdbcDatasourceQueryServiceImpl
 * @Version 1.0
 * @since 2019/7/31 20:51
 */
@Service
public class DatasourceQueryServiceImpl implements DatasourceQueryService {

    @Autowired
    private JobDatasourceService jobDatasourceService;

    @Override
    public List<String> getDBs(Long id) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        return new MongoDBQueryTool(datasource).getDBNames();
    }


    @Override
    public List<String> getTables(Long id) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        if (JdbcConstants.HBASE.equals(datasource.getDatasource())) {
            return new HBaseQueryTool(datasource).getTableNames();
        } else if (JdbcConstants.MONGODB.equals(datasource.getDatasource())) {
            return new MongoDBQueryTool(datasource).getCollectionNames(datasource.getDatabaseName());
        } else {


            BaseQueryTool qTool = QueryToolFactory.getByDbType(datasource);
            qTool.execeBuildTableSql(ClickHouseConstant.create_sql);
            return qTool.getTableNames();
        }
    }

    @Override
    public List<String> getCollectionNames(long id, String dbName) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        return new MongoDBQueryTool(datasource).getCollectionNames(dbName);
    }


    @Override
    public List<String> getColumns(Long id, String tableName) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        if (JdbcConstants.HBASE.equals(datasource.getDatasource())) {
            return new HBaseQueryTool(datasource).getColumns(tableName);
        } else if (JdbcConstants.MONGODB.equals(datasource.getDatasource())) {
            return new MongoDBQueryTool(datasource).getColumns(tableName);
        } else {
            BaseQueryTool queryTool = QueryToolFactory.getByDbType(datasource);

               if(JdbcConstants.MYSQL.equals(datasource.getDatasource())){
                   build_createMysql2ClickHouseSQL(tableName, queryTool);
               }

            return queryTool.getColumnNames(tableName, datasource.getDatasource());
        }
    }

    private void build_createMysql2ClickHouseSQL(String tableName, BaseQueryTool queryTool) {
        List<ColumnInfo> columnInfos = queryTool.getColumns(tableName);

        /**
         * 构建建表语句
         */

        //TODO
        StringBuilder stringBuilder = new StringBuilder();
        String preSql = String.format("CREATE TABLE IF NOT EXISTS %s (", ClickHouseConstant.database_name + "_" + tableName);
        stringBuilder.append(preSql);
        String primatyKey = "";
        for (int i = 0; i < columnInfos.size(); i++) {
            ColumnInfo c = columnInfos.get(i);
            if (c.getIfPrimaryKey()) {
                primatyKey = c.getName();
            }
            String str = "";
            if (i == columnInfos.size() - 1) {
                switch (c.getType()) {
                    case "VARCHAR":
                    case "CHAR":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " String COMMENT '" + c.getComment() + "'";

                        } else {
                            str = c.getName() + " Nullable(String) COMMENT '" + c.getComment() + "'";
                        }
                        break;
                    case "INT":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " UInt16 COMMENT '" + c.getComment() + "'";

                        } else {
                            str = c.getName() + " Nullable(UInt16) COMMENT '" + c.getComment() + "'";
                        }
                        break;
                    case "DATE":
                    case "DATETIME":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " datetime COMMENT '" + c.getComment() + "'";

                        } else {
                            str = c.getName() + " Nullable(datetime) COMMENT '" + c.getComment() + "'";
                        }
                        break;
                    default:
                        System.out.println(c.getType());
                        break;


                }
            } else {
                switch (c.getType()) {
                    case "VARCHAR":
                    case "CHAR":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " String COMMENT '" + c.getComment() + "',";

                        } else {
                            str = c.getName() + " Nullable(String) COMMENT '" + c.getComment() + "',";
                        }
                        break;
                    case "INT":
                    case "TINYINT":
                    case "BIGINT":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " UInt16 COMMENT '" + c.getComment() + "',";

                        } else {
                            str = c.getName() + " Nullable(UInt16) COMMENT '" + c.getComment() + "',";
                        }
                        break;
                    case "DATE":
                    case "DATETIME":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " datetime COMMENT '" + c.getComment() + "',";

                        } else {
                            str = c.getName() + " Nullable(datetime) COMMENT '" + c.getComment() + "',";
                        }
                        break;
                    default:
                        System.out.println("=============尚未捕获的数据类型"+c.getType());
                        break;


                }
            }


            stringBuilder.append(str);


        }
        String afterSQl = String.format("   ,datacenter_insert_time DateTime DEFAULT now() COMMENT '数据中心数据抽取入库时间' ) ENGINE = MergeTree PARTITION BY id ORDER BY id", primatyKey, preSql);
        stringBuilder.append(afterSQl);
        System.out.println(stringBuilder.toString());
        ClickHouseConstant.create_sql = stringBuilder.toString();








    }

    @Override
    public List<String> getColumnsByQuerySql(Long datasourceId, String querySql) {
        //获取数据源对象
        JobDatasource jdbcDatasource = jobDatasourceService.getById(datasourceId);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        return queryTool.getColumnsByQuerySql(querySql);
    }
}
