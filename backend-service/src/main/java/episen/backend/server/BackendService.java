package episen.backend.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import episen.backend.pool.DataSource;
import episen.backend.pool.PropertiesClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;


public class BackendService {

    private static final Logger logger = LoggerFactory.getLogger(BackendService.class.getName());


    public static void main(String[] args) throws IOException {

        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties"));

        int maxCo = 5;

        DataSource ds = DataSource.getInstance();

        Options options = new Options();
        options.addOption("testMode", false, "a simple cli option to run this service as test, default is false");

        final Option maxConnection = Option.builder().longOpt("maxConnections")
                .hasArg()
                .argName("maxConnections")
                .desc("specify max connections in this run, default set to 10")
                .build();

        options.addOption(maxConnection);

        final Option sqlReq = Option.builder().longOpt("sqlReq")
                .hasArg()
                .argName("sqlReq")
                .desc("specify witch request you want to set on the db")
                .build();

        options.addOption(sqlReq);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Options parser error : " + e.getMessage());
        }

        String loggerInfo = "BackendService is running.";
        if (cmd.hasOption("testMode")) {
           // isInTestMode = true;
            loggerInfo += "\n Test mode is on";
        }
        if (cmd.hasOption("maxConnections")) {
            maxCo = Integer.parseInt(cmd.getOptionValue("maxConnections"));
            loggerInfo += "\n Max connections has been set to " + maxCo;

        }
//        if(!yamlProps.getSqlReq().isEmpty()){
//            executeSqlRequest(ds,yamlProps.getSqlReq());
//        }

        if(cmd.hasOption("sqlReq")){
            String request =cmd.getOptionValue("sqlReq");
            Connection con = ds.giveConnection();
            //logger.info(request);
            try(Statement stm= con.createStatement();
                ResultSet result = stm.executeQuery(request)){
                stm.execute(request);
                ResultSetMetaData rsmd = result.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while(result.next()){
                    for(int i = 1; i <= columnsNumber; i++) {
                        if (i < columnsNumber) {
                            logger.info(result.getString(i));
                        } else {
                            logger.info(result.getString(i) + " / ");
                        }
                    }
                }
            } catch (Exception e){
                logger.error("Error with executing to db : " +e.getMessage());
            }
            finally {
                ds.retrieveConnection(con);
            }
        }
        logger.info(loggerInfo);
        Server server = new Server(new ServerConfiguration(10));
        server.serverService(ds);


    }

    private static void executeSqlRequest(DataSource ds, String sqlReq){
        Connection con = ds.giveConnection();
        try(Statement stm = con.createStatement(); ResultSet result = stm.executeQuery(sqlReq)){
            stm.execute(sqlReq);
            ResultSetMetaData rsmd = result.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while(result.next()){
                for(int i = 1; i <= columnsNumber; i++) {
                    if (i < columnsNumber) {
                        logger.info(result.getString(i));
                    } else {
                        logger.info(result.getString(i) + " / ");
                    }
                }
            }
        } catch (Exception e){
            logger.error("Error with executing to db : " +e.getMessage());
        }
        finally {
            ds.retrieveConnection(con);
        }

    }

}

