package com.example.firebaseml;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import java.util.List;

public class FaceDetectionActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Button buttonTakePhoto;
    private FaceDetectionHelper faceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);

        imageView = findViewById(R.id.imageView);
        buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        faceHelper = new FaceDetectionHelper();

        buttonTakePhoto.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            InputImage image = InputImage.fromBitmap(imageBitmap, 0);

            faceHelper.detectFaces(image, new FaceDetectionHelper.FaceDetectionCallback() {
                @Override
                public void onSuccess(List<Face> faces) {
                    Toast.makeText(FaceDetectionActivity.this, "Rostos detectados: " + faces.size(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(FaceDetectionActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
