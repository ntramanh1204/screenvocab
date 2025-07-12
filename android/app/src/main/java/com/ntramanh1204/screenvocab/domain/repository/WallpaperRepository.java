package com.ntramanh1204.screenvocab.domain.repository;

import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface WallpaperRepository {
    Completable insertWallpaper(WallpaperEntity wallpaper);
    Completable updateWallpaper(WallpaperEntity wallpaper);
    Completable deleteWallpaper(WallpaperEntity wallpaper);
    Single<List<WallpaperEntity>> getWallpapersByCollection(String collectionId);
}
