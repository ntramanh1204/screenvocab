package com.ntramanh1204.screenvocab.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface WallpaperDao {
    @Insert
    Completable insert(WallpaperEntity wallpaper);

    @Update
    Completable update(WallpaperEntity wallpaper);

    @Delete
    Completable delete(WallpaperEntity wallpaper);

    // Existing methods
    @Query("SELECT * FROM wallpapers WHERE collectionId = :collectionId ORDER BY createdAt DESC")
    Single<List<WallpaperEntity>> getWallpapersByCollection(String collectionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWallpaper(WallpaperEntity wallpaper);

    @Update
    Completable updateWallpaper(WallpaperEntity wallpaper);

    @Query("DELETE FROM wallpapers WHERE wallpaperId = :wallpaperId")
    Completable deleteWallpaper(String wallpaperId);

    @Query("SELECT * FROM wallpapers WHERE wallpaperId = :wallpaperId")
    Single<WallpaperEntity> getWallpaperById(String wallpaperId);

    // New methods for user-based queries
    @Query("SELECT w.* FROM wallpapers w " +
            "INNER JOIN collections c ON w.collectionId = c.collectionId " +
            "WHERE c.userId = :userId " +
            "ORDER BY w.createdAt DESC")
    Single<List<WallpaperEntity>> getWallpapersByUser(String userId);

    @Query("SELECT w.* FROM wallpapers w " +
            "INNER JOIN collections c ON w.collectionId = c.collectionId " +
            "WHERE c.userId = :userId " +
            "ORDER BY w.createdAt DESC " +
            "LIMIT :pageSize OFFSET :offset")
    Single<List<WallpaperEntity>> getWallpapersByUserPaginated(String userId, int pageSize, int offset);

    @Query("SELECT COUNT(*) FROM wallpapers w " +
            "INNER JOIN collections c ON w.collectionId = c.collectionId " +
            "WHERE c.userId = :userId")
    Single<Integer> getWallpaperCountByUser(String userId);

    // Optional: Combined query for user + specific collection
    @Query("SELECT w.* FROM wallpapers w " +
            "INNER JOIN collections c ON w.collectionId = c.collectionId " +
            "WHERE c.userId = :userId AND w.collectionId = :collectionId " +
            "ORDER BY w.createdAt DESC " +
            "LIMIT :pageSize OFFSET :offset")
    Single<List<WallpaperEntity>> getWallpapersByUserAndCollection(String userId, String collectionId, int pageSize, int offset);

    // Helper method to get recent wallpapers (for quick access)
    @Query("SELECT w.* FROM wallpapers w " +
            "INNER JOIN collections c ON w.collectionId = c.collectionId " +
            "WHERE c.userId = :userId " +
            "ORDER BY w.createdAt DESC " +
            "LIMIT :limit")
    Single<List<WallpaperEntity>> getRecentWallpapersByUser(String userId, int limit);
}
