package com.wugui.datatx.core.datasource;


import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * data source of mySQL
 */
public class MySQLDataSource extends BaseDataSource {

  private final Logger logger = LoggerFactory.getLogger(MySQLDataSource.class);

  private final String sensitiveParam = "autoDeserialize=true";

  private final char symbol = '&';

  /**
   * gets the JDBC url for the data source connection
   * @return jdbc url
   */
  @Override
  public String driverClassSelector() {
    return Constants.COM_MYSQL_JDBC_DRIVER;
  }

  /**
   * @return db type
   */
  @Override
  public DbType dbTypeSelector() {
    return DbType.MYSQL;
  }

  @Override
  protected String filterOther(String other){
    if(StringUtils.isBlank(other)){
        return "";
    }
    if(other.contains(sensitiveParam)){
      int index = other.indexOf(sensitiveParam);
      String tmp = sensitiveParam;
      if(index == 0 || other.charAt(index + 1) == symbol){
        tmp = tmp + symbol;
      } else if(other.charAt(index - 1) == symbol){
        tmp = symbol + tmp;
      }
      logger.warn("sensitive param : {} in otherParams field is filtered", tmp);
      other = other.replace(tmp, "");
    }
    logger.debug("other : {}", other);
    return other;
  }

  @Override
  public String getUser() {
    if(user.contains(sensitiveParam)){
      logger.warn("sensitive param : {} in username field is filtered", sensitiveParam);
      user = user.replace(sensitiveParam, "");
    }
    logger.debug("username : {}", user);
    return user;
  }

  @Override
  public String getPassword() {
    if(password.contains(sensitiveParam)){
      logger.warn("sensitive param : {} in password field is filtered", sensitiveParam);
      password = password.replace(sensitiveParam, "");
    }
    return password;
  }
}
