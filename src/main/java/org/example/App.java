package org.example;

import java.util.LinkedHashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        //Krijimi i nje db connection object
        DBConnection db = new DBConnection("jdbc:postgresql://localhost:5433/internship", "postgres", "openpgpwd");


        //koloneTip per te krijuar nje tabel kursi
        Map<String, String> koloneTip = new LinkedHashMap<>();
        koloneTip.put("kursi_id", "serial primary key");
        koloneTip.put("emer_kursi", "varchar(100)");
        koloneTip.put("kohezgjatja", "varchar(50)");
        koloneTip.put("create_date", "date");
        koloneTip.put("update_date", "date");

        DBManager.createTable(koloneTip, "kursi", db);

        //koloneTip per te krijuar nje tabel student
        Map<String, String> koloneTipStudent = new LinkedHashMap<>();
        koloneTipStudent.put("student_id", "serial primary key");
        koloneTipStudent.put("emri", "varchar(100)");
        koloneTipStudent.put("email", "varchar(50)");
        koloneTipStudent.put("birth_date", "date");
        koloneTipStudent.put("phone_number", "varchar(15)");
        koloneTipStudent.put("points", "int");

        DBManager.createTable(koloneTipStudent, "student", db);

        //Krijimi i nje objekti kursi dhe shtimi si rresht tek tabela ne db
        Kursi k = Kursi.builder().emerKursi("Java Basics").kohezgjatja("3 months").create_date("10-10-2010").update_date("10-10-2010").build();
        k.createKurs(db);
        Kursi.getKurset(db);
        Kursi.getKursi(db, "1");

        //Ndryshimi i nje rreshti duke perdorur emrin e kolones dhe vleren
        Map<String, String> kursiChanges = new LinkedHashMap<>();
        kursiChanges.put("emer_kursi", "Java Advanced");
        Kursi.modifyKursi("1", kursiChanges, db);

        //Fshirja e nje rreshti me id 1
        Kursi.deleteKursi("1", db);

        //Krijimi i nje objekti student dhe shtimi si rresht tek tabela ne db
        Student s = Student.builder().emri("test test").email("test@email.com").birthDate("10-10-1010").phoneNumber("12345678").build();
        s.createStudent(db);

        //Kthen nje student me id 1
        Student.getStudent(db, "1");
        //Kthen te gjithe studentet
        Student.getStudents(db);


        //Ndryshimi i nje rreshti duke perdorur emrin e kolones dhe vleren
        Map<String, String> changes = new LinkedHashMap<>();
        changes.put("emri", "John Doe");
        changes.put("email", "john@email.com");
        Student.modifyStudent("1", changes, db);

//        Map<String, String> deleteMap = new LinkedHashMap<>();
//        deleteMap.put("student_id", "1");
//        DBManager.deleteRow(deleteMap, "student", db);

        //Fshirja e nje studenti me id 1
        Student.deleteStudent("1",db);

        //Fshirja e nje tabele
        DBManager.deleteTable("kursi", db);
    }
}
