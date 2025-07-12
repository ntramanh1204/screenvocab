package com.ntramanh1204.screenvocab.data.remote;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FirestoreDataSource {
    private final FirebaseFirestore db;

    public FirestoreDataSource() {
        this.db = FirebaseFirestore.getInstance();
    }

    // User
    public Completable saveUser(String userId, Object user) {
        return Completable.create(emitter -> {
            db.collection("users").document(userId).set(user)
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }
    public Single<DocumentSnapshot> getUser(String userId) {
        return Single.create(emitter -> {
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (!emitter.isDisposed()) emitter.onSuccess(documentSnapshot);
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    // Collection
    public Completable saveCollection(String collectionId, Object collection) {
        return Completable.create(emitter -> {
            db.collection("collections").document(collectionId).set(collection)
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }
    public Single<DocumentSnapshot> getCollection(String collectionId) {
        return Single.create(emitter -> {
            db.collection("collections").document(collectionId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (!emitter.isDisposed()) emitter.onSuccess(documentSnapshot);
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }
    public Single<QuerySnapshot> getCollectionsByUser(String userId) {
        return Single.create(emitter -> {
            db.collection("collections").whereEqualTo("userId", userId).get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!emitter.isDisposed()) emitter.onSuccess(querySnapshot);
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    // Word
    public Completable saveWord(String wordId, Object word) {
        return Completable.create(emitter -> {
            db.collection("words").document(wordId).set(word)
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }
    public Single<DocumentSnapshot> getWord(String wordId) {
        return Single.create(emitter -> {
            db.collection("words").document(wordId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (!emitter.isDisposed()) emitter.onSuccess(documentSnapshot);
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }
    public Single<QuerySnapshot> getWordsByCollection(String collectionId) {
        return Single.create(emitter -> {
            db.collection("words").whereEqualTo("collectionId", collectionId).get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!emitter.isDisposed()) emitter.onSuccess(querySnapshot);
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    // Wallpaper
    // Wallpaper
    public Completable saveWallpaper(WallpaperEntity wallpaper) {
        return Completable.create(emitter -> {
            Map<String, Object> data = new HashMap<>();
            data.put("wallpaperId", wallpaper.getWallpaperId());
            data.put("collectionId", wallpaper.getCollectionId());
            data.put("theme", wallpaper.getTheme());
            data.put("rows", wallpaper.getRows());
            data.put("cols", wallpaper.getCols());
            data.put("textHierarchy", wallpaper.getTextHierarchy());
            data.put("cloudinaryUrl", wallpaper.getCloudinaryUrl());
            data.put("thumbnailUrl", wallpaper.getThumbnailUrl());
            data.put("localFileUrl", wallpaper.getLocalFileUrl());
            data.put("createdAt", wallpaper.getCreatedAt());
            data.put("resolution", wallpaper.getResolution());
            data.put("format", wallpaper.getFormat());
            data.put("fileSize", wallpaper.getFileSize());

            db.collection("wallpapers")
                    .document(wallpaper.getWallpaperId())
                    .set(data)
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    public Single<DocumentSnapshot> getWallpaper(String wallpaperId) {
        return Single.create(emitter -> {
            db.collection("wallpapers").document(wallpaperId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (!emitter.isDisposed()) emitter.onSuccess(documentSnapshot);
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    public Single<QuerySnapshot> getWallpapersByCollection(String collectionId) {
        return Single.create(emitter -> {
            db.collection("wallpapers").whereEqualTo("collectionId", collectionId).get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!emitter.isDisposed()) emitter.onSuccess(querySnapshot);
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    public Completable deleteWallpaper(String wallpaperId) {
        return Completable.create(emitter -> {
            db.collection("wallpapers").document(wallpaperId).delete()
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }
}