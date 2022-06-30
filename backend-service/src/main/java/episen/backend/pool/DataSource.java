package episen.backend.pool;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public final class DataSource {

    private static JDBCConnectionPool pool = new JDBCConnectionPool();
    private static DataSource instance;

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }


    public static Connection giveConnection() {
        return pool.giveConnection();
    }

    public static void retrieveConnection(Connection con) {
        pool.retrieveConnection(con);
    }

    public static void closeAllConnection() {
        pool.closeConnection();
    }
}
