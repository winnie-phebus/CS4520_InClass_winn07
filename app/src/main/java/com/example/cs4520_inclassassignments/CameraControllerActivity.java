package com.example.cs4520_inclassassignments;

import static com.example.cs4520_inclassassignments.R.id.previewView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.ExecutionException;

public class CameraControllerActivity extends AppCompatActivity implements View.OnClickListener {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private CameraSelector cameraSelector;
    private Preview preview;
    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider = null;
    private int lenseFacing;
    private int lenseFacingBack;
    private int lenseFacingFront;

    private DisplayTakenPhoto mListener;

    private FloatingActionButton buttonTakePhoto;
    private FloatingActionButton buttonSwitchCamera;
    private FloatingActionButton buttonOpenGallery;

    private static final int PERMISSIONS_CODE = 0x100;
    private FirebaseStorage storage;

    // TODO add to activity:
    // https://github.com/sakibnm/CameraXJava/blob/master/app/src/main/java/space/sakibnm/cameraxdemo/MainActivity.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //TODO: this might need to move to InClass08

        //  grant permissions to camera access, and read/write on device storage.
        Boolean cameraAllowed = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        Boolean readAllowed = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Boolean writeAllowed = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(cameraAllowed && readAllowed && writeAllowed){
            Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show();

        }else{
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSIONS_CODE);
        }

        // set up + display what camera is seeing.
        setUpCamera();

    }

    private void setUpCamera() {
        //            binding hardware camera with preview, and imageCapture.......
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(()->{ // using lambda to make this less complicated
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
                cameraProvider.bindToLifecycle(this ,cameraSelector, preview, imageCapture);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));

    }

    // TODO from here, send back to activity with the photo Uri
    private void takePhoto() {
        long timestamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/CameraX-Image");
        }

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions
                .Builder(
                this.getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
        )
                .build();


        imageCapture.takePicture(outputFileOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                        Log.d("demo", "onImageSaved: "+ outputFileResults.getSavedUri());
                        mListener.onTakePhoto(outputFileResults.getSavedUri());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        //Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ic09_camera_capture:
                takePhoto();
                break;
            case R.id.ic09_camera_gallery:
                mListener.onOpenGalleryPressed();
                break;
            case R.id.ic09_camera_flip:
                if(lenseFacing==lenseFacingBack){
                    lenseFacing = lenseFacingFront;
                    setUpCamera();
                }else{
                    lenseFacing = lenseFacingBack;
                    setUpCamera();
                }
                break;
        }
    }

    //Retrieving an image from gallery....
    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        // TODO send selecetedImageUri back to original activity.
                    }
                }
            }
    );

//    @Override
//    public void onAttach(@NonNull Context context) {
//        if(this instanceof CameraControllerFragment.DisplayTakenPhoto){
//            mListener = (CameraControllerFragment.DisplayTakenPhoto) context;
//        }else{
//            throw new RuntimeException(context+" must implement DisplayTakenPhoto");
//        }
//    }

    public interface DisplayTakenPhoto{
        void onTakePhoto(Uri imageUri);
        void onOpenGalleryPressed();
    }

    // TODO : override the above method.
    //@Override
    public void onOpenGalleryPressed() {
        openGallery();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        galleryLauncher.launch(intent);
    }

    //@Override
    public void onRetakePressed() {
        // restart CameraControllerActivity
    }

    //@Override
    public void onUploadButtonPressed(Uri imageUri, ProgressBar progressBar) {
//        ProgressBar.......
        progressBar.setVisibility(View.VISIBLE);
//        Upload an image from local file....
        StorageReference storageReference = storage.getReference().child("images/"+imageUri.getLastPathSegment());
        UploadTask uploadImage = storageReference.putFile(imageUri);
        uploadImage.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CameraControllerActivity.this, "Upload Failed! Try again!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(CameraControllerActivity.this, "Upload successful! Check Firestore", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        Log.d("demo", "onProgress: "+progress);
                        progressBar.setProgress((int) progress);
                    }
                });
    }


}