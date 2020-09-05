package com.wugui.datax.admin.tool.query;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wugui.datatx.core.datasource.HBaseDataSource;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;


public class HBaseQueryTool {

    private Configuration conf = HBaseConfiguration.create();

    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
    ExecutorService pool = new ThreadPoolExecutor(2, 50, 1L, SECONDS,
            new LinkedBlockingQueue<>(10), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    private Connection connection = null;
    private Admin admin;
    private Table table;

    public HBaseQueryTool(DbType dbType, String parameter) throws IOException {
        HBaseDataSource hBaseDataSource = JSONUtils.parseObject(parameter, HBaseDataSource.class);
        String[] zkAddress = hBaseDataSource.getZkAddress().split(Constants.SPLIT_SCOLON);
        conf.set("hbase.zookeeper.quorum", zkAddress[0]);
        conf.set("hbase.zookeeper.property.clientPort", zkAddress[1]);
        connection =  ConnectionFactory.createConnection(conf, pool);
        admin = connection.getAdmin();
    }

    /**
     * 关闭连接
     */
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
        for (TableName name : names) {
            list.add(name.getNameAsString());
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
