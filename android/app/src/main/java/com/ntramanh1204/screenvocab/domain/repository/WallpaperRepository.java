package com.ntramanh1204.screenvocab.domain.repository;

import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.model.Wallpaper;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface WallpaperRepository {

    // Existing methods
    Single<List<Wallpaper>> getWallpapersByCollection(String collectionId);
    Completable insertWallpaper(WallpaperEntity wallpaper);
    Completable updateWallpaper(WallpaperEntity wallpaper);
    Completable deleteWallpaper(String wallpaperId);
    Completable deleteWallpaper(WallpaperEntity wallpaper);
    Single<Wallpaper> getWallpaperById(String wallpaperId);

    // New methods for user-based queries with pagination
    Single<List<Wallpaper>> getWallpapersByUser(String userId);
    Single<List<Wallpaper>> getWallpapersByUserPaginated(String userId, int page, int pageSize);
    Single<List<Wallpaper>> getWallpapersByUserWithOffset(String userId, int offset, int limit);
    Single<Integer> getWallpaperCountByUser(String userId);

    // Optional: Combined query (user + collection with pagination)
    Single<List<Wallpaper>> getWallpapersByUserAndCollection(String userId, String collectionId, int page, int pageSize);
}