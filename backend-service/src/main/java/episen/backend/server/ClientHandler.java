package episen.backend.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.*;


public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Connection connection;
    private static String[] requestList = new String[10];
    private final static Logger log = LoggerFactory.getLogger(ClientHandler.class.getName());


    // Constructor
    public ClientHandler(Socket socket, Connection connection) {
        this.clientSocket = socket;
        this.connection = connection;


    }

    public void run() {


        ObjectMapper mapper = new ObjectMapper(new JsonFactory());

        try {
            OutputStream out = clientSocket.getOutputStream();
            InputStream in = clientSocket.getInputStream();
            DataOutputStream ds = new DataOutputStream(out);
            DataInputStream di = new DataInputStream(in);

            String request = di.readUTF();
            System.out.println(request);


            Map<String, String> map = mapper.readValue(request.split("@")[1], new TypeReference<Map<String, String>>() {
            });

            if (request.split("@")[0].equals("demoOneSelectAll")) {
                ds.writeUTF(demoOneSelectAll(connection, map).toString());
            }
            if (request.split("@")[0].equals("demoOneSelectId")) {
                ds.writeUTF(demoOneSelectId(connection, map).toString());
            }
            if (request.split("@")[0].equals("demoOneInsert")) {
                ds.writeUTF(demoOneInsert(connection, map).toString());
            }



            // LOCATION


            if (request.split("@")[0].equals("initPlanLocation")) {
                ds.writeUTF(initPlanLocation(connection, map).toString());
            }
            if (request.split("@")[0].equals("getPlace")) {
                ds.writeUTF(getPlace(connection, map).toString());
            }
            if (request.split("@")[0].equals("getListBuilding")) {
                ds.writeUTF(getListBuilding(connection, map).toString());
            }
            if (request.split("@")[0].equals("getListFloor")) {
                ds.writeUTF(getListFloor(connection, map).toString());
            }
            if (request.split("@")[0].equals("getNameBuilding")) {
                ds.writeUTF(getNameBuilding(connection, map).toString());
            }
            if (request.split("@")[0].equals("getNbFloorFree")) {
                ds.writeUTF(getNbFloorFree(connection, map).toString());
            }
            if (request.split("@")[0].equals("getFloorStatu")) {
                ds.writeUTF(getFloorStatu(connection, map).toString());
            }
            if (request.split("@")[0].equals("setFloorStatu")) {
                ds.writeUTF(setFloorStatu(connection, map).toString());
            }
            if (request.split("@")[0].equals("getDispoBat")) {
                ds.writeUTF(getDispoBat(connection, map).toString());
            }
            if (request.split("@")[0].equals("getDispoBatEvo")) {
                ds.writeUTF(getDispoBatEvo(connection, map).toString());
            }
            if (request.split("@")[0].equals("setStatuResa")) {
                ds.writeUTF(setStatuResa(connection, map).toString());
            }
            if (request.split("@")[0].equals("insertCompany")) {
                ds.writeUTF(insertCompany(connection, map).toString());
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*************** starting method request for building indicators*********************/

    // LOCATION

    public StringBuilder initPlanLocation(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        try {

            String sql = "SELECT status FROM room r " +
                    " inner join floor f on r.id_floor = f.id_floor" +
                    " inner join building b on b.id_building = f.id_building" +
                    "    WHERE b.building_name = '" + map.get("rl_building") + "'" +
                    " and f.name_floor='" + map.get("rl_floor") + "';";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                str += (!rs.isLast()) ? rs.getString("status") + "-" : rs.getString("status");
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder getPlace(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        int nb = 0;
        try {

            String sql = "SELECT count(r.id_room) as TOTAL FROM room r " +
                    " inner join floor f on r.id_floor = f.id_floor" +
                    " inner join building b on b.id_building = f.id_building" +
                    "    WHERE b.building_name = '" + map.get("rl_building") + "'" +
                    " and r.status='free';";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                nb = rs.getInt("TOTAL");
            }
            sb.append(nb);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder getListBuilding(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        try {

            String sql = "SELECT building_name FROM building order by id_building";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                str += (!rs.isLast()) ? rs.getString("building_name") + "-" : rs.getString("building_name");
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder getListFloor(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        try {

            String sql = "SELECT distinct(f.name_floor) FROM floor f"+
                    " inner join building b on b.id_building = f.id_building" +
                    " where b.building_name='" + map.get("rl_building") + "'" +
                    " order by f.name_floor";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                str += (!rs.isLast()) ? rs.getString("name_floor") + "-" : rs.getString("name_floor");
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder getNameBuilding(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        try {

            String sql = "SELECT building_name FROM building where id_building='" + map.get("rl_building") + "'";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                str = rs.getString("building_name");
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder getNbFloorFree(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        int nb = 0;
        try {

            String sql = "SELECT count(r.id_room) from room r"+
                    " inner join floor f on r.id_floor = f.id_floor" +
                    " inner join building b on b.id_building = f.id_building" +
                    "    WHERE b.building_name = '" + map.get("rl_building") + "'" +
                    " and f.name_floor='" + map.get("rl_floor") + "'" +
                    " and r.status='free'";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                nb++;
            }
            sb.append(nb);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder getFloorStatu(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        try {

            String sql = "SELECT status from room r"+
                    " inner join floor f on r.id_floor = f.id_floor" +
                    " inner join building b on b.id_building = f.id_building" +
                    "    WHERE b.building_name = '" + map.get("rl_building") + "'" +
                    " and f.name_floor='" + map.get("rl_floor") + "'";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                str += (!rs.isLast()) ? rs.getString("status") + "-" : rs.getString("status");
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder setFloorStatu(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        String[] f = map.get("rl_floor_to_set").split("-");
        int index = 0;
        try {
            for(int i = 0; i < 10; i++) {
                index = i+1;
                String sql = "update room as r" +
                        " set status = '" + f[i] + "'" +
                        " from floor as f" +
                        " join building as b on b.id_building = f.id_building" +
                        "  where f.id_floor = r.id_floor " +
                        " and b.building_name = '" + map.get("rl_building") + "'" +
                        " and f.name_floor='" + map.get("rl_floor") + "'" +
                        " and RIGHT(cast(id_room as varchar),1)='" + index + "';";
                connection.createStatement().executeQuery(sql);
                System.out.println(sql);
                sb = new StringBuilder();
                sb.append("Update done");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder getDispoBat(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        try {

            String sql = "select id_room, name_floor from room r"+
                    " inner join floor f on r.id_floor = f.id_floor"+
                    " inner join building b on b.id_building = f.id_building"+
                    " WHERE b.building_name = '" + map.get("rl_building") + "' and r.status='free' " +
                    " order by id_room" +
                    " LIMIT " + map.get("rl_nb_loc");
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            sb = new StringBuilder();
            while (rs.next()) {
                str += (!rs.isLast()) ? rs.getString("id_room") + "//" + rs.getString("name_floor") + "-" :
                        rs.getString("id_room") + "//" + rs.getString("name_floor");
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder getDispoBatEvo(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        try {
            sb = new StringBuilder();
            String sql = "select id_room, name_floor, name_room, price from room r"+
                    " inner join floor f on r.id_floor = f.id_floor"+
                    " inner join building b on b.id_building = f.id_building"+
                    " WHERE b.building_name = '" + map.get("rl_building") + "' and r.status='free' and name_room like 'Salle de conf%'" +
                    " order by id_room" +
                    " LIMIT " + map.get("rl_nb_loc_sc");
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            while (rs.next()) {
                str += rs.getString("id_room") + "//" + rs.getString("name_floor") + "//" + rs.getString("name_room") + "//" + rs.getString("price") + "-";
            }
            sb.append(str);

            str = "";
            String sql1 = "select id_room, name_floor, name_room, price from room r"+
                    " inner join floor f on r.id_floor = f.id_floor"+
                    " inner join building b on b.id_building = f.id_building"+
                    " WHERE b.building_name = '" + map.get("rl_building") + "' and r.status='free' and name_room like 'Bureau%'" +
                    " order by id_room" +
                    " LIMIT " + map.get("rl_nb_loc_b");
            ResultSet rs1 = connection.createStatement().executeQuery(sql1);
            System.out.println(sql1);
            while (rs1.next()) {
                str += rs1.getString("id_room") + "//" + rs1.getString("name_floor") + "//" + rs1.getString("name_room") + "//" + rs1.getString("price") + "-";
            }
            sb.append(str);

            str = "";
            String sql2 = "select id_room, name_floor, name_room, price from room r"+
                    " inner join floor f on r.id_floor = f.id_floor"+
                    " inner join building b on b.id_building = f.id_building"+
                    " WHERE b.building_name = '" + map.get("rl_building") + "' and r.status='free' and name_room like 'Salle ouverte%'" +
                    " order by id_room" +
                    " LIMIT " + map.get("rl_nb_loc_so");
            ResultSet rs2 = connection.createStatement().executeQuery(sql2);
            System.out.println(sql2);
            while (rs2.next()) {
                str += rs2.getString("id_room") + "//" + rs2.getString("name_floor") + "//" + rs2.getString("name_room") + "//" + rs2.getString("price") + "-";
            }
            sb.append(str);

            str = "";
            String sql3 = "select id_room, name_floor, name_room, price from room r"+
                    " inner join floor f on r.id_floor = f.id_floor"+
                    " inner join building b on b.id_building = f.id_building"+
                    " WHERE b.building_name = '" + map.get("rl_building") + "' and r.status='free' and name_room like 'Petite salle%'" +
                    " order by id_room" +
                    " LIMIT " + map.get("rl_nb_loc_ps");
            ResultSet rs3 = connection.createStatement().executeQuery(sql3);
            System.out.println(sql3);
            while (rs3.next()) {
                str += rs3.getString("id_room") + "//" + rs3.getString("name_floor") + "//" + rs3.getString("name_room") + "//" + rs3.getString("price") + "-";
            }
            sb.append(str);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder setStatuResa(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        String[] f = map.get("rl_resa").split("-");
        try {
            for(int i = 0; i < f.length; i++) {
                String idRoom = f[i].trim();
                String sql = "update room" +
                        " set position_sensor = 't'," +
                        " position_screen = 't'," +
                        " position_plug = 't'," +
                        " position_windows = 't'," +
                        " id_location = " + idRoom +
                        " where id_room='" + idRoom + "';";
                connection.createStatement().executeUpdate(sql);
                System.out.println(sql);
                sb = new StringBuilder();
                sb.append("Update done");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    public StringBuilder insertCompany(Connection connection, Map<String, String> map) {
        StringBuilder sb = null;
        String str = "";
        int idCompany = 0;
        String[] f = map.get("rl_resp").split("-");
        try {

            String sql = "select MAX(company_id) as max from company";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            System.out.println(sql);
            while (rs.next()) {
                idCompany = rs.getInt("max");
            }
            idCompany++;

            String sql2 = "insert into company (company_id, company_name, begin_location) " +
                    "values (" + idCompany +
                    ",'" + map.get("rl_company_name") + "',now());";
            connection.createStatement().executeUpdate(sql2);
            System.out.println(sql2);
            sb = new StringBuilder();
            sb.append("Insert done");

            for(int i = 0; i < f.length; i++) {
                String idRoom = f[i].split("//")[0];
                String sql3 = "update location" +
                        " set company_id = " + idCompany +
                        " where id_location = "+ idRoom;
                connection.createStatement().executeUpdate(sql3);
                System.out.println(sql3);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb;
    }

    //--------------------------------------------------------------------

    public StringBuilder demoOneSelectAll(Connection connection, Map<String, String> map) {
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

    public StringBuilder demoOneSelectId(Connection connection, Map<String, String> map) {
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

    public StringBuilder demoOneInsert(Connection connection, Map<String, String> map) {
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
}



