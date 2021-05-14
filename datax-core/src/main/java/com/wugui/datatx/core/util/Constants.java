package com.wugui.datatx.core.util;

public final class Constants {

    public static final String MYSQL_DATABASE = "Unknown database";
    public static final String MYSQL_CONNEXP = "Communications link failure";
    public static final String MYSQL_ACCDENIED = "Access denied";
    public static final String MYSQL_TABLE_NAME_ERR1 = "Table";
    public static final String MYSQL_TABLE_NAME_ERR2 = "doesn't exist";
    public static final String MYSQL_SELECT_PRI = "SELECT command denied to user";
    public static final String MYSQL_COLUMN1 = "Unknown column";
    public static final String MYSQL_COLUMN2 = "field list";
    public static final String MYSQL_WHERE = "where clause";

    public static final String ORACLE_DATABASE = "ORA-12505";
    public static final String ORACLE_CONNEXP = "The Network Adapter could not establish the connection";
    public static final String ORACLE_ACCDENIED = "ORA-01017";
    public static final String ORACLE_TABLE_NAME = "table or view does not exist";
    public static final String ORACLE_SELECT_PRI = "insufficient privileges";
    public static final String ORACLE_SQL = "invalid identifier";


    public static final String SPLIT_COMMA = ",";
    public static final String SPLIT_AT = "@";
    public static final String SPLIT_COLON = ";";
    public static final String SPLIT_POINT = ".";
    public static final String SPLIT_SCOLON=":";
    public static final String SPLIT_HYPHEN = "-";
    public static final String SPLIT_DIVIDE = "/";
    public static final String SPLIT_STAR = "*";
    public static final String SPLIT_QUESTION = "?";
    public static final String EQUAL = "=";
    public static final String SPLIT_AMPERSAND = "&";
    public static final String AND = "AND";
    public static final String SPACE = " ";
    public static final String STRING_BLANK = "";
    public static final String MONGO_URL_PREFIX = "mongodb://";

    public static final String USER = "user";
    public static final String PASSWORD = "password";

    /**
     * hive conf
     */
    public static final String HIVE_CONF = "hiveconf:";


    /**
     * driver
     */
    public static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";
    public static final String COM_MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String ORG_APACHE_HIVE_JDBC_HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    public static final String COM_CLICKHOUSE_JDBC_DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";
    public static final String COM_ORACLE_JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String COM_SQLSERVER_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String COM_DB2_JDBC_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    public static final String COM_OSCAR_JDBC_DRIVER = "com.oscar.Driver";
    public static final String COM_PHOENIX_JDBC_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";

    /**
     * database type
     */
    public static final String MYSQL = "MYSQL";
    public static final String POSTGRESQL = "POSTGRESQL";
    public static final String HIVE = "HIVE";
    public static final String SPARK = "SPARK";
    public static final String CLICKHOUSE = "CLICKHOUSE";
    public static final String ORACLE = "ORACLE";
    public static final String SQLSERVER = "SQLSERVER";
    public static final String DB2 = "DB2";
    public static final String OSCAR = "OSCAR";

    /**
     * jdbc url
     */
    public static final String JDBC_MYSQL = "jdbc:mysql://";
    public static final String JDBC_POSTGRESQL = "jdbc:postgresql://";
    public static final String JDBC_HIVE_2 = "jdbc:hive2://";
    public static final String JDBC_CLICKHOUSE = "jdbc:clickhouse://";
    public static final String JDBC_ORACLE_SID = "jdbc:oracle:thin:@";
    public static final String JDBC_ORACLE_SERVICE_NAME = "jdbc:oracle:thin:@//";
    public static final String JDBC_SQLSERVER = "jdbc:sqlserver://";
    public static final String JDBC_DB2 = "jdbc:db2://";
    public static final String JDBC_OSCAR = "jdbc:oscar://";


    public static final String ADDRESS = "address";
    public static final String DATABASE = "database";
    public static final String JDBC_URL = "jdbcUrl";
    public static final String PRINCIPAL = "principal";
    public static final String OTHER = "other";
    public static final String ORACLE_DB_CONNECT_TYPE = "connectType";


    /**
     * kerberos
     */
    public static final String KERBEROS = "kerberos";

    /**
     * kerberos expire time
     */
    public static final String KERBEROS_EXPIRE_TIME = "kerberos.expire.time";

    /**
     * java.security.krb5.conf
     */
    public static final String JAVA_SECURITY_KRB5_CONF = "java.security.krb5.conf";

    /**
     * java.security.krb5.conf.path
     */
    public static final String JAVA_SECURITY_KRB5_CONF_PATH = "java.security.krb5.conf.path";


    /**
     * hadoop.security.authentication
     */
    public static final String HADOOP_SECURITY_AUTHENTICATION = "hadoop.security.authentication";

    /**
     * hadoop.security.authentication
     */
    public static final String HADOOP_SECURITY_AUTHENTICATION_STARTUP_STATE = "hadoop.security.authentication.startup.state";


    /**
     * loginUserFromKeytab user
     */
    public static final String LOGIN_USER_KEY_TAB_USERNAME = "login.user.keytab.username";

    /**
     * loginUserFromKeytab path
     */
    public static final String LOGIN_USER_KEY_TAB_PATH = "login.user.keytab.path";

    /**
     * dolphinscheduler.env.path
     */
    public static final String DOLPHINSCHEDULER_ENV_PATH = "datax.web.env.path";

    /**
     * environment properties default path
     */
    public static final String ENV_PATH = "env/dataxweb_env.sh";

    /**
     * resource storage type
     */
    public static final String RESOURCE_STORAGE_TYPE = "resource.storage.type";

    /**
     * development.state
     */
    public static final String DEVELOPMENT_STATE = "development.state";

}
