package com.wugui.datax.admin.tool.query;


import com.wugui.datatx.core.util.Constants;
import com.wugui.datax.admin.core.util.LocalCacheUtil;
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

    private Configuration conf = HBaseConfiguration.create();
    private ExecutorService pool = Executors.newScheduledThreadPool(2);
    private Connection connection = null;
    private Admin admin;
    private Table table;

    public HBaseQueryTool(JobDatasource jobDatasource) throws IOException {
        if (LocalCacheUtil.get(jobDatasource.getDatasourceName()) == null) {
            getDataSource(jobDatasource);
        } else {
            connection = (Connection) LocalCacheUtil.get(jobDatasource.getDatasourceName());
            if (connection == null || connection.isClosed()) {
                LocalCacheUtil.remove(jobDatasource.getDatasourceName());
                getDataSource(jobDatasource);
            }
        }
        LocalCacheUtil.set(jobDatasource.getDatasourceName(), connection, 4 * 60 * 60 * 1000);
    }

    private void getDataSource(JobDatasource jobDatasource) throws IOException {
        String[] zkAdress = jobDatasource.getZkAdress().split(Constants.SPLIT_SCOLON);
        conf.set("hbase.zookeeper.quorum", zkAdress[0]);
        conf.set("hbase.zookeeper.property.clientPort", zkAdress[1]);
        connection = ConnectionFactory.createConnection(conf, pool);
        admin = connection.getAdmin();
    }

    // 关闭连接
    public void sourceClose() {
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
     * 测试是否连接成功
     *
     * @return
     * @throws IOException
     */
    public boolean dataSourceTest() throws IOException {
        Admin admin = connection.getAdmin();
        HTableDescriptor[] tableDescriptor = admin.listTables();
        return tableDescriptor.length > 0;
    }

    /**
     * 获取HBase表名称
     *
     * @return
     * @throws IOException
     */
    public List<String> getTableNames() throws IOException {
        List<String> list = new ArrayList<>();
        Admin admin = connection.getAdmin();
        TableName[] names = admin.listTableNames();
        for (int i = 0; i < names.length; i++) {
            list.add(names[i].getNameAsString());
        }
        return list;
    }

    /**
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
        if (it.hasNext()) {
            Result re = it.next();
            List<Cell> listCells = re.listCells();
            for (Cell cell : listCells) {
                list.add(new String(CellUtil.cloneFamily(cell)) + ":" + new String(CellUtil.cloneQualifier(cell)));
            }
        }
        return list;
    }
}
