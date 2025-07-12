package com.ntramanh1204.screenvocab.data.repository;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ntramanh1204.screenvocab.data.local.dao.WallpaperDao;
import com.ntramanh1204.screenvocab.data.remote.FirestoreDataSource;
import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.repository.WallpaperRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallpaperRepositoryImpl implements WallpaperRepository {
    private final WallpaperDao wallpaperDao;
    private final FirestoreDataSource firestoreDataSource;

    public WallpaperRepositoryImpl(WallpaperDao wallpaperDao) {
        this.wallpaperDao = wallpaperDao;
        this.firestoreDataSource = new FirestoreDataSource();
    }

    @Override
    public Single<List<WallpaperEntity>> getWallpapersByCollection(String collectionId) {
        return wallpaperDao.getWallpapersByCollection(collectionId)
                .onErrorResumeNext(throwable ->
                        firestoreDataSource.getWallpapersByCollection(collectionId)
                                .map(querySnapshot -> {
                                    List<WallpaperEntity> wallpapers = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc : querySnapshot) {
                                        WallpaperEntity wallpaper = new WallpaperEntity();
                                        wallpaper.setWallpaperId(doc.getId());
                                        wallpaper.setCollectionId(doc.getString("collectionId"));
                                        wallpaper.setTheme(doc.getString("theme"));
                                        wallpaper.setRows(doc.getLong("rows").intValue());
                                        wallpaper.setCols(doc.getLong("cols").intValue());
                                        wallpaper.setTextHierarchy(doc.getString("textHierarchy"));
                                        wallpaper.setCloudinaryUrl(doc.getString("cloudinaryUrl"));
                                        wallpaper.setThumbnailUrl(doc.getString("thumbnailUrl"));
                                        wallpaper.setLocalFileUrl(doc.getString("localFileUrl"));
                                        wallpaper.setCreatedAt(doc.getLong("createdAt"));
                                        wallpaper.setResolution(doc.getString("resolution"));
                                        wallpaper.setFormat(doc.getString("format"));
                                        wallpaper.setFileSize(doc.getLong("fileSize"));
                                        wallpapers.add(wallpaper);
                                    }
                                    return wallpapers;
                                })
                );
    }

    @Override
    public Completable insertWallpaper(WallpaperEntity wallpaper) {
        return wallpaperDao.insert(wallpaper)
                .andThen(syncToFirestore(wallpaper));
    }

    @Override
    public Completable updateWallpaper(WallpaperEntity wallpaper) {
        return wallpaperDao.update(wallpaper)
                .andThen(syncToFirestore(wallpaper));
    }

    @Override
    public Completable deleteWallpaper(WallpaperEntity wallpaper) {
        return wallpaperDao.delete(wallpaper)
                .andThen(Completable.create(emitter -> {
                    if (isNetworkAvailable()) {
                        firestoreDataSource.deleteWallpaper(wallpaper.getWallpaperId())
                                .subscribe(emitter::onComplete, emitter::onError);
                    } else {
                        emitter.onComplete(); // Queue for later sync
                    }
                }));
    }

    private Completable syncToFirestore(WallpaperEntity wallpaper) {
        return Completable.create(emitter -> {
            if (isNetworkAvailable()) {
                firestoreDataSource.saveWallpaper(wallpaper)
                        .subscribe(emitter::onComplete, emitter::onError);
            } else {
                emitter.onComplete(); // Queue for later sync
            }
        });
    }

    private boolean isNetworkAvailable() {
        // TODO: Implement network check using ConnectivityManager
        return true; // Giả lập cho đơn giản
    }
}