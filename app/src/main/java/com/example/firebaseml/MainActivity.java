package com.example.firebaseml;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextInput;
    private Button buttonTranslate;
    private TextView textViewTranslated;
    private Translate translateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInput = findViewById(R.id.editTextInput);
        buttonTranslate = findViewById(R.id.buttonTranslate);
        textViewTranslated = findViewById(R.id.textViewTranslated);

        // Inicializar nossa classe de tradução
        translateHelper = new Translate(this);

        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToTranslate = editTextInput.getText().toString().trim();
                if (!textToTranslate.isEmpty()) {
                    translateHelper.translateText(textToTranslate, new Translate.TranslateCallback() {
                        @Override
                        public void onSuccess(String translatedText) {
                            textViewTranslated.setText(translatedText);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(MainActivity.this, "Erro na tradução: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Digite algo para traduzir", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (translateHelper != null) {
            translateHelper.close();
        }
    }
}