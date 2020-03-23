package com.wugui.datax.admin.tool.query;


import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.wugui.datax.admin.entity.JobDatasource;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MongoDBQueryTool {


    private static MongoClient mongoClient = null;
    private static MongoDBQueryTool instance = null;
    private static MongoDatabase collections;


    MongoDBQueryTool(JobDatasource jobDatasource) throws UnknownHostException {
        if (mongoClient == null) {
            if (StringUtils.isBlank(jobDatasource.getJdbcUsername()) && StringUtils.isBlank(jobDatasource.getJdbcPassword())) {
                mongoClient = new MongoClient(jobDatasource.getJdbcUrl());
            } else {
                MongoCredential credential = MongoCredential.createCredential(jobDatasource.getJdbcUsername(), jobDatasource.getDatabaseName(), jobDatasource.getJdbcPassword().toCharArray());
                mongoClient = new MongoClient(parseServerAddress(jobDatasource.getJdbcUrl()), Arrays.asList(credential));
            }

        }
    }

    /**
     * 获得该类的实例，单例模式
     *
     * @return
     */
    public static MongoDBQueryTool getInstance(JobDatasource jobDatasource) throws UnknownHostException {
        if (instance == null) {
            synchronized (MongoDBQueryTool.class) {
                if (instance == null) {
                    instance = new MongoDBQueryTool(jobDatasource);
                }
            }
        }
        return instance;
    }

    // 关闭连接
    public static void sourceClose() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * 获取DB名称列表
     *
     * @return
     */
    public List<String> getDBNames() {
        MongoIterable<String> dbs = mongoClient.listDatabaseNames();
        List<String> dbNames = new ArrayList<>();
        dbs.forEach((Block<? super String>) dbNames::add);
        return dbNames;
    }

    /**
     * 测试是否连接成功
     *
     * @return
     */
    public boolean dataSourceTest(String dbName) {
        collections = mongoClient.getDatabase(dbName);
        return collections.listCollectionNames().iterator().hasNext();
    }

    /**
     * 获取Collection名称列表
     *
     * @return
     */
    public List<String> getCollectionNames(String dbName) {
        collections = mongoClient.getDatabase(dbName);
        List<String> collectionNames = new ArrayList<>();
        collections.listCollectionNames().forEach((Block<? super String>) collectionNames::add);
        return collectionNames;
    }

    /**
     * 通过CollectionName查询列
     *
     * @param collectionName
     * @return
     */
    public List<String> getColumns(String collectionName) {
        MongoCollection<Document> collection = collections.getCollection(collectionName);
        Document document = collection.find(new BasicDBObject()).first();
        List<String> list = new ArrayList<>();
        document.forEach((k, v) -> {
            String type = v.getClass().getSimpleName();
      /*if ("Document".equals(type)) {
        ((Document) v).forEach((k1, v1) -> {
          String simpleName = v1.getClass().getSimpleName();
        });
      } */
            list.add(k + ":" + type);
        });
        return list;
    }

    /**
     * 判断地址类型是否符合要求
     *
     * @param addressList
     * @return
     */
    private static boolean isHostPortPattern(List<Object> addressList) {
        for (Object address : addressList) {
            String regex = "(\\S+):([0-9]+)";
            if (!((String) address).matches(regex)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转换为mongo地址协议
     *
     * @param rawAddress
     * @return
     */
    private static List<ServerAddress> parseServerAddress(String rawAddress) throws UnknownHostException {
        List<ServerAddress> addressList = new ArrayList<>();
        for (String address : Arrays.asList(rawAddress.split(","))) {
            String[] tempAddress = address.split(":");
            try {
                ServerAddress sa = new ServerAddress(tempAddress[0], Integer.valueOf(tempAddress[1]));
                addressList.add(sa);
            } catch (Exception e) {
                throw new UnknownHostException();
            }
        }
        return addressList;
    }
}
