package com.wugui.datatx.core.datasource;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * produce datasource in this custom defined datasource factory.
 */
public class DataSourceFactory {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    /**
     * getDatasource
     *
     * @param dbType    dbType
     * @param parameter parameter
     * @return getDatasource
     */
    public static BaseDataSource getDatasource(DbType dbType, String parameter) {
        BaseDataSource baseDataSource = null;
        try {
            if (dbType.getClazz() != null) {
                baseDataSource = JSONUtils.parseObject(parameter, dbType.getClazz());
            }
        } catch (Exception e) {
            logger.error("get datasource object error", e);
        }
        return baseDataSource;
    }

    /**
     * load class
     *
     * @param dbType
     * @throws Exception
     */
    public static void loadClass(DbType dbType) throws Exception {
        if (dbType.getDriver() != null) {
            Class.forName(dbType.getDriver());
        } else {
            logger.error("not support sql type: {},can't load class", dbType);
            throw new IllegalArgumentException("not support sql type,can't load class");
        }
    }
}
