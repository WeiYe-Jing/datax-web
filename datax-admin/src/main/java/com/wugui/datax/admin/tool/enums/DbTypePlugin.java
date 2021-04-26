package com.wugui.datax.admin.tool.enums;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.tool.datax.reader.*;
import com.wugui.datax.admin.tool.datax.writer.*;
import com.wugui.datax.admin.tool.meta.*;
import com.wugui.datax.admin.tool.query.*;
import lombok.Getter;

/**
 * 数据源类型插件枚举类
 *
 * @author jiangyang
 * @date 2020/12/24
 */
@Getter
public enum DbTypePlugin {
    /**
     * 数据源类型插件枚举值
     */
    MYSQL(DbType.MYSQL, MySQLDatabaseMeta.getInstance(), MySQLQueryTool.class, new MysqlReader(), new MysqlWriter()),
    POSTGRESQL(DbType.POSTGRESQL, PostgresqlDatabaseMeta.getInstance(), PostgresqlQueryTool.class, new PostgresqlReader(), new PostgresqllWriter()),
    HIVE(DbType.HIVE, HiveDatabaseMeta.getInstance(), HiveQueryTool.class, new HiveReader(), new HiveWriter()),
    SPARK(DbType.SPARK, null, null, null, null),
    CLICKHOUSE(DbType.CLICKHOUSE, ClickHouseDataBaseMeta.getInstance(), ClickHouseQueryTool.class, new ClickHouseReader(), new ClickHouseWriter()),
    ORACLE(DbType.ORACLE, OracleDatabaseMeta.getInstance(), OracleQueryTool.class, new OracleReader(), new OraclelWriter()),
    SQLSERVER(DbType.SQLSERVER, SqlServerDatabaseMeta.getInstance(), SqlServerQueryTool.class, new SqlServerReader(), new SqlServerlWriter()),
    DB2(DbType.DB2, DB2DatabaseMeta.getInstance(), DB2SQLQueryTool.class, new DB2Reader(), new DB2Writer()),
    OSCAR(DbType.OSCAR, OscarDatabaseMeta.getInstance(), OscarQueryTool.class, new OscarReader(), new OscarWriter()),
    GREENPLUM(DbType.GREENPLUM, PostgresqlDatabaseMeta.getInstance(), PostgresqlQueryTool.class, new PostgresqlReader(), new PostgresqllWriter()),
    HBASE20XSQL(DbType.HBASE20XSQL, Hbase20xsqlMeta.getInstance(), Hbase20XsqlQueryTool.class, null, null),
    HBASE(DbType.HBASE, null, null, new HBaseReader(), new HBaseWriter()),
    MONGODB(DbType.MONGODB, null, null, new MongoDBReader(), new MongoDBWriter()),
    PHOENIX(DbType.PHOENIX, null, null, null, null);

    private DbType dbType;
    private DatabaseInterface databaseInterface;
    private Class<? extends BaseQueryTool> clazz;
    private BaseReaderPlugin readerPlugin;
    private BaseWriterPlugin writerPlugin;

    DbTypePlugin(DbType dbType, DatabaseInterface databaseInterface, Class<? extends BaseQueryTool> clazz, BaseReaderPlugin readerPlugin, BaseWriterPlugin writerPlugin) {
        this.dbType = dbType;
        this.databaseInterface = databaseInterface;
        this.clazz = clazz;
        this.readerPlugin = readerPlugin;
        this.writerPlugin = writerPlugin;
    }

    public static DbTypePlugin getDbTypePlugin(DbType dbType) {
        for (DbTypePlugin typeEnum : DbTypePlugin.values()) {
            if (typeEnum.dbType == dbType) {
                return typeEnum;
            }
        }
        throw new UnsupportedOperationException("找不到该类型: ".concat(dbType.name()));
    }
}
