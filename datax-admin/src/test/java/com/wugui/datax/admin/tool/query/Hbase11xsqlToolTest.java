//package com.wugui.datax.admin.tool.query;
//
//
//import com.wugui.datax.admin.util.ReflectionUtil;
//import org.apache.phoenix.compile.ColumnProjector;
//import org.apache.phoenix.compile.RowProjector;
//import org.apache.phoenix.util.SchemaUtil;
//import sqlline.SqlLine;
//import org.apache.phoenix.jdbc.PhoenixDriver;
//import java.io.IOException;
//import java.sql.*;
//import java.util.List;
//import java.util.Properties;
//
//public class Hbase11xsqlToolTest {
//
//    public static void main(String[] args) throws Exception {
//        String connectionString="jdbc:phoenix:hadoop1,hadoop2,hadoop3:2181";
////        String tableName = "SYSTEM.CATALOG";
//        String tableName = "STOCK_SYMBOL";
//
////        getPColumns(connectionString,tableName);
//
//
//        showTables(connectionString);
//
//        getColumns(connectionString,tableName);
//
////        showFields(connectionString,null);
//    }
//
//
//    private static int getSize(ResultSet rs) {
//        try {
//            if (rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {
//                return -1;
//            }
//
//            rs.last();
//            int total = rs.getRow();
//            rs.beforeFirst();
//            return total;
//        } catch (SQLException sqle) {
//            return -1;
//        } catch (AbstractMethodError ame) {
//            return -1;
//        }
//    }
//
//
//
//    private static void getColumns(String connectionString, String tableName) throws SQLException {
//        Connection conn = DriverManager.getConnection(connectionString);
//        DatabaseMetaData metaData = conn.getMetaData();
//        ResultSet columns = metaData.getColumns(conn.getCatalog(), null, tableName, "%");
//
//
//            try {
//                int total = getSize(columns);
//                int index = 0;
//
//                while (columns.next()) {
//
//                    System.out.println(columns.getString("COLUMN_NAME")+"  "+columns.getString("TYPE_NAME"));
//                }
//
//            } finally {
//                columns.close();
//            }
//
//
//    }
//
//
//
//
//    // 查询所有的表
//    private static void showTables(String connectionString) throws SQLException, IOException {
//        Connection conn = DriverManager.getConnection(connectionString);
//        DatabaseMetaData metaData = conn.getMetaData();
//        ResultSet rs = metaData.getTables(conn.getCatalog(), null, "%", new String[] {"TABLE"});
//
//        try {
//            while (rs.next()) {
//                System.out.println(rs.getString("TABLE_NAME"));
//            }
//        } finally {
//            try {
//                rs.close();
//            } catch (Exception e) {
//            }
//        }
//
//    }
//
//
//    // 查询所有的表
//    private static void showFields(String connectionString,String tableName) throws SQLException, NoSuchFieldException, IllegalAccessException {
//
//        Connection conn = DriverManager.getConnection(connectionString);
//
//
//
//        Statement stat = conn.createStatement();
//        ResultSet rs = stat.executeQuery("select * from   LKY_STOCK_SYMBOL limit 1");
//        RowProjector row = (RowProjector) ReflectionUtil.getPrivateField(rs, "rowProjector");
//        List<? extends ColumnProjector> column = row.getColumnProjectors();
//
//        for (int i = 0; i < column.size(); i++) {
//            ColumnProjector columnProjector = column.get(i);
//            System.out.println("这是字段信息 ====>" + column.get(i).getName());
//            //arry[i] = column.get(i).getName();
//        }
//        stat.close();
//        conn.close();
//    }
//
//}
