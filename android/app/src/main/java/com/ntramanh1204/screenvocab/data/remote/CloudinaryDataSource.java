package com.ntramanh1204.screenvocab.data.remote;

import android.net.Uri;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;

public class CloudinaryDataSource {
    public CloudinaryDataSource() {
        // Initialize MediaManager if needed
        // MediaManager.init() should be called in Application class or elsewhere
    }

    public Completable uploadImage(Uri fileUri, Map<String, Object> options) {
        return Completable.create(emitter -> {
            MediaManager.get().upload(fileUri)
                    .option("resource_type", "image")
                    .options(options)
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) { }
                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) { }
                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            if (!emitter.isDisposed()) emitter.onComplete();
                        }
                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            if (!emitter.isDisposed()) emitter.onError(new Exception(error.getDescription()));
                        }
                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            if (!emitter.isDisposed()) emitter.onError(new Exception(error.getDescription()));
                        }
                    })
                    .dispatch();
        });
    }
}