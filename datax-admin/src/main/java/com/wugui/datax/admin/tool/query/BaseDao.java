package com.wugui.datax.admin.tool.query;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public abstract class BaseDao {

    private static Connection conn;
    private static Admin admin;

    protected void start(String zookeeper) throws Exception {
        getConnection(zookeeper);
        getAdmin(zookeeper);
    }

    public synchronized Connection getConnection(String zookeeper) throws IOException {
        if (conn == null){
           // Configuration conf = HBaseConfiguration.create();
            Configuration conf=new Configuration();
            conf.set("hbase.zookeeper.quorum",zookeeper);
            conn = ConnectionFactory.createConnection(conf);
            admin = conn.getAdmin();
        }
        return conn;
    }

    protected void end(String zookeeper) throws Exception {
        Admin admin = getAdmin(zookeeper);
        if (admin != null) {
            admin.close();
        }

        Connection conn = getConnection(zookeeper);
        if (conn != null) {
            conn.close();
        }
    }

    protected synchronized Admin getAdmin(String zookeeper) throws IOException {
        if( admin == null){
            admin = getConnection(zookeeper).getAdmin();
        }
        return admin;
    }


}
