package com.example.programma;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class InstallationActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressText;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.installation_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 10;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            progressText.setText(progressStatus + "0 % terminé");
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        DatabaseHelper dbHelper = new DatabaseHelper(InstallationActivity.this);
        String selectedSoftwares = getIntent().getStringExtra("logiciels");
        String currentDate = new SimpleDateFormat("JJ/MM/AAAA HH:mm", Locale.getDefault()).format(new Date());
        dbHelper.insertInstallation(selectedSoftwares, currentDate);
    }
}
