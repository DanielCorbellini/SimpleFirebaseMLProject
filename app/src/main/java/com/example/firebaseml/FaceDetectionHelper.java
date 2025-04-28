package com.example.firebaseml;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class FaceDetectionHelper {

    private FaceDetector detector;

    public FaceDetectionHelper() {
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        detector = FaceDetection.getClient(options);
    }

    public void detectFaces(InputImage image, FaceDetectionCallback callback) {
        detector.process(image)
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    public interface FaceDetectionCallback {
        void onSuccess(List<Face> faces);
        void onFailure(Exception e);
    }

    public void close() {
        detector.close();
    }
}
