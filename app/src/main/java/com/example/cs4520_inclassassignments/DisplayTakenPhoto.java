package com.example.cs4520_inclassassignments;

import android.net.Uri;

public interface DisplayTakenPhoto {
    void onTakePhoto(Uri imageUri);

    void onOpenGalleryPressed();
}
