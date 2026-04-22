package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {
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

    public static List<Object> rowHelperFun(ResultSet result) throws SQLException {
        ResultSetMetaData rsmd = result.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List<Object> row = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            row.add(result.getObject(i));
        }
        return row;
    }

    public static void QueryResultPrinter(String q, DbConn props, String qType, String cName) {
        try (Connection conn = DriverManager.getConnection(props.url, props.user, props.pass)) {
            Statement stmt = conn.createStatement();
            stmt.execute(q);
            ResultSet result = stmt.executeQuery(q);

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

    public static void QueryUpdate(String q, DbConn props) {
        try (Connection conn = DriverManager.getConnection(props.url, props.user, props.pass)) {
            Statement s = conn.createStatement();

            int rowsAffected = s.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);
            if (rowsAffected > 0) {
                ResultSet rs = s.getGeneratedKeys();
                System.out.println("Query Successful , updated/inserted/deleted row: ");
                while (rs.next()) {
                    System.out.println(rowHelperFun(rs));
                }
                rs.close();
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        DbConn props = new DbConn("jdbc:postgresql://localhost:5433/internship", "postgres", "openpgpwd");

        String allTablesQuery = "select * from pg_catalog.pg_tables where tablename not like 'pg_%' and tablename not like 'sql_%'";
        String allRowsKursi = "select * from kursi";
        String studentsPointsMoreThanTen = "select * from student where points > 10";
        String addStudent = "insert into student (emri, email, birth_date, phone_number, points) values ('Filan Fisteku','student@email.com','01-01-2001','0681231234',11)";
        String showStudents = "select * from student";
        String updatePointsStudent = "update student set points = 100 where student_id = 69";
        String deleteStudent = "delete from student where student_id = 69";



        QueryResultPrinter(allTablesQuery,props,"col","tablename" );
        QueryResultPrinter(allRowsKursi,props,"row",null );
        QueryResultPrinter(studentsPointsMoreThanTen,props,"row",null );

        QueryUpdate(addStudent,props);
        QueryUpdate(updatePointsStudent, props);
        QueryUpdate(deleteStudent, props);



    }
}
