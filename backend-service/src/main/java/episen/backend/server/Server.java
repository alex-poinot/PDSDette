package episen.backend.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import episen.backend.pool.DataSource;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.Properties;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class.getName());
    private ServerSocket server;


    public Server(ServerConfiguration c) {

        try {
            this.server = new ServerSocket(c.getConfiguration().getPort());
            Properties props = new Properties();
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties"));
            logger.debug("Server starting...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverService(DataSource ds) {
        try {
            while (true) {
                // socket object to receive incoming client
                Socket client = server.accept();
                logger.debug("A new request!");

                //create a new thread object
                Connection connection = ds.giveConnection();
                ClientHandler clientSocket = new ClientHandler(client, connection);

                // This thread will handle the client
                new Thread(clientSocket).start();
                ds.retrieveConnection(connection); // je rend la connexion

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
