package com.example.firebaseml;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.*;
import java.io.IOException;
import java.util.List;

public class FaceDetectionActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_CAMERA_CAPTURE = 2;

    private Button buttonSelectImage;
    private ImageView imageViewFace;
    private TextView textViewFaceInfo;
    private FaceDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);

        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        imageViewFace = findViewById(R.id.imageViewFace);
        textViewFaceInfo = findViewById(R.id.textViewFaceInfo);

        // Configurando o detector
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        detector = FaceDetection.getClient(options);

        // Ao clicar no botão, mostrar as opções de escolher entre tirar foto ou anexar
        buttonSelectImage.setOnClickListener(view -> showImageChoiceDialog());
    }

    // Exibe um diálogo para o usuário escolher entre câmera ou galeria
    private void showImageChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha a fonte da imagem")
                .setItems(new CharSequence[]{"Tirar foto", "Escolher da galeria"}, (dialog, which) -> {
                    if (which == 0) {
                        // Tirar foto
                        openCamera();
                    } else {
                        // Escolher da galeria
                        openGallery();
                    }
                })
                .show();
    }

    // Abre a câmera para capturar a foto
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA_CAPTURE);
        }
    }

    // Abre a galeria para escolher a imagem
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = null;

            if (requestCode == REQUEST_CAMERA_CAPTURE && data.getExtras() != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (bitmap != null) {
                imageViewFace.setImageBitmap(bitmap);
                InputImage image = InputImage.fromBitmap(bitmap, 0);

                detector.process(image)
                        .addOnSuccessListener(faces -> showFaceInfo(faces))
                        .addOnFailureListener(e -> textViewFaceInfo.setText("Erro ao detectar rostos: " + e.getMessage()));
            }
        }
    }

    private void showFaceInfo(List<Face> faces) {
        if (faces.isEmpty()) {
            textViewFaceInfo.setText("Nenhum rosto detectado.");
            return;
        }

        StringBuilder result = new StringBuilder();
        for (Face face : faces) {
            result.append("Rosto detectado:\n");

            if (face.getSmilingProbability() != null) {
                float smileProb = face.getSmilingProbability();
                result.append("Sorrindo: ").append(smileProb > 0.5 ? "Sim" : "Não").append("\n");
            }

            if (face.getLeftEyeOpenProbability() != null) {
                float leftEyeOpenProb = face.getLeftEyeOpenProbability();
                result.append("Olho esquerdo aberto: ").append(leftEyeOpenProb > 0.5 ? "Sim" : "Não").append("\n");
            }

            if (face.getRightEyeOpenProbability() != null) {
                float rightEyeOpenProb = face.getRightEyeOpenProbability();
                result.append("Olho direito aberto: ").append(rightEyeOpenProb > 0.5 ? "Sim" : "Não").append("\n");
            }

            result.append("\n");
        }

        textViewFaceInfo.setText(result.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (detector != null) {
            detector.close();
        }
    }
}
