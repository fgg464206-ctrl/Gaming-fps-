package com.housemaster.shizukuapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import rikka.shizuku.Shizuku;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setPadding(40, 40, 40, 40);
        setContentView(textView);

        if (Shizuku.pingBinder()) {
            textView.setText("Shizuku Service is Active! 🔥\n\nCredit: house master | king smp. arv");
            checkShizukuPermission();
        } else {
            textView.setText("Shizuku is not running. Please start Shizuku first! ⚠️");
        }
    }

    private void checkShizukuPermission() {
        if (Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission already granted!", Toast.LENGTH_SHORT).show();
        } else {
            Shizuku.requestPermission(101);
        }
    }
                            }

