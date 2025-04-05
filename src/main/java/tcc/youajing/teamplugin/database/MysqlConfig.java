//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import tcc.youajing.teamplugin.ObjectPool;

public class MysqlConfig {
    public MysqlConfig() {
    }

    public static Connection connect() throws SQLException {
        String database = String.format("jdbc:mysql://%s:%s/%s", ObjectPool.pluginConfig.db_host, ObjectPool.pluginConfig.db_port, ObjectPool.pluginConfig.db_database);
        Connection conn = DriverManager.getConnection(database, ObjectPool.pluginConfig.db_user, ObjectPool.pluginConfig.db_passwd);
        conn.setAutoCommit(false);
        return conn;
    }
}
