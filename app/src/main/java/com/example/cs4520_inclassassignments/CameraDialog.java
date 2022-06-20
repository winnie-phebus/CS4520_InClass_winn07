package com.example.cs4520_inclassassignments;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.ExecutionException;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 09
 */
public class CameraDialog extends Dialog implements View.OnClickListener {

    public static final String IMG_KEY = "ic09 IMG URL";
    public static final String TAG = "IC09_CCD";
    private static final int PERMISSIONS_CODE = 0x100;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private CameraSelector cameraSelector;
    private Preview preview;
    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider = null;
    private int lenseFacing;
    private int lenseFacingBack;
    private int lenseFacingFront;
    // private DisplayTakenPhoto mListener;
    private FloatingActionButton buttonTakePhoto;
    private FloatingActionButton buttonSwitchCamera;
    private FloatingActionButton buttonOpenGallery;
    // TODO add to activity:
    // https://github.com/sakibnm/CameraXJava/blob/master/app/src/main/java/space/sakibnm/cameraxdemo/MainActivity.java
    private FirebaseStorage storage;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Context context;

    public CameraDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CameraDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CameraDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setup() {
        setContentView(R.layout.activity_ic09_camera_controller);

        lenseFacingBack = CameraSelector.LENS_FACING_BACK;
        lenseFacingFront = CameraSelector.LENS_FACING_FRONT;

        previewView = findViewById(R.id.previewView);

        buttonTakePhoto = findViewById(R.id.ic09_camera_capture);
        buttonSwitchCamera = findViewById(R.id.ic09_camera_flip);
        buttonOpenGallery = findViewById(R.id.ic09_camera_gallery);

        buttonTakePhoto.setOnClickListener(this);
        buttonSwitchCamera.setOnClickListener(this);
        buttonOpenGallery.setOnClickListener(this);

        // default to back camera
        lenseFacing = lenseFacingBack;

        // get storage set up
        storage = FirebaseStorage.getInstance();

        //TODO: this might not be working;
        getContext().enforceCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE, "Permission needed.");

        //Retrieving an image from gallery....
      /*  galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            Uri selectedImageUri = data.getData();

                            returnImg(selectedImageUri);
                            // startActivity(backToSender);
                            // new Intent(CameraControllerActivity.class, MessageActivity.class);
                            // Picasso.get().load(selectedImageUri).into(previewView);
                            // TODO send selectedImageUri back to original activity.
                        }
                    }
                }
        );*/
        // set up + display what camera is seeing.
        setUpCamera();
    }

    private void setUpCamera() {
        //            binding hardware camera with preview, and imageCapture.......
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> { // using lambda to make this less complicated
            preview = new Preview.Builder()
                    .build();
            preview.setSurfaceProvider(previewView.getSurfaceProvider());
            imageCapture = new ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build();
            try {
                cameraProvider = cameraProviderFuture.get();
                cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle((LifecycleOwner) getOwnerActivity(), cameraSelector, preview, imageCapture);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getContext()));

    }

    // TODO from here, send back to activity with the photo Uri
    private void takePhoto() {
        long timestamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions
                        .Builder(
                        getContext().getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build();


        imageCapture.takePicture(outputFileOptions,
                ContextCompat.getMainExecutor(context),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.d("demo", "onImageSaved: " + outputFileResults.getSavedUri());
                        onTakePhoto(outputFileResults.getSavedUri());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        MainActivity.showToast(context, exception.getMessage());
                    }
                });
    }

    public void onTakePhoto(Uri imageUri) {
        // dk about this one
        returnImg(imageUri);
    }

    public void returnImg(Uri imgUri) {
        // onUploadButtonPressed(imgUri);
        Log.d(TAG, String.valueOf(imgUri));
        //final Intent data = new Intent();
        // Add the required data to be returned to the MainActivity
        // data.putExtra(IMG_KEY, imgUri);
        //data.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //setResult(RESULT_OK, data);
        //finish();
    }

    public void onOpenGalleryPressed() {
        openGallery();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        galleryLauncher.launch(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic09_camera_capture:
                takePhoto();
                break;
            case R.id.ic09_camera_gallery:
                onOpenGalleryPressed();
                break;
            case R.id.ic09_camera_flip:
                if (lenseFacing == lenseFacingBack) {
                    lenseFacing = lenseFacingFront;
                } else {
                    lenseFacing = lenseFacingBack;
                }
                setUpCamera();
                break;
        }
    }
}
