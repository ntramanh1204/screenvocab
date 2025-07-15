package com.ntramanh1204.screenvocab.domain.usecase.wallpaper;

import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.repository.WallpaperRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class GetWallpapersByCollectionUseCase {

    private final WallpaperRepository wallpaperRepository;

    public GetWallpapersByCollectionUseCase(WallpaperRepository wallpaperRepository) {
        this.wallpaperRepository = wallpaperRepository;
    }

    public Single<List<WallpaperEntity>> execute(String collectionId) {
        return wallpaperRepository.getWallpapersByCollection(collectionId);
    }
}
