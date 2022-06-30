package episen.backend.pool;

import episen.backend.prototype.BackendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class JDBCConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(JDBCConnectionPool.class.getName());

    private static ArrayList<Connection> myConnection = new ArrayList<>();
    private Properties props;

    public JDBCConnectionPool()  {
        props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(props.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.turnConnection();
    }



    private Connection connectionFactory() {
        Connection con = null;

        try {
            String url = props.getProperty("jdbc.url");
            String login = props.getProperty("jdbc.login");
            String password = props.getProperty("jdbc.password");
            con = DriverManager.getConnection(url, login, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }

    public void turnConnection() {
        int size = 5;
        logger.info("The size of the pool is : " + size);
        for (int i = 0; i < size; i++) {
            myConnection.add(connectionFactory());
        }
    }

    public void retrieveConnection(Connection con) {
        logger.info("a connection is given back to the pool");
        myConnection.add(con);
    }

    public Connection giveConnection() {
        if (myConnection.size() == 0) {
            try {
                throw new Exception("No connection available! The pool is overload !");
            } catch (Exception e) {
                logger.info(e.getMessage());
                System.exit(-1);
            }
        }
        logger.info("Giving a connection");
        logger.info(myConnection.size() + "connection available");
        Connection con = myConnection.remove(myConnection.size() - 1);
        logger.info(myConnection.size() + " left");
        return con;

    }

    public void closeConnection() {
        for (Connection con : myConnection)
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
