package com.wugui.datatx.core.datasource;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;

/**
 * data source of OSCAR Server
 *
 * @author Locki
 * @date 2021-04-26
 */
public class OscarServerDataSource extends BaseDataSource{
    @Override
    public String driverClassSelector() {
        return Constants.COM_OSCAR_JDBC_DRIVER;
    }

    @Override
    public DbType dbTypeSelector() {
        return DbType.OSCAR;
    }
}
