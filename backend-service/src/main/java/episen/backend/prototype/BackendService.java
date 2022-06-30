package episen.backend.prototype;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import episen.backend.pool.DataSource;
import episen.backend.server.ClientHandlerPrototype;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static java.lang.Thread.sleep;


public class BackendService {
    private static final Logger logger = LoggerFactory.getLogger(BackendService.class.getName());
    private static DataSource dataSource;
    private static ServerSocket serverSocket;
    private static Socket socket;


    public BackendService() {
    }

    public static void main(String[] args) throws IOException {
        dataSource = DataSource.getInstance();

        try {
            final Options options = new Options();
            final Option mode = Option.builder().longOpt("mode").hasArg().argName("mode").build();
            final Option modecrud = Option.builder().longOpt("modecrud").argName("modecrud").build();
            options.addOption(mode);
            options.addOption(modecrud);

            final CommandLineParser commandLineParser = new DefaultParser();
            final CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.hasOption("mode")) {
                String requestmode = commandLine.getOptionValue("mode");
                logger.info(requestmode);
                if (requestmode.contains("json")) {
                    receiveJson();
                } else if (requestmode.contains("crud")) {
                    startCrud(false, dataSource.giveConnection());
                } else if (requestmode.contains("overload")) {
                    while (true) {

                        serverSocket = new ServerSocket(5039);
                        serverSocket.setSoTimeout(60000);
                        socket = serverSocket.accept();
                        new Thread(new ClientHandlerPrototype()).start();
                    }
                }

            }

        } catch (ParseException | SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void startCrud(boolean overload, Connection co) throws IOException, SQLException, InterruptedException {

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            DataOutputStream ds = new DataOutputStream(out);
            DataInputStream di = new DataInputStream(in);
            String crud_op = di.readUTF();
            logger.info(crud_op);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String request = di.readUTF();
            logger.info(request);
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());

            Map<String, String> map = mapper.readValue(request.split("@")[1], new TypeReference<Map<String, String>>() {
            });
            String req = request.split("@")[0];
            co = dataSource.giveConnection();
            if(crud_op.equals("read")) {
                ds.writeUTF(demoOneSelectFirst(co, map).toString());
            }
            if (request.split("@")[0].equals("demoOneSelectAll")) {
                ds.writeUTF(demoOneSelectAll(co, map).toString());
            }
            if (request.split("@")[0].equals("demoOneSelectId")) {
                ds.writeUTF(demoOneSelectId(co, map).toString());
            }
            if (request.split("@")[0].equals("demoOneSelectFirst")) {
                ds.writeUTF(demoOneSelectFirst(co, map).toString());
            }
            if (request.split("@")[0].equals("demoOneInsert")) {
                ds.writeUTF(demoOneInsert(co, map).toString());
            }
            if (request.split("@")[0].equals("demoOneUpdate")) {
                ds.writeUTF(demoOneUpdate(co, map).toString());
            }
            if (request.split("@")[0].equals("demoOneDelete")) {
                ds.writeUTF(demoOneDelete(co, map).toString());
            }

            if (overload) {

            }
            dataSource.retrieveConnection(co);

        }

    private static StringBuilder demoOneSelectAll(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        try {

            String sql = "select * from demoone";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                str += (!rs.isLast()) ? rs.getString("DO_ID") + " - " + rs.getString("DO_Text") + "//"
                        : rs.getString("DO_ID") + " - " + rs.getString("DO_Text");
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    private static StringBuilder demoOneSelectId(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        String id = map.get("id");
        try {

            String sql = "select * from demoone where DO_ID='" + id +"'";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                str += rs.getString("DO_ID") + " - " + rs.getString("DO_Text");
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    private static StringBuilder demoOneSelectFirst(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        try {

            String sql = "select * from demoone where DO_ID='1'";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                str += rs.getString("DO_ID") + " - " + rs.getString("DO_Text");
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    private static StringBuilder demoOneInsert(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String text = map.get("text");
        try {

            String sql = "INSERT INTO DEMOONE (DO_Text) VALUES ('" + text + "')";
            connection.createStatement().executeUpdate(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            sb.append("DONE");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    private static StringBuilder demoOneUpdate(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;

        try {
            String sql = "Update DEMOONE set DO_Text = '" + map.get("text") + "'where DO_ID ='" + map.get("id") + "'";
            connection.createStatement().executeUpdate(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            sb.append("Update done.");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public static String demoOneDelete(Connection connection, Map<String, String> map) {
        String sb = null;

        try {
            String sql = "DELETE from DEMOONE " +
                    "where DO_ID ='" + map.get("id") + "'";
            connection.createStatement().executeUpdate(sql);
            System.out.println(sql);
            sb = "Delete done.";

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    private static void receiveJson() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5039);
        serverSocket.setSoTimeout(60000);
        Socket socket;
        while (true) {
            socket = serverSocket.accept();
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            DataOutputStream ds = new DataOutputStream(out);
            DataInputStream di = new DataInputStream(in);
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String request = di.readUTF();
            logger.info(request);
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());

            Map<String, String> map = mapper.readValue(request.split("@")[1], new TypeReference<Map<String, String>>() {
            });
            if (request.split("@")[0].equals("Etudiant1")) {
                ds.writeUTF("Bonjour " + map.get("nom") + " " + map.get("prenom") + " vous avez " + map.get("age") + " ans");
            }
            if (request.split("@")[0].equals("Etudiant2")) {
                ds.writeUTF("Bonjour " + map.get("nom") + " " + map.get("prenom") + " vous avez " + map.get("age") + " ans");
            }
            if (request.split("@")[0].equals("Etudiant3")) {
                ds.writeUTF("Bonjour " + map.get("nom") + " " + map.get("prenom") + " vous avez " + map.get("age") + " ans");
            }
            if (request.split("@")[0].equals("Etudiant4")) {
                ds.writeUTF("Bonjour " + map.get("nom") + " " + map.get("prenom") + " vous avez " + map.get("age") + " ans");
            }
            if (request.split("@")[0].equals("Etudiant5")) {
                ds.writeUTF("Bonjour " + map.get("nom") + " " + map.get("prenom") + " vous avez " + map.get("age") + " ans");
            }
            if (request.split("@")[0].equals("Etudiant6")) {
                ds.writeUTF("Bonjour " + map.get("nom") + " " + map.get("prenom") + " vous avez " + map.get("age") + " ans");
            }
            if (request.split("@")[0].equals("Etudiant7")) {
                ds.writeUTF("Bonjour " + map.get("nom") + " " + map.get("prenom") + " vous avez " + map.get("age") + " ans");
            }
            if (request.split("@")[0].equals("Etudiant22")) {
                ds.writeUTF("Bonjour " + map.get("nom") + " " + map.get("prenom") + " vous avez " + map.get("age") + " ans");
            }
        }
    }
}




