package com.ntramanh1204.screenvocab.core.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.io.OutputStream;
import java.io.IOException;

public class SaveUtils {

    public static String saveImageToGallery(Context context, Bitmap bitmap) throws IOException {
        String filename = "wallpaper_" + System.currentTimeMillis() + ".png";
        String savedImagePath = null;

        ContentResolver resolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/ScreenVocabWallpapers");

        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        if (imageUri != null) {
            try (OutputStream outputStream = resolver.openOutputStream(imageUri)) {
                if (outputStream == null) throw new IOException("Failed to get output stream.");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                savedImagePath = imageUri.toString();  // Uri dạng string
            } catch (IOException e) {
                Log.e("SaveUtils", "Error saving image to gallery", e);
                throw e;
            }
        } else {
            throw new IOException("Failed to create new MediaStore record.");
        }

        return savedImagePath;
    }
}
