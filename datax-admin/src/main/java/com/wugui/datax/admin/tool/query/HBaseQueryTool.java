package com.wugui.datax.admin.tool.query;


import com.wugui.datatx.core.util.Constants;
import com.wugui.datax.admin.entity.JobDatasource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HBaseQueryTool {

  private static Configuration conf = HBaseConfiguration.create();
  private static ExecutorService pool = Executors.newScheduledThreadPool(2);
  private static Connection connection = null;
  private static HBaseQueryTool instance = null;
  private static Admin admin;
  private static Table table;


   HBaseQueryTool(JobDatasource jobDatasource) throws IOException {
    if (connection == null|| connection.isClosed()){
      String[] zkAdress=jobDatasource.getZkAdress().split(Constants.SPLIT_SCOLON);
      conf.set("hbase.zookeeper.quorum",zkAdress[0]);
      conf.set("hbase.zookeeper.property.clientPort", zkAdress[1]);
      connection = ConnectionFactory.createConnection(conf,pool);
      admin = connection.getAdmin();
    }
  }

  /**
   * 获得该类的实例，单例模式
   *
   * @return
   */
  public static HBaseQueryTool getInstance(JobDatasource jobDatasource) throws IOException {
    if (instance == null) {
      synchronized(HBaseQueryTool.class){
        if(instance == null){
          instance = new HBaseQueryTool(jobDatasource);
        }
      }
    }
    return instance;
  }

  // 关闭连接
  public static void sourceClose() {
    try {
      if (admin != null) {
        admin.close();
      }
      if (null != connection) {
        connection.close();
      }
      if (table != null) {
        table.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * 查询是否连接Hbase成功
   *
   * @return
   * @throws IOException
   */
  public boolean getFamily() throws IOException {
    Admin admin =connection.getAdmin();
    HTableDescriptor[] tableDescriptor = admin.listTables();
    return tableDescriptor.length > 0;
  }

  /**
   * 获取HBase表名称
   * @return
   * @throws IOException
   */
  public List<String> getTableNames() throws IOException {
    List<String> list = new ArrayList<>();
    Admin admin = connection.getAdmin();
    TableName[] names=admin.listTableNames();
    for (int i = 0; i < names.length; i++) {
      list.add(names[i].getNameAsString());
    }
    return list;
  }

  /**
   *
   * 通过表名查询所有l列祖和列
   *
   * @param tableName
   * @return
   * @throws IOException
   */
  public List<String> getColumns(String tableName) throws IOException {
    List<String> list = new ArrayList<>();
    table = connection.getTable(TableName.valueOf(tableName));
    Scan scan = new Scan();
    //Filter filter = new PageFilter(1);
    //scan.setFilter(filter);
    scan.getStartRow();
    ResultScanner scanner = table.getScanner(scan);
    Iterator<Result> it = scanner.iterator();
    if(it.hasNext()) {
      Result re = it.next();
      List<Cell> listCells = re.listCells();
      for (Cell cell : listCells) {
        list.add(new String(CellUtil.cloneFamily(cell))+":"+new String(CellUtil.cloneQualifier(cell)));
      }
    }
    return list;
  }
}
