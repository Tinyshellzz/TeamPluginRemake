package tcc.youajing.teamplugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static tcc.youajing.teamplugin.ObjectPool.pluginConfig;


public class MysqlConfig {
    public static Connection connect() throws SQLException {
        String database = String.format("jdbc:mysql://%s:%s/%s", pluginConfig.db_host, pluginConfig.db_port, pluginConfig.db_database);
        Connection conn = DriverManager.getConnection(database, pluginConfig.db_user, pluginConfig.db_passwd);
        conn.setAutoCommit(false);
        return conn;
    }
}
