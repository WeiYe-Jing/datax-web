package com.wugui.datatx.core.datasource;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * data source of SQL Server
 */
public class SQLServerDataSource extends BaseDataSource {

    private static final Logger logger = LoggerFactory.getLogger(SQLServerDataSource.class);

    /**
     * gets the JDBC url for the data source connection
     * @return jdbc url
     */
    @Override
    public String getJdbcUrl() {
        String jdbcUrl = getAddress();
        jdbcUrl += ";databaseName=" + getDatabase();

        if (StringUtils.isNotEmpty(getOther())) {
            jdbcUrl += ";" + getOther();
        }

        return jdbcUrl;
    }

    /**
     * test whether the data source can be connected successfully
     */
    @Override
    public void isConnectable() {
        Connection con = null;
        try {
            Class.forName(Constants.COM_SQLSERVER_JDBC_DRIVER);
            con = DriverManager.getConnection(getJdbcUrl(), getUser(), getPassword());
        } catch (Exception e) {
            logger.error("error", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    logger.error("SQL Server datasource try conn close conn error", e);
                }
            }
        }
    }
  /**
   * @return driver class
   */
  @Override
  public String driverClassSelector() {
    return Constants.COM_SQLSERVER_JDBC_DRIVER;
  }

  /**
   * @return db type
   */
  @Override
  public DbType dbTypeSelector() {
    return DbType.SQLSERVER;
  }
}
