package org.example;


import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppOld {

    //Class for keeping db connection properties
    public static class DbConn {
        String url;
        String user;
        String pass;


        public DbConn(String url, String user, String pass) {
            this.url = url;
            this.user = user;
            this.pass = pass;
        }
    }


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


    // Execute a query, check what type if to print a column or return a row
    public static void queryResultPrinter(String q, DbConn props, String qType, String cName) {
        try (Connection conn = DriverManager.getConnection(props.url, props.user, props.pass);
             Statement stmt = conn.createStatement();
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


    public static List<Object> queryResult(String q, DbConn props) {

        List<Object> rows = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(props.url, props.user, props.pass);
             Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery(q);
        ) {
            while (result.next()) {
                rows.add(rowHelperFun(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    //Function which handles most data manipulations like inserts, alter and delete plus prints the affected row
    public static void queryUpdate(String q, DbConn props) {
        String info = "";
        boolean needsFollowUp = false;

        if (q.matches("(?i)insert.*")) {
            info = "Query Successful, inserted a new row: ";
        } else if (q.matches("(?i)update.*")) {
            info = "Query Successful, changed a row: ";
            needsFollowUp = true;
        } else if (q.matches("(?i)delete.*")) {
            info = "Query Successful, deleted a row: ";
            needsFollowUp = true;
        }

        try (Connection conn = DriverManager.getConnection(props.url, props.user, props.pass)) {
            Statement s = conn.createStatement();

            int rowsAffected = s.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);

            if (rowsAffected > 0) {
                System.out.println(info);

                if (needsFollowUp) {
                    Pattern p = Pattern.compile("\\w+_id");
                    Pattern p1 = Pattern.compile("\\w+_id\\s+=\\s+(\\d+)");
                    Matcher m = p.matcher(q);
                    Matcher m1 = p1.matcher(q);
                    System.out.println("m found: " + m.find());
                    System.out.println("m1 found: " + m1.find());
                    System.out.println("Query: " + q);
                    String showAffectedQuery = "select * from " + m.group().split("_")[0] + " where " + m.group() + " = " + m1.group(1);
                    queryResultPrinter(showAffectedQuery, props, "row", null);
                } else {
                    ResultSet rs = s.getGeneratedKeys();
                    while (rs.next()) {
                        System.out.println(rowHelperFun(rs));
                    }
                    rs.close();
                }
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Using lang3 dependency string utils example, rotating names of students
    public static void rotateName(List<Object> rows, int n, DbConn props) {

       for (Object row : rows) {
            List<Object> columns = (List<Object>) row;
            String name = (String) columns.get(1);
            int id = (int) columns.get(0);
            // rotate and update
            String rotatedName = StringUtils.rotate(name, n);

            String updateStudentName = "update student set emri = "+"'" + rotatedName +"'" + " where student_id = " + id;
            queryUpdate(updateStudentName,props);

        }

    }


    //Using lang3 dependency string utils example, change to uppercase the names of students
    public static void upperCaseName(List<Object> rows, DbConn props) {

        for (Object row : rows) {
            List<Object> columns = (List<Object>) row;
            String name = (String) columns.get(1);
            int id = (int) columns.get(0);
            // rotate and update
            String rotatedName = StringUtils.upperCase(name);

            String updateStudentName = "update student set emri = "+"'" + rotatedName +"'" + " where student_id = " + id;
            queryUpdate(updateStudentName,props);

        }

    }


    public static void main(String[] args) {
        DbConn props = new DbConn("jdbc:postgresql://localhost:5433/internship", "postgres", "openpgpwd");

        String allTablesQuery = "select * from pg_catalog.pg_tables where tablename not like 'pg_%' and tablename not like 'sql_%'";
        String allRowsKursi = "select * from kursi";
        String studentsPointsMoreThanTen = "select * from student where points > 10";
        String addStudent = "insert into student (emri, email, birth_date, phone_number, points) values ('Filan Fisteku','student@email.com','01-01-2001','0681231234',11)";
        String showStudents = "select * from student";
        String updatePointsStudent = "update student set points = 100 where student_id = 74";
        String deleteStudent = "delete from student where student_id = 75";

//        queryResultPrinter(allTablesQuery, props, "col", "tablename");
//        queryResultPrinter(allRowsKursi, props, "row", null);
//        queryResultPrinter(studentsPointsMoreThanTen, props, "row", null);
//
////        queryUpdate(addStudent, props);
//        queryUpdate(updatePointsStudent, props);
//        queryUpdate(deleteStudent, props);

//        rotateName(queryResult(showStudents,props), 3,props);
        upperCaseName(queryResult(showStudents,props), props);

    }
}
