package com.ntramanh1204.screenvocab.domain.usecase.wallpaper;

import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.repository.WallpaperRepository;

import io.reactivex.rxjava3.core.Completable;

public class DeleteWallpaperUseCase {

    private final WallpaperRepository wallpaperRepository;

    public DeleteWallpaperUseCase(WallpaperRepository wallpaperRepository) {
        this.wallpaperRepository = wallpaperRepository;
    }

    public Completable execute(WallpaperEntity wallpaper) {
        return wallpaperRepository.deleteWallpaper(wallpaper);
    }
}
