package com.wugui.datatx.core.datasource;

import com.wugui.datatx.core.enums.DbConnectType;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
/**
 * data source of Oracle
 */
public class OracleDataSource extends BaseDataSource {

    private DbConnectType connectType;

    public DbConnectType getConnectType() {
        return connectType;
    }

    public void setConnectType(DbConnectType connectType) {
        this.connectType = connectType;
    }

    /**
     * @return driver class
     */
    @Override
    public String driverClassSelector() {
        return Constants.COM_ORACLE_JDBC_DRIVER;
    }

    /**
     * append service name or SID
     */
    @Override
    protected void appendDatabase(StringBuilder jdbcUrl) {
        if (getConnectType() == DbConnectType.ORACLE_SID) {
            jdbcUrl.append(":");
        } else {
            jdbcUrl.append("/");
        }
        jdbcUrl.append(getDatabase());
    }

    /**
     * @return db type
     */
    @Override
    public DbType dbTypeSelector() {
        return DbType.ORACLE;
    }

}
