package com.example.firebaseml;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.nl.translate.Translation;

public class Translate {

    private Translator translator;
    private Context context;

    // Construtor: receber o Context (pra poder usar Toast)
    public Translate(Context context, String sourceLang, String targetLang) {
        this.context = context;
        setupTranslator(sourceLang, targetLang);
    }

    public Translate(TranslateActivity context) {
    }

    private void setupTranslator(String sourceLang, String targetLang) {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLang)
                .setTargetLanguage(targetLang)
                .build();

        translator = Translation.getClient(options);

        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Modelo de tradução pronto!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Falha ao baixar modelo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Translate", "Erro ao baixar modelo: " + e.getMessage());
                });
    }

    // Nova função que realmente traduz texto
    public void translateText(@NonNull String text, @NonNull TranslateCallback callback) {
        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused -> {
                    // Só depois que o modelo estiver pronto, a gente traduz
                    translator.translate(text)
                            .addOnSuccessListener(callback::onSuccess)
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }


    // Interface para callback
    public interface TranslateCallback {
        void onSuccess(String translatedText);
        void onFailure(Exception e);
    }

    // Para fechar o tradutor (liberar memória)
    public void close() {
        if (translator != null) {
            translator.close();
        }
    }
}
