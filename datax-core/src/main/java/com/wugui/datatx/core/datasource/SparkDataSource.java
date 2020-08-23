package com.wugui.datatx.core.datasource;


import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;

/**
 * data source of spark
 */
public class SparkDataSource extends BaseDataSource {

  /**
   * gets the JDBC url for the data source connection
   * @return jdbc url
   */
  @Override
  public String driverClassSelector() {
    return Constants.ORG_APACHE_HIVE_JDBC_HIVE_DRIVER;
  }

  /**
   * @return db type
   */
  @Override
  public DbType dbTypeSelector() {
    return DbType.SPARK;
  }
}
