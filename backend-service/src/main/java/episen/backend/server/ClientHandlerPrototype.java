package episen.backend.server;

import episen.backend.pool.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static episen.backend.prototype.BackendService.startCrud;
import static java.lang.Thread.sleep;

public class ClientHandlerPrototype implements Runnable{
    @Override
    public void run() {

        DataSource ds = new DataSource();
        Connection co = ds.getInstance().giveConnection();
        try {
            startCrud(true, co);
            sleep(10000);
            ds.getInstance().retrieveConnection(co);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
