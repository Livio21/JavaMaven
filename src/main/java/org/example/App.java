package org.example;

import java.util.LinkedHashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) {

        DBConnection db = new DBConnection("jdbc:postgresql://localhost:5433/internship", "postgres", "openpgpwd");

        Map<String, String> koloneTip = new LinkedHashMap<>();
        koloneTip.put("kursi_id", "serial primary key");
        koloneTip.put("emer_kursi", "varchar(100)");
        koloneTip.put("kohezgjatja", "varchar(50)");
        koloneTip.put("create_date", "date");
        koloneTip.put("update_date", "date");

        DBManager.createTable(koloneTip, "kursi", db);

        Map<String, String> koloneTipStudent = new LinkedHashMap<>();
        koloneTipStudent.put("student_id", "serial primary key");
        koloneTipStudent.put("emri", "varchar(100)");
        koloneTipStudent.put("email", "varchar(50)");
        koloneTipStudent.put("birth_date", "date");
        koloneTipStudent.put("phone_number", "varchar(15)");
        koloneTipStudent.put("points", "int");

        DBManager.createTable(koloneTipStudent, "student", db);


        Kursi k = Kursi.builder().emerKursi("Java Basics").kohezgjatja("3 months").create_date("10-10-2010").update_date("10-10-2010").build();
        k.createKurs(db);
        Kursi.getKurset(db);
        Kursi.getKursi(db, "1");

        Map<String, String> kursiChanges = new LinkedHashMap<>();
        kursiChanges.put("emer_kursi", "Java Advanced");
        Kursi.modifyKursi("1", kursiChanges, db);
        Kursi.deleteKursi("1", db);

        Student s = Student.builder().emri("test test").email("test@email.com").birthDate("10-10-1010").phoneNumber("12345678").build();
        s.createStudent(db);

        Student.getStudent(db, "1");
        Student.getStudents(db);

        Map<String, String> changes = new LinkedHashMap<>();
        changes.put("emri", "John Doe");
        changes.put("email", "john@email.com");
        Student.modifyStudent("1", changes, db);

        Map<String, String> deleteMap = new LinkedHashMap<>();
        deleteMap.put("student_id", "1");
        DBManager.deleteRow(deleteMap, "student", db);

        DBManager.deleteTable("kursi", db);
    }
}
