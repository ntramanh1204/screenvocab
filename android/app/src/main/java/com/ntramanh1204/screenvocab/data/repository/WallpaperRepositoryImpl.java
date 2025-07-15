package com.ntramanh1204.screenvocab.data.repository;

import com.ntramanh1204.screenvocab.data.local.dao.WallpaperDao;
import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.repository.WallpaperRepository;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public class WallpaperRepositoryImpl implements WallpaperRepository {
    private final WallpaperDao wallpaperDao;

    public WallpaperRepositoryImpl(WallpaperDao wallpaperDao) {
        this.wallpaperDao = wallpaperDao;
    }

    @Override
    public Single<List<WallpaperEntity>> getWallpapersByCollection(String collectionId) {
        return wallpaperDao.getWallpapersByCollection(collectionId);
    }

    @Override
    public Completable insertWallpaper(WallpaperEntity wallpaper) {
        return wallpaperDao.insert(wallpaper);
    }

    @Override
    public Completable updateWallpaper(WallpaperEntity wallpaper) {
        return wallpaperDao.update(wallpaper);
    }

    @Override
    public Completable deleteWallpaper(WallpaperEntity wallpaper) {
        return wallpaperDao.delete(wallpaper);
    }
}
