package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBManager {


    // Use the resultset and metadata to loop through and get all columns and return a list of them
    public static List<Object> rowHelperFun(ResultSet result) throws SQLException {
        ResultSetMetaData rsmd = result.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List<Object> row = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            row.add(result.getObject(i));
        }
        return row;
    }

    public static boolean rowExistsHelperFun(String tableName, String col, String id, DBConnection db) {
        if (id == null) return false;
        String query = "select count(*) from " + tableName + " where " + col+ " = " + id;
        try (Statement s = db.Connect().createStatement();
             ResultSet rs = s.executeQuery(query)) {
            if (rs.next()) {
//                System.out.println( rs.getInt(1));
                return rs.getInt(1) > 0 ? true : false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static void execQuery(String q, DBConnection db) {
//        ResultSet rs = null;
//        try (Statement s = db.Connect().createStatement()) {
//            rs = s.executeQuery(q);
//            while (rs.next()) {
//                System.out.println(rowHelperFun(rs));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    // Execute a query, check what type if to print a column or return a row
    public static void execQuery(String q, DBConnection db, String qType, String cName) {
        try (Statement stmt = db.Connect().createStatement();
             ResultSet result = stmt.executeQuery(q);
        ) {
            while (result.next()) {
                switch (qType) {
                    case "col":
                        if (cName != null) {
                            System.out.println(result.getString(cName));
                        }
                        break;
                    case "row":
                        System.out.println(rowHelperFun(result));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void execUpdate(String q, DBConnection db) {
        String info = "";

        if (q.matches("(?i)insert.*")) {
            info = "Query Successful, inserted a new row: ";
        } else if (q.matches("(?i)update.*")) {
            info = "Query Successful, changed a row: ";
        } else if (q.matches("(?i)delete.*")) {
            info = "Query Successful, deleted a row: ";
        }else if (q.matches("(?i)create.*")) {
            info = "Query Successful, created a new table: ";
        }

        try (Statement s = db.Connect().createStatement();) {
            int rowsAffected = s.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);

            if (rowsAffected > 0) {
                System.out.println(info);
                    ResultSet rs = s.getGeneratedKeys();
                    while (rs.next()) {
                        System.out.println("Affected row: " + rowHelperFun(rs));
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(Map<String, String> koloneTip, String tableName, DBConnection db) {
        String firstHalf = "create table " + tableName + " ( ";
        String secondHalf = "";
        List<String> columns = new ArrayList<>();
        for (String kolone : koloneTip.keySet()) {
            columns.add(kolone + " " + koloneTip.get(kolone));
        }
        secondHalf = String.join(" , ", columns);
        execUpdate(firstHalf + secondHalf + " )", db);

    }


    public static void deleteTable(String tableName, DBConnection db) {
        execUpdate("drop table " + tableName, db);
    }


    public static void createRow(Map<String, String> koloneVlere, String[] vals, String tableName, DBConnection db) {


//        String colId = koloneVlere.keySet().iterator().next();
//        String idVal = koloneVlere.get(colId);
//
//        if (rowExistsHelperFun(tableName, colId, idVal, db)) {
//            System.out.println("Error: row exists.");
//            return;
//        }

        String query = "insert into " + tableName + " ";

        if (koloneVlere != null) {
            List<String> values = new ArrayList<>(koloneVlere.values());
            List<String> columns = new ArrayList<>(koloneVlere.keySet());
            String col = "(" + String.join(",", columns) + ")";
            String val = " values ('" + String.join("','", values) + "' )";
            query = query + col + val;
        } else {
            if (vals != null) {
                String val = " values (default,'" + String.join("','", vals) + "' )";
                query = query + val;
            }
        }
        execUpdate(query, db);
    }

    public static void deleteRow(Map<String, String> koloneVlere, String tableName, DBConnection db){
        String colId = koloneVlere.keySet().iterator().next();
        String idVal = koloneVlere.get(colId);
        if (idVal == null) {
            System.out.println("Error: id cannot be null");
            return;
        }
        if (!rowExistsHelperFun(tableName, colId, idVal, db)) {
            System.out.println("Error: row does not exist");
            return;
        }
        String query = "delete from " + tableName + " where " + colId + " = " + idVal ;
        execUpdate(query,db);

    }



}
