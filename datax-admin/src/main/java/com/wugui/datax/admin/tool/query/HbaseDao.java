package com.wugui.datax.admin.tool.query;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HbaseDao extends BaseDao implements Serializable {
  private static volatile HbaseDao hbaseDao = null;

  public HbaseDao() {
  }

  public static HbaseDao me() {
    if (hbaseDao == null) {
      synchronized (HbaseDao.class) {
        if (hbaseDao == null) {
          hbaseDao = new HbaseDao();
        }
      }
    }
    return hbaseDao;
  }

  /**
   * 查询是否连接Hbase成功
   *
   * @param con
   * @return
   * @throws IOException
   */
  public boolean getFamliy(Connection con) throws IOException {
    List<String> list = new ArrayList<>();
    HBaseAdmin admin = new HBaseAdmin(con);
    HTableDescriptor[] tableDescriptor = admin.listTables();
    for (int i = 0; i < tableDescriptor.length; i++) {
      list.add(tableDescriptor[i].getNameAsString());
    }
    if (list.size() > 0) {
      return true;
    } else return false;
  }

  /**
   * 查询出Hbase表名称
   *
   * @param con
   * @return
   * @throws IOException
   */
  public List<String> getTbales(Connection con) throws IOException {
    List<String> list = new ArrayList<>();
    // Instantiating HBaseAdmin class
    HBaseAdmin admin = new HBaseAdmin(con);
    // Getting all the list of tables using HBaseAdmin object
    HTableDescriptor[] tableDescriptor = admin.listTables();
    // printing all the table names.
    for (int i = 0; i < tableDescriptor.length; i++) {
      System.out.println();
      list.add(tableDescriptor[i].getNameAsString());
    }
    return list;
  }

  /**
   * \
   * 通过表名查询所有l列祖和列
   *
   * @param con
   * @param tableName
   * @return
   * @throws IOException
   */
  public List<String> getColumns(Connection con, String tableName) throws IOException {
    List<String> list = new ArrayList<>();
    Table table = con.getTable(TableName.valueOf(tableName));
    HTableDescriptor hTableDescriptor = table.getTableDescriptor();
    for (HColumnDescriptor fdescriptor : hTableDescriptor.getColumnFamilies()) {
      list.add(fdescriptor.getNameAsString());
    }
    for (int i = 0; i < list.size(); i++) {
      System.out.println(list.get(i));
    }
    return list;
  }

  /**
   * 查询列
   * @param con
   * @param tablename
   * @param row
   * @return
   * @throws IOException
   */
  public String getlies(Configuration con, String tablename, String row) throws IOException {
   // List<String> list = new ArrayList<>();
    HTable table = new HTable(con, tablename);
    Get get = new Get(Bytes.toBytes(row));
    Result result = table.get(get);
    String rows = Bytes.toString(result.getRow());
    //list.add(rows);
    return rows;
  }

  /**
   * 全局扫描
   * @param conf
   * @param tableName
   * @return
   * @throws Exception
   */
  public ResultScanner getResultScann(Configuration conf, String tableName) throws Exception {

    Scan scan=new Scan();
    ResultScanner rs =null;
    HTable htable=new HTable(conf, tableName);
    try{
      rs=htable.getScanner(scan);
      for(Result r: rs){
        for(KeyValue kv:r.list()){
          System.out.println(Bytes.toString(kv.getRow()));
          System.out.println(Bytes.toString(kv.getFamily()));
          System.out.println(Bytes.toString(kv.getQualifier()));
          System.out.println(Bytes.toString(kv.getValue()));
          System.out.println(kv.getTimestamp());
        }
      }
    }finally{
      rs.close();
    }
    return rs;
  }
}

