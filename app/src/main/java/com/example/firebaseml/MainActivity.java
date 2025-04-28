package com.example.firebaseml;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonTranslate, buttonFaceDetection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTranslate = findViewById(R.id.buttonTranslate);
        buttonFaceDetection = findViewById(R.id.buttonFaceDetection);

        buttonTranslate.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TranslateActivity.class);
            startActivity(intent);
        });

        buttonFaceDetection.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FaceDetectionActivity.class);
            startActivity(intent);
        });
    }
}
