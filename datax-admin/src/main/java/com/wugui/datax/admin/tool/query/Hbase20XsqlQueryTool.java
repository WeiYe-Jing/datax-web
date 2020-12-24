package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.enums.DbType;


/**
 * @author jingwk
 */
public class Hbase20XsqlQueryTool extends BaseQueryTool implements QueryToolInterface {


    public Hbase20XsqlQueryTool(DbType dbType, String parameter){
        super(dbType,parameter);
    }

//    @Override
//    public List<String> getTableNames(String tableSchema) {
//        DatabaseMetaData metaData;
//        List<String> tables = new ArrayList<>();
//        ResultSet rs = null;
//        try {
//            metaData = conn.getMetaData();
//            rs = metaData.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});
//            while (rs.next()) {
//                tables.add(rs.getString("TABLE_NAME"));
//            }
//
//        } catch (SQLException e) {
//            logger.error("[getTableNames Exception] --> the exception message is:{}", e.getMessage());
//        } finally {
//            JdbcUtils.close(rs);
//        }
//        return tables;
//    }

//    @Override
//    public List<String> getColumnNames(String tableName, String dataSource) {
//        DatabaseMetaData metaData;
//        List<String> columnNames = Lists.newArrayList();
//        ResultSet rs = null;
//        try {
//            metaData = conn.getMetaData();
//            rs = metaData.getColumns(conn.getCatalog(), null, tableName, "%");
//            while (rs.next()) {
//                columnNames.add(rs.getString("COLUMN_NAME"));
//                // 获取字段的数据类型  rs.getString("TYPE_NAME")
//            }
//
//        } catch (SQLException e) {
//            logger.error("[getColumnNames Exception] --> the exception message is:{}", e.getMessage());
//        } finally {
//            JdbcUtils.close(rs);
//        }
//        return columnNames;
//    }

}
