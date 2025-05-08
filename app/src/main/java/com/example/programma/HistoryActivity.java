package com.example.programma;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyList = findViewById(R.id.history_list);
        Button btnExport = findViewById(R.id.btnExport);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        List<String> history = dbHelper.getAllInstallations();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, history);
        historyList.setAdapter(adapter);

        btnExport.setOnClickListener(v -> {
            // Demander la permission d’écrire si nécessaire
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                exportToCSV(history);
            }
        });

    }

    private void exportToCSV(List<String> data) {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "ProgrammaExports");
        if (!exportDir.exists()) exportDir.mkdirs();

        File file = new File(exportDir, "historique_installations.csv");

        try {
            FileWriter writer = new FileWriter(file);
            writer.append("Logiciel,Date d'installation\n");
            for (String line : data) {
                String[] split = line.split(" \\(le ");
                String logiciel = split[0];
                String date = split[1].replace(")", "");
                writer.append(logiciel).append(",").append(date).append("\n");
            }
            writer.flush();
            writer.close();
            Toast.makeText(this, "Fichier exporté dans " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur d'exportation", Toast.LENGTH_SHORT).show();
        }
    }
}
