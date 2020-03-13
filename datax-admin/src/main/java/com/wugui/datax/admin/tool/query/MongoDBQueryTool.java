package com.wugui.datax.admin.tool.query;


import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.wugui.datax.admin.entity.JobDatasource;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MongoDBQueryTool {


  private static MongoClient mongoClient = null;
  private static MongoDBQueryTool instance = null;
  private static MongoDatabase collections;



   MongoDBQueryTool(JobDatasource jobDatasource) {
    if (mongoClient == null){
      mongoClient = new MongoClient(new MongoClientURI(jobDatasource.getJdbcUrl()));
    }
  }

  /**
   * 获得该类的实例，单例模式
   * @return
   */
  public static MongoDBQueryTool getInstance(JobDatasource jobDatasource) throws IOException {
    if (instance == null) {
      synchronized(MongoDBQueryTool.class){
        if(instance == null){
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
    List<String> dbNames=new ArrayList<>();
    dbs.forEach((Block<? super String>) dbNames::add);
    return dbNames;
  }

  /**
   * 获取Collection名称列表
   * @return
   */
  public List<String> getCollectionNames(String dbName) {
    collections = mongoClient.getDatabase(dbName);
    List<String> collectionNames=new ArrayList<>();
    collections.listCollectionNames().forEach((Block<? super String>) collectionNames::add);
    return collectionNames;
  }

  /**
   *
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
      list.add(k+":"+type);
    });
    return list;
  }
}
