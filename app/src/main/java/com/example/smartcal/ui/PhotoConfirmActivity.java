package com.example.smartcal.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.smartcal.R;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles camera preview, capture, and confirmation using CameraX.
 * Includes necessary runtime permission checks to avoid capture failures.
 */
public class PhotoConfirmActivity extends AppCompatActivity {

    private static final String TAG = "PhotoConfirmActivity";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    // Determine required permissions based on Android version
    private final String[] REQUIRED_PERMISSIONS = getRequiredPermissions();

    private PreviewView previewView;
    private ImageView ivPreview;
    private Button btnCapture, btnConfirm, btnRetake;
    private ImageCapture imageCapture;
    private File photoFile;
    private ExecutorService cameraExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_confirm);

        previewView = findViewById(R.id.previewView);
        ivPreview = findViewById(R.id.ivPreview);
        btnCapture = findViewById(R.id.btnCapture);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnRetake = findViewById(R.id.btnRetake);

        // Initialize executor
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Ensure permissions are handled at runtime
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        btnCapture.setOnClickListener(v -> takePhoto());

        btnRetake.setOnClickListener(v -> {
            // Reset UI state for retake
            ivPreview.setImageURI(null);
            ivPreview.setVisibility(View.GONE);
            previewView.setVisibility(View.VISIBLE);

            // Re-bind camera to ensure a clean state
            startCamera();

            // Show only the capture button for retake
            updateButtonVisibility(false);
        });

        btnConfirm.setOnClickListener(v -> {
            if (photoFile != null && photoFile.exists()) {
                Intent intent = new Intent();
                // Return the absolute path of the saved photo file
                intent.putExtra("photo_path", photoFile.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "No photo captured!", Toast.LENGTH_SHORT).show();
            }
        });

        // Initial button state: Show capture, hide confirm/retake
        updateButtonVisibility(false);
    }

    /**
     * Toggles the visibility of the control buttons based on capture state.
     * @param photoCaptured true to show Retake/Confirm, false to show Capture.
     */
    private void updateButtonVisibility(boolean photoCaptured) {
        if (photoCaptured) {
            btnCapture.setVisibility(View.GONE);
            btnRetake.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(View.VISIBLE);
        } else {
            btnCapture.setVisibility(View.VISIBLE);
            btnRetake.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
        }
    }

    /**
     * Determines the required permissions based on the Android version.
     */
    private String[] getRequiredPermissions() {
        // Use MediaStore for Android Q+ (API 29+), which only requires CAMERA permission.
        // For simplicity and compatibility with older systems when saving to external files dir,
        // we'll keep the storage permission check only for older versions where it's strictly required
        // when using getExternalFilesDir(null).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new String[]{Manifest.permission.CAMERA};
        } else {
            // Older devices need WRITE_EXTERNAL_STORAGE permission
            return new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

    /**
     * Checks if all required permissions are granted.
     */
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets up CameraX to display the camera feed in the PreviewView.
     */
    private void startCamera() {
        ivPreview.setVisibility(View.GONE);
        previewView.setVisibility(View.VISIBLE);
        updateButtonVisibility(false);

        ProcessCameraProvider.getInstance(this).addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = ProcessCameraProvider.getInstance(this).get();

                // 1. Create Preview Use Case
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // 2. Create ImageCapture Use Case
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .build();

                // 3. Select Camera
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // 4. Bind Use Cases
                cameraProvider.unbindAll(); // Crucial: Unbind existing use cases for a clean restart
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (Exception e) {
                Log.e(TAG, "Use case binding failed", e);
                Toast.makeText(this, "Failed to start camera: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
     * Captures a photo and saves it to external app storage.
     */
    private void takePhoto() {
        if (imageCapture == null) {
            Toast.makeText(this, "Camera not initialized. Check permissions.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the file in the app's private external storage
        photoFile = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");

        // Use the File as output option
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, cameraExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    // Hide preview, show captured image
                    previewView.setVisibility(View.GONE);
                    ivPreview.setVisibility(View.VISIBLE);
                    ivPreview.setImageURI(Uri.fromFile(photoFile));
                    updateButtonVisibility(true); // Show Confirm/Retake
                    Toast.makeText(PhotoConfirmActivity.this, "Photo captured!", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e(TAG, "Photo capture failed: " + exception.getMessage(), exception);
                runOnUiThread(() -> Toast.makeText(PhotoConfirmActivity.this, "Capture failed: " + exception.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    // --- Permission Handling Callback ---

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                // Permissions granted, start the camera
                startCamera();
            } else {
                // Permissions denied, show a toast and close the activity
                Toast.makeText(this, "Permissions not granted. Camera features disabled.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Crucial: shut down the executor
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
        // Unbind camera (optional, but good practice)
        ProcessCameraProvider.getInstance(this).addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = ProcessCameraProvider.getInstance(this).get();
                cameraProvider.unbindAll();
            } catch (Exception e) {
                // Ignore failure during shutdown
            }
        }, ContextCompat.getMainExecutor(this));
    }
}
