package com.example.firebaseml;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.mlkit.nl.translate.TranslateLanguage;
import java.util.HashMap;
import java.util.Map;

public class TranslateActivity extends AppCompatActivity {

    private EditText editTextInput;
    private Button buttonTranslate;
    private TextView textViewTranslated;
    private Spinner spinnerSourceLang, spinnerTargetLang;
    private Translate translateHelper;
    private String sourceLangCode = TranslateLanguage.PORTUGUESE;
    private String targetLangCode = TranslateLanguage.ENGLISH;

    private Map<String, String> languagesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        editTextInput = findViewById(R.id.editTextInput);
        buttonTranslate = findViewById(R.id.buttonTranslate);
        textViewTranslated = findViewById(R.id.textViewTranslated);
        spinnerSourceLang = findViewById(R.id.spinnerSourceLanguage);
        spinnerTargetLang = findViewById(R.id.spinnerTargetLanguage);

        setupLanguages();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languagesMap.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSourceLang.setAdapter(adapter);
        spinnerTargetLang.setAdapter(adapter);

        spinnerSourceLang.setSelection(adapter.getPosition("Português")); // seleção padrão
        spinnerTargetLang.setSelection(adapter.getPosition("Inglês"));    // seleção padrão

        spinnerSourceLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = parent.getItemAtPosition(position).toString();
                sourceLangCode = languagesMap.get(selectedLang);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerTargetLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = parent.getItemAtPosition(position).toString();
                targetLangCode = languagesMap.get(selectedLang);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        buttonTranslate.setOnClickListener(view -> {
            String textToTranslate = editTextInput.getText().toString().trim();
            if (!textToTranslate.isEmpty()) {
                translateHelper = new Translate(this, sourceLangCode, targetLangCode);
                translateHelper.translateText(textToTranslate, new Translate.TranslateCallback() {
                    @Override
                    public void onSuccess(String translatedText) {
                        textViewTranslated.setText(translatedText);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(TranslateActivity.this, "Erro na tradução: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Digite algo para traduzir", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupLanguages() {
        languagesMap = new HashMap<>();
        languagesMap.put("Inglês", TranslateLanguage.ENGLISH);
        languagesMap.put("Português", TranslateLanguage.PORTUGUESE);
        languagesMap.put("Espanhol", TranslateLanguage.SPANISH);
        languagesMap.put("Francês", TranslateLanguage.FRENCH);
        languagesMap.put("Alemão", TranslateLanguage.GERMAN);
        languagesMap.put("Italiano", TranslateLanguage.ITALIAN);
        languagesMap.put("Japonês", TranslateLanguage.JAPANESE);
        languagesMap.put("Coreano", TranslateLanguage.KOREAN);
        languagesMap.put("Chinês", TranslateLanguage.CHINESE);
        // Você pode adicionar mais idiomas aqui...
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (translateHelper != null) {
            translateHelper.close();
        }
    }
}