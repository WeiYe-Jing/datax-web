package com.wugui.datax.admin.tool.query;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.JdbcUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wugui.datatx.core.datasource.BaseDataSource;
import com.wugui.datatx.core.datasource.DataSourceFactory;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datax.admin.tool.database.ColumnInfo;
import com.wugui.datax.admin.tool.database.DasColumn;
import com.wugui.datax.admin.tool.database.TableInfo;
import com.wugui.datax.admin.tool.meta.DatabaseInterface;
import com.wugui.datax.admin.tool.meta.DatabaseMetaFactory;
import com.wugui.datax.admin.tool.table.TableNameHandle;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
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

    private static final Logger logger = LoggerFactory.getLogger(BaseQueryTool.class);
    /**
     * 用于获取查询语句
     */
    private final DatabaseInterface sqlBuilder;

    private final Connection connection;

    private final String currentSchema;

    private final String currentDatabase;


    public BaseQueryTool(final DbType dbType, final String parameter) {
        sqlBuilder = DatabaseMetaFactory.getByDbType(dbType);
        connection = DriverConnectionFactory.getConnection(dbType, parameter);
        BaseDataSource datasource = DataSourceFactory.getDatasource(dbType, parameter);
        currentSchema = getSchema(datasource.getUser());
        currentDatabase = datasource.getDatabase();
    }

    /**
     * 根据connection获取schema
     *
     * @param jdbcUsername String
     * @return String
     */
    private String getSchema(final String jdbcUsername) {
        String res = null;
        try {
            res = connection.getCatalog();
        } catch (SQLException e) {
            try {
                res = connection.getSchema();
            } catch (SQLException e1) {
                logger.error("[SQLException getSchema Exception] --> "
                        + "the exception message is:" + e1.getMessage());
            }
            logger.error("[getSchema Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        // 如果res是null，则将用户名当作 schema
        if (StrUtil.isBlank(res) && StringUtils.isNotBlank(jdbcUsername)) {
            res = jdbcUsername.toUpperCase();
        }
        return res;
    }

    @Override
    public TableInfo buildTableInfo(String tableSchema, String tableName) {
        //获取表信息
        List<Map<String, Object>> tableInfos = this.getTableInfo(tableName);
        if (tableInfos.isEmpty()) {
            throw new NullPointerException("查询出错! ");
        }

        TableInfo tableInfo = new TableInfo();
        //表名，注释
        List<Map<String, Object>> tValues = new ArrayList(tableInfos.get(0).values());

        tableInfo.setName(StrUtil.toString(tValues.get(0)));
        tableInfo.setComment(StrUtil.toString(tValues.get(1)));

        //获取所有字段
        List<ColumnInfo> fullColumn = getColumns(tableSchema, tableName);
        tableInfo.setColumns(fullColumn);

        //获取主键列
        List<String> primaryKeys = getPrimaryKeys(tableName);
        logger.info("主键列为：{}", primaryKeys);

        //设置ifPrimaryKey标志
        for (ColumnInfo e : fullColumn) {
            e.setIfPrimaryKey(primaryKeys.contains(e.getName()));
        }
        return tableInfo;
    }


    /**
     * 无论怎么查，返回结果都应该只有表名和表注释，遍历map拿value值即可
     *
     * @param tableName String
     * @return List<Map < String, Object>>
     */
    @Override
    public List<Map<String, Object>> getTableInfo(final String tableName) {
        String sqlQueryTableNameComment = sqlBuilder.getSQLQueryTableNameComment();
        logger.info(sqlQueryTableNameComment);
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlQueryTableNameComment, ImmutableList.of(currentSchema, tableName));
        } catch (SQLException e) {
            logger.error("[getTableInfo Exception] --> "
                    + "the exception message is:" + e.getMessage());
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
            logger.error("[getTables Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return res;
    }

    @Override
    public List<ColumnInfo> getColumns(String tableSchema, String tableName) {
        List<ColumnInfo> fullColumn = Lists.newArrayList();
        //获取查询指定表所有字段的sql语句
        String querySql = sqlBuilder.getSQLQueryFields(tableSchema, tableName);
        logger.info("querySql: {}", querySql);
        //获取指定表的所有字段
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(querySql)){
            //获取所有字段
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<DasColumn> dasColumns = buildDasColumn(tableName, metaData);
            statement.close();
            //构建 fullColumn
            fullColumn = buildFullColumn(dasColumns);
        } catch (SQLException e) {
            logger.error("[getColumns Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return fullColumn;
    }

    private List<ColumnInfo> buildFullColumn(final List<DasColumn> dasColumns) {
        List<ColumnInfo> res = Lists.newArrayList();
        dasColumns.forEach(e -> {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setName(e.getColumnName());
            columnInfo.setComment(e.getColumnComment());
            columnInfo.setType(e.getColumnTypeName());
            columnInfo.setIfPrimaryKey(e.isIsprimaryKey());
            columnInfo.setIsnull(e.getIsNull());
            res.add(columnInfo);
        });
        return res;
    }


    /**
     * 构建DasColumn对象
     *
     * @param tableName
     * @param metaData
     * @return
     */
    private List<DasColumn> buildDasColumn(final String tableName, final ResultSetMetaData metaData) {
        List<DasColumn> res = Lists.newArrayList();
        try {
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                DasColumn dasColumn = new DasColumn();
                dasColumn.setColumnClassName(metaData.getColumnClassName(i));
                dasColumn.setColumnTypeName(metaData.getColumnTypeName(i));
                dasColumn.setColumnName(metaData.getColumnName(i));
                dasColumn.setIsNull(metaData.isNullable(i));
                res.add(dasColumn);
            }
            try (Statement statement = connection.createStatement()) {
                if (currentDatabase.equals(Constants.MYSQL) || currentDatabase.equals(Constants.ORACLE)) {
                    DatabaseMetaData databaseMetaData = connection.getMetaData();
                    ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
                    while (resultSet.next()) {
                        String name = resultSet.getString("COLUMN_NAME");
                        for (DasColumn e : res) {
                            e.setIsprimaryKey(e.getColumnName().equals(name));
                        }
                    }
                    res.forEach(e -> {
                        String sqlQueryComment = sqlBuilder.getSQLQueryComment(currentSchema, tableName, e.getColumnName());
                        //查询字段注释
                        try (ResultSet resultSetComment = statement.executeQuery(sqlQueryComment)) {
                            while (resultSetComment.next()) {
                                e.setColumnComment(resultSetComment.getString(1));
                            }
                        } catch (SQLException e1) {
                            logger.error("[buildDasColumn executeQuery Exception] --> "
                                    + "the exception message is:" + e1.getMessage());
                        }
                    });
                }
            }
        } catch (SQLException e) {
            logger.error("[buildDasColumn Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return res;
    }


    /**
     * 获取指定表的主键，可能是多个，所以用list
     *
     * @param tableName String
     * @return List<String>
     */
    private List<String> getPrimaryKeys(final String tableName) {
        List<String> res = Lists.newArrayList();
        String sqlQueryPrimaryKey = sqlBuilder.getSQLQueryPrimaryKey();
        try {
            List<Map<String, Object>> pkColumns = JdbcUtils.executeQuery(connection, sqlQueryPrimaryKey, ImmutableList.of(currentSchema, tableName));
            //返回主键名称即可
            pkColumns.forEach(e -> res.add((String) new ArrayList<>(e.values()).get(0)));
        } catch (SQLException e) {
            logger.error("[getPrimaryKeys Exception] --> the exception message is:" + e.getMessage());
        }
        return res;
    }

    @Override
    public List<String> getColumnNames(String tableName, String tableSchema, final DbType dbType) {

        List<String> res = Lists.newArrayList();
        //处理表名
        if (DbType.ORACLE.equals(dbType) || DbType.POSTGRESQL.equals(dbType)) {
            tableName = TableNameHandle.addDoubleQuotes(tableName);
        }
        //获取查询指定表所有字段的sql语句
        String querySql = sqlBuilder.getSQLQueryFields(tableSchema, tableName);
        logger.info("querySql: {}", querySql);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySql);
        ) {
            //获取所有字段
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                if (DbType.HIVE.equals(dbType)) {
                    if (columnName.contains(Constants.SPLIT_POINT)) {
                        res.add(i - 1 + Constants.SPLIT_SCOLON + columnName.substring(columnName.indexOf(Constants.SPLIT_POINT) + 1) + Constants.SPLIT_SCOLON + metaData.getColumnTypeName(i));
                    } else {
                        res.add(i - 1 + Constants.SPLIT_SCOLON + columnName + Constants.SPLIT_SCOLON + metaData.getColumnTypeName(i));
                    }
                } else {
                    res.add(columnName);
                }

            }
        } catch (SQLException e) {
            logger.error("[getColumnNames Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return res;
    }

    @Override
    public List<String> getTableNames(final String tableSchema) {
        List<String> tables = new ArrayList<>();
        String sql = getSqlQueryTables(tableSchema);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tableName = rs.getString(1);
                tables.add(tableName);
            }
            tables.sort(Comparator.naturalOrder());
        } catch (SQLException e) {
            logger.error("[getTableNames Exception] --> input tableSchema is {} ,the exception message is {}",
                    tableSchema, e.getMessage());
        }
        return tables;
    }

    @Override
    public List<String> getTableNames() {
        List<String> tables = new ArrayList<>();
        String sql = getSqlQueryTables();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tableName = rs.getString(1);
                tables.add(tableName);
            }
        } catch (SQLException e) {
            logger.error("[getTableNames Exception] --> the exception message is:" + e.getMessage());
        }
        return tables;
    }

    public Boolean dataSourceTest() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            if (metaData.getDatabaseProductName().length() > 0) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("[dataSourceTest Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
        return false;
    }


    protected String getSqlQueryTables(final String tableSchema) {
        return sqlBuilder.getSQLQueryTables(tableSchema);
    }

    /**
     * @return String
     */
    private String getSqlQueryTables() {
        return sqlBuilder.getSQLQueryTables();
    }

    @Override
    public List<String> getColumnsByQuerySql(String querySql) throws SQLException {

        List<String> res = Lists.newArrayList();
        //替换sql中的from 、where、on、group、order为小写
        String sql;
        querySql = querySql.replace(";", "");
        querySql = querySql.replace("\nFROM ", " from ").replace("\nfrom ", " from ").replace(" FROM ", " from ");
        querySql = querySql.replace("\nWHERE ", " where ").replace("\nwhere ", " where ").replace(" WHERE ", " where ");
        querySql = querySql.replace("\nON ", " on ").replace("\non ", " on ").replace(" ON ", " on ");
        querySql = querySql.replace("\nGROUP ", " group ").replace("\ngroup ", " group ").replace(" GROUP ", " group ");
        querySql = querySql.replace("\nORDER ", " order ").replace("\norder ", " order ").replace(" ORDER ", " order ");
        //寻找from 、where、on、group、order在sql中的位置
        int idx_from = querySql.lastIndexOf(" from ");
        int idx_where = querySql.lastIndexOf(" where ");
        int idx_on = querySql.lastIndexOf(" on ");
        int idx_group = querySql.lastIndexOf(" group ");
        int idx_order = querySql.lastIndexOf(" order ");
        //根据各情况添加where 1=0 或on 1=0
        if ((idx_where > idx_on && idx_on > idx_from) || (idx_where > idx_from && idx_from > idx_on)) {
            sql = replaceLast(querySql, " where ", " where 1=0 and ");
        } else if (idx_group > idx_on && idx_on > idx_from) {
            sql = replaceLast(querySql, " group ", " where 1=0 group ");
        } else if (idx_order > idx_on && idx_on > idx_from) {
            sql = replaceLast(querySql, " order ", " where 1=0 order ");
        } else {
            sql = querySql.concat(" where 1=0");
        }
        //获取所有字段
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                res.add(metaData.getColumnName(i));
            }
        }
        return res;
    }

    @Override
    public long getMaxIdVal(final String tableName, final String primaryKey) {
        long maxVal = 0;
        String sql = getSQLMaxID(tableName, primaryKey);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            maxVal = rs.getLong(1);
        } catch (SQLException e) {
            logger.error("[getMaxIdVal Exception] --> the exception message is:" + e.getMessage());
        }
        return maxVal;
    }

    private String getSQLMaxID(final String tableName, final String primaryKey) {
        return sqlBuilder.getMaxId(tableName, primaryKey);
    }

    public void executeCreateTableSql(final String querySql) {
        if (StringUtils.isBlank(querySql)) {
            return;
        }
        try (Statement stmt = connection.createStatement();) {
            stmt.executeUpdate(querySql);
        } catch (SQLException e) {
            logger.error("[executeCreateTableSql Exception] --> "
                    + "the exception message is:" + e.getMessage());
        }
    }

    public List<String> getDbSchema() {
        List<String> schemas = new ArrayList<>();
        String sql = getSQLQueryTableSchema();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tableName = rs.getString(1);
                schemas.add(tableName);
            }
        } catch (SQLException e) {
            logger.error("[getDbSchema Exception] --> the exception message is:" + e.getMessage());
        }
        return schemas;
    }

    protected String getSQLQueryTableSchema() {
        return sqlBuilder.getSQLQueryTableSchema();
    }

    public static String replaceLast(final String text, final String strToReplace, final String replaceWithThis) {
        return text.replaceFirst("(?s)" + strToReplace + "(?!.*?" + strToReplace + ")", replaceWithThis);
    }
}
