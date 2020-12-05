package com.wugui.datatx.core.datasource;

/**
 * data source of phoenix
 */
public class MongoDBDataSource {

  /**
   * user name
   */
  protected String user;

  /**
   * user password
   */
  protected String password;

  /**
   * database name
   */
  private String database;

  /**
   * mongo client URI
   */
  private String mongoClientURI;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getMongoClientURI() {
    return mongoClientURI;
  }

  public void setMongoClientURI(String mongoClientURI) {
    this.mongoClientURI = mongoClientURI;
  }
}
