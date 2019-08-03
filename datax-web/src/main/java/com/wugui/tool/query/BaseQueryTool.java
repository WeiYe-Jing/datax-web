package com.wugui.tool.query;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.JdbcUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wugui.dataxweb.entity.JobJdbcDatasource;
import com.wugui.tool.database.ColumnInfo;
import com.wugui.tool.database.DasColumn;
import com.wugui.tool.database.TableInfo;
import com.wugui.tool.meta.DatabaseInterface;
import com.wugui.tool.meta.DatabaseMetaFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 抽象查询工具
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName BaseQueryTool
 * @Version 1.0
 * @since 2019/7/18 9:22
 */
public abstract class BaseQueryTool implements QueryToolInterface {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 用于获取查询语句
     */
    protected DatabaseInterface sqlBuilder;

    protected DataSource datasource;

    protected Connection connection;

    /**
     * 当前数据库类型
     */
    protected String currentDbType;

    /**
     * 当前数据库名
     */
    protected String currentSchema;

    protected JobJdbcDatasource jobJdbcDatasource;


    BaseQueryTool() {
        throw new UnsupportedOperationException();
    }


    /**
     * 构造方法
     *
     * @param jobJdbcDatasource
     */
    BaseQueryTool(JobJdbcDatasource jobJdbcDatasource) throws SQLException {
        //这里默认使用 hikari 数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(jobJdbcDatasource.getJdbcUsername());
        dataSource.setPassword(jobJdbcDatasource.getJdbcPassword());
        dataSource.setJdbcUrl(jobJdbcDatasource.getJdbcUrl());
        dataSource.setDriverClassName(jobJdbcDatasource.getJdbcDriverClass());
        //设为只读
        dataSource.setReadOnly(true);
        this.jobJdbcDatasource = jobJdbcDatasource;
        this.datasource = dataSource;
        this.connection = this.datasource.getConnection();
        currentDbType = JdbcUtils.getDbType(jobJdbcDatasource.getJdbcUrl(), jobJdbcDatasource.getJdbcDriverClass());
        sqlBuilder = DatabaseMetaFactory.getByDbType(currentDbType);
        currentSchema = getSchema();
    }

    //根据connection获取schema
    protected String getSchema() {
        String res = null;
        try {
            res = connection.getCatalog();
        } catch (SQLException e) {
            try {
                res = connection.getSchema();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        // 如果res是null，则将用户名当作 schema
        if (StrUtil.isBlank(res)) {
            res = jobJdbcDatasource.getJdbcUsername().toUpperCase();
        }
        return res;
    }

    @Override
    public TableInfo buildTableInfo(String tableName) {
        //获取表信息
        List<Map<String, Object>> tableInfos = this.getTableInfo(tableName);
        if (tableInfos.isEmpty()) {
            throw new NullPointerException("查询出错! ");
        }

        TableInfo tableInfo = new TableInfo();
        //表名，注释
        List tValues = new ArrayList(tableInfos.get(0).values());

        tableInfo.setName(StrUtil.toString(tValues.get(0)));
        tableInfo.setComment(StrUtil.toString(tValues.get(1)));


        //获取所有字段
        List<ColumnInfo> fullColumn = getColumns(tableName);
        tableInfo.setColumns(fullColumn);

        //获取主键列
        List<String> primaryKeys = getPrimaryKeys(tableName);
        logger.info("主键列为：{}", primaryKeys);

        //设置ifPrimaryKey标志
        fullColumn.forEach(e -> {
            if (primaryKeys.contains(e.getName())) {
                e.setIfPrimaryKey(true);
            } else {
                e.setIfPrimaryKey(false);
            }
        });

        return tableInfo;
    }

    //无论怎么查，返回结果都应该只有表名和表注释，遍历map拿value值即可
    @Override
    public List<Map<String, Object>> getTableInfo(String tableName) {
        String sqlQueryTableNameComment = sqlBuilder.getSQLQueryTableNameComment();
        logger.info(sqlQueryTableNameComment);
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlQueryTableNameComment, ImmutableList.of(currentSchema, tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getTables() {
        String sqlQueryTables = sqlBuilder.getSQLQueryTables();
        logger.info(sqlQueryTables);
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlQueryTables, ImmutableList.of(currentSchema));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<ColumnInfo> getColumns(String tableName) {

        List<ColumnInfo> fullColumn = Lists.newArrayList();
        //获取指定表的所有字段
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = sqlBuilder.getSQLQueryFields(tableName);
            logger.info("querySql: {}", querySql);

            //获取所有字段
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);
            ResultSetMetaData metaData = resultSet.getMetaData();

            List<DasColumn> dasColumns = buildDasColumn(tableName, metaData);
            statement.close();

            //构建 fullColumn
            fullColumn = buildFullColumn(dasColumns);

            logger.info("fullColumn: ");
            fullColumn.forEach(e -> logger.info(e.toString()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullColumn;
    }

    protected List<ColumnInfo> buildFullColumn(List<DasColumn> dasColumns) {
        List<ColumnInfo> res = Lists.newArrayList();
        dasColumns.forEach(e -> {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setName(e.getColumnName());
            columnInfo.setComment(e.getColumnComment());
            columnInfo.setType(e.getColumnClassName());
            res.add(columnInfo);
        });
        return res;
    }

    //构建DasColumn对象
    public List<DasColumn> buildDasColumn(String tableName, ResultSetMetaData metaData) {
        List<DasColumn> res = Lists.newArrayList();
        try {
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                DasColumn dasColumn = new DasColumn();
                dasColumn.setColumnClassName(metaData.getColumnClassName(i));
                dasColumn.setColumnTypeName(metaData.getColumnTypeName(i));
                dasColumn.setColumnName(metaData.getColumnName(i));
                res.add(dasColumn);
            }

            Statement statement = connection.createStatement();
            res.forEach(e -> {
                String sqlQueryComment = sqlBuilder.getSQLQueryComment(currentSchema, tableName, e.getColumnName());
                //查询字段注释
                try {
                    ResultSet resultSetComment = statement.executeQuery(sqlQueryComment);
                    while (resultSetComment.next()) {
                        e.setColumnComment(resultSetComment.getString(1));
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //获取指定表的主键，可能是多个，所以用list
    public List<String> getPrimaryKeys(String tableName) {
        List<String> res = Lists.newArrayList();
        String sqlQueryPrimaryKey = sqlBuilder.getSQLQueryPrimaryKey();
        try {
            List<Map<String, Object>> pkColumns = JdbcUtils.executeQuery(datasource, sqlQueryPrimaryKey, ImmutableList.of(currentSchema, tableName));
            //返回主键名称即可
            pkColumns.stream().forEach(e -> res.add((String) new ArrayList<>(e.values()).get(0)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<String> getColumnNames(String tableName) {

        List<String> res = Lists.newArrayList();
        Statement stmt = null;
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = sqlBuilder.getSQLQueryFields(tableName);
            logger.info("querySql: {}", querySql);

            //获取所有字段
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(querySql);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                res.add(metaData.getColumnName(i));
            }
//            logger.info("res: ");
//            res.forEach(e -> logger.info(e.toString()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(stmt);
        }
        return res;
    }

    @Override
    public List<String> getTableNames() {
        List<String> tables = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = getSQLQueryTables();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                tables.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return tables;
    }

    /**
     * 不需要其他参数的可不重写
     *
     * @return
     */
    protected String getSQLQueryTables() {
        return sqlBuilder.getSQLQueryTables();
    }

    @Override
    public List<String> getColumnsByQuerySql(String querySql) {

        List<String> res = Lists.newArrayList();
        Statement stmt = null;
        try {
            String sql = "";
            //拼装sql语句，在后面加上 where 1=0 即可
            sql = querySql.concat(" where 1=0");
            //判断是否已有where，如果是，则加 and 1=0
            //从最后一个 ) 开始找 where，或者整个语句找
            if (querySql.indexOf(")") != -1) {
                if (querySql.substring(querySql.indexOf(")")).contains("where")) {
                    sql = querySql.concat(" and 1=0");
                }
            } else {
                if (querySql.contains("where")) {
                    sql = querySql.concat(" and 1=0");
                }
            }

            logger.info("querySql: {}", sql);

            //获取所有字段
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                res.add(metaData.getColumnName(i));
            }
//            logger.info("res: ");
//            res.forEach(e -> logger.info(e.toString()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(stmt);
        }
        return res;
    }
}
