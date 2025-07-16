package com.ntramanh1204.screenvocab.data.repository;

import android.content.Context;

import com.ntramanh1204.screenvocab.data.local.dao.WallpaperDao;
import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.data.mapper.WallpaperMapper;
import com.ntramanh1204.screenvocab.domain.model.Wallpaper;
import com.ntramanh1204.screenvocab.domain.repository.WallpaperRepository;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class WallpaperRepositoryImpl implements WallpaperRepository {

    private final WallpaperDao wallpaperDao;
    private final WallpaperMapper wallpaperMapper;
    private final Context context;

    public WallpaperRepositoryImpl(WallpaperDao wallpaperDao, WallpaperMapper wallpaperMapper, Context context) {
        this.wallpaperDao = wallpaperDao;
        this.wallpaperMapper = wallpaperMapper;
        this.context = context;
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByCollection(String collectionId) {
        return wallpaperDao.getWallpapersByCollection(collectionId)
                .map(entities -> entities.stream()
                        .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByUser(String userId) {
        return wallpaperDao.getWallpapersByUser(userId)
                .map(entities -> entities.stream()
                        .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByUserPaginated(String userId, int page, int pageSize) {
        int offset = page * pageSize;
        return wallpaperDao.getWallpapersByUserPaginated(userId, pageSize, offset)
                .map(entities -> entities.stream()
                        .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByUserWithOffset(String userId, int offset, int limit) {
        return wallpaperDao.getWallpapersByUserPaginated(userId, limit, offset)
                .map(entities -> entities.stream()
                        .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<Integer> getWallpaperCountByUser(String userId) {
        return wallpaperDao.getWallpaperCountByUser(userId);
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByUserAndCollection(String userId, String collectionId, int page, int pageSize) {
        int offset = page * pageSize;
        return wallpaperDao.getWallpapersByUserAndCollection(userId, collectionId, pageSize, offset)
                .map(entities -> entities.stream()
                        .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                        .collect(Collectors.toList()));
    }

    @Override
    public Completable insertWallpaper(WallpaperEntity wallpaper) {
        return wallpaperDao.insertWallpaper(wallpaper);
    }

    @Override
    public Completable updateWallpaper(WallpaperEntity wallpaper) {
        return wallpaperDao.updateWallpaper(wallpaper);
    }

    @Override
    public Completable deleteWallpaper(String wallpaperId) {
        return wallpaperDao.deleteWallpaper(wallpaperId);
    }

    @Override
    public Completable deleteWallpaper(WallpaperEntity wallpaper) {
        return wallpaperDao.delete(wallpaper);
    }

    @Override
    public Single<Wallpaper> getWallpaperById(String wallpaperId) {
        return wallpaperDao.getWallpaperById(wallpaperId)
                .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)));
    }

    private Wallpaper enhanceWithLocalPath(Wallpaper wallpaper) {
        String internalPath = getInternalStoragePath(wallpaper.getWallpaperId());
        if (internalPath != null && new File(internalPath).exists()) {
            return wallpaper.toBuilder()
                    .localFileUrl(internalPath)
                    .build();
        }

        String fallbackUrl = wallpaper.getThumbnailUrl() != null ?
                wallpaper.getThumbnailUrl() : wallpaper.getCloudinaryUrl();

        return wallpaper.toBuilder()
                .localFileUrl(fallbackUrl)
                .build();
    }

    private String getInternalStoragePath(String wallpaperId) {
        File wallpapersDir = new File(context.getFilesDir(), "wallpapers");
        if (!wallpapersDir.exists()) {
            return null;
        }

        String[] extensions = {".png", ".jpg", ".jpeg"};
        for (String ext : extensions) {
            File file = new File(wallpapersDir, wallpaperId + ext);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    public Single<List<Wallpaper>> getAllWallpapers() {
        return wallpaperDao.getWallpapersByUser("")
                .map(entities -> entities.stream()
                        .map(entity -> wallpaperMapper.toDomain(entity))
                        .collect(Collectors.toList()));
    }

    public Single<List<Wallpaper>> getWallpapersForGuest(String collectionId) {
        return getWallpapersByCollection(collectionId);
    }
}
