package com.wugui.datax.admin.util;

import org.apache.hadoop.security.UserGroupInformation;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Kerberos {
    /**
     * 用于连接Hive所需的一些参数设置 driverName:用于连接hive的JDBC驱动名 When connecting to
     * HiveServer2 with Kerberos authentication, the URL format is:
     * jdbc:hive2://<host>:<port>/<db>;principal=
     * <Server_Principal_of_HiveServer2>
     */
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    public ApiMsg get_conn(String url,String iniPath , String user ,String keytabPath) throws ClassNotFoundException, SQLException {
        ApiMsg apiMsg =new  ApiMsg();
        Boolean flag =false;
        /** 使用Hadoop安全登录 **/
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("hadoop.security.authentication", "Kerberos");
        // todo 上传时需要注释掉
        if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
//        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            // 默认：这里不设置的话，win默认会到 C盘下读取krb5.init
//            System.setProperty("java.security.krb5.conf", iniPath);
//            System.setProperty("hadoop.home.dir", "D:\\hadoop-2.5.0\\hadoop-2.5.0");
        } // linux 会默认到 /etc/krb5.conf 中读取krb5.conf,本文笔者已将该文件放到/etc/目录下，因而这里便不用再设置了
        try {
            UserGroupInformation.setConfiguration(conf);
            // jc_anql 代表用户名  D ：\\****代表keytab文件位置
            UserGroupInformation.loginUserFromKeytab(user, keytabPath);
            Class.forName(driverName);
            Connection conn = DriverManager.getConnection(url);
            apiMsg.setFlag(true);
            apiMsg.setObj(conn);
        } catch (Exception e1) {
            e1.printStackTrace();
            apiMsg.setFlag(false);
        }
        return apiMsg;
    }
    public static void main(String[] args) {
//        String url = "jdbc:hive2://hadoop2:10000/jc_wud;principal=hive/hadoop2@DSJ.COM";
//        //设置 krb5.init 文件路径
//        String iniPath="C:\\ProgramData\\MIT\\Kerberos5\\krb5.ini";
//        //设置 keytab 用户名
//        String user="jc_wud";
//        //设置 keytab 文件路径
//        String keytabPath="C:\\ProgramData\\MIT\\Kerberos5\\jc_wud.keytab";
//
        String url="jdbc:hive2://hadoop2:10000/jc_wud;principal=hive/hadoop2@DSJ.COM111111";
        String iniPath="C:\\ProgramData\\MIT\\Kerberos5\\krb5.ini";
        String user= "jc_wud";
        String keytabPath="C:\\ProgramData\\MIT\\Kerberos5\\jc_wud.keytab";
        boolean flag =false;
        try {
            ApiMsg api = new Kerberos().get_conn(url, iniPath, user, keytabPath);
            flag=api.getFlag();
            System.out.println("连接成功"+flag);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("连接失败"+flag);
        }
    }
}