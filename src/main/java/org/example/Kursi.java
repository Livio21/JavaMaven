package org.example;

import lombok.*;
import java.util.*;
import static org.example.DBManager.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Kursi {

    private int id;
    @NonNull private String emerKursi;
    private String kohezgjatja;
    private String create_date;
    private String update_date;

    public void createKurs(DBConnection db) {
        String fixEmer = "'" + this.emerKursi + "'";
        if (rowExistsHelperFun("kursi", "emer_kursi",fixEmer, db)) {
            System.out.println("Error: kursi me kte emer ekziston.");
            return;
        }
        String[] kursiCols = {"emer_kursi", "kohezgjatja", "create_date", "update_date"};
        String[] rowVals = {this.emerKursi, this.kohezgjatja, this.create_date, this.update_date};

        Map<String, String> row = new LinkedHashMap<>();
        for (int i = 0; i < kursiCols.length; i++) {
            row.put(kursiCols[i], rowVals[i]);
        }
        createRow(row, null, "kursi", db);
    }

    public static void getKurset(DBConnection db) {
        execQuery("select * from kursi", db, "row", null);
    }

    public static void getKursi(DBConnection db, @NonNull String kursi_id) {
        if (!rowExistsHelperFun("kursi", "kursi_id", kursi_id, db)) {
            System.out.println("Error: kursi nuk ekziston");
            return;
        }
        String query = "select * from kursi where kursi_id = " + kursi_id;
        execQuery(query, db, "row", null);
    }

    public static void modifyKursi(@NonNull String kursi_id, @NonNull Map<String, String> changedValues, DBConnection db) {
        if (!rowExistsHelperFun("kursi", "kursi_id", kursi_id, db)) {
            System.out.println("Error: kursi nuk ekziston.");
            return;
        }
        List<String> changes = new ArrayList<>();
        for (Map.Entry<String, String> entry : changedValues.entrySet()) {
            changes.add(entry.getKey() + " = '" + entry.getValue() + "'");
        }
        String setString = String.join(" , ", changes);
        String query = "update kursi set " + setString + " where kursi_id = " + kursi_id;
        execUpdate(query, db);
    }

    public static void deleteKursi(@NonNull String kursi_id, DBConnection db) {
        if (!rowExistsHelperFun("kursi", "kursi_id", kursi_id, db)) {
            System.out.println("Error: kursi nuk ekziston.");
            return;
        }
        Map<String, String> idMap = new LinkedHashMap<>();
        idMap.put("kursi_id", kursi_id);
        deleteRow(idMap, "kursi", db);
    }
}