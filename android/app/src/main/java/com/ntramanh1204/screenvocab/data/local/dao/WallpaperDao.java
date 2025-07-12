package com.ntramanh1204.screenvocab.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
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
    @Query("SELECT * FROM wallpapers WHERE collectionId = :collectionId")
    Single<List<WallpaperEntity>> getWallpapersByCollection(String collectionId);
}
