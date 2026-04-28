package org.example;

import lombok.*;

import java.util.*;

import static org.example.DBManager.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Student {

    private int id;
    @NonNull private String emri;
    @NonNull private String email;
    private String birthDate;
    private String phoneNumber;
    private int points;

    public  void createStudent( DBConnection db) {
//        if (this.emri == null || this.email == null) {
//            System.out.println("Error: name and email cannot be null");
//            return;
//        }
        String[] studentCols = {"emri", "email", "birth_date", "phone_number"};

        String fixEmail = "'" + this.email + "'";

        String[] rowVals = {this.emri,this.email, this.birthDate,this.phoneNumber};
        if (rowExistsHelperFun("student", "email", fixEmail, db)) {
            System.out.println("Error: student me kte email ekziston.");
            return;
        }
        Map<String, String> row = new LinkedHashMap<>();
        for (int i = 0; i < studentCols.length; i++) {
            row.put(studentCols[i], rowVals[i]);
        }
        createRow(row, null, "student", db);
    }

    public static void getStudents(DBConnection db){
        execQuery("select * from student",db, "row",null);
    }

    public static void getStudent(DBConnection db, @NonNull String student_id){
        if (!rowExistsHelperFun("student", "student_id", student_id, db)) {
            System.out.println("Error: student nuk ekziston");
            return;
        }
        String query = "select * from student where student_id = "+student_id ;
        execQuery(query,db,"row",null);
    }

    public static void modifyStudent( @NonNull String student_id,@NonNull Map<String,String> changedValues,DBConnection db){
//        if (student_id == null) {
//            System.out.println("Error: Student id cannot be null");
//            return;
//        }
        if (!rowExistsHelperFun("student", "student_id", student_id, db)) {
            System.out.println("Error: student nuk ekziston");
            return;
        }
        List<String> changes = new ArrayList<>();
        for (Map.Entry<String, String> entry : changedValues.entrySet()) {
            changes.add(entry.getKey() + " = '" + entry.getValue() + "'");
        }
        String setString = String.join(" , ", changes);
        String query = "update student set " + setString + " where student_id = " + student_id;
        execUpdate(query, db);

    }

    public static void deleteStudent(@NonNull String student_id, DBConnection db) {
        if (!rowExistsHelperFun("student", "student_id", student_id, db)) {
            System.out.println("Error: student nuk ekziston");
            return;
        }
        Map<String, String> idMap = new LinkedHashMap<>();
        idMap.put("student_id", student_id);
        deleteRow(idMap, "student", db);
    }

}
