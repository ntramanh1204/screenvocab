package com.ntramanh1204.screenvocab.data.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.model.Wallpaper;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class WallpaperMapper {

    private static final Gson gson = new Gson();

    // ---------------------- TO ENTITY ----------------------
    public static WallpaperEntity toEntity(Wallpaper wallpaper) {
        return new WallpaperEntity(
                wallpaper.getWallpaperId(),
                wallpaper.getCollectionId(),
                wallpaper.getTheme().name(), // store enum name as string
                wallpaper.getGridDimensions().getRows(),
                wallpaper.getGridDimensions().getCols(),
                gson.toJson(wallpaper.getTextHierarchy()), // convert list to JSON
                wallpaper.getCloudinaryUrl(),
                wallpaper.getThumbnailUrl(),
                wallpaper.getLocalFileUrl(),
                wallpaper.getCreatedAt(),
                wallpaper.getMetadata().getResolution(),
                wallpaper.getMetadata().getFormat(),
                wallpaper.getMetadata().getFileSize()
        );
    }

    // ---------------------- TO DOMAIN ----------------------
    public static Wallpaper toDomain(WallpaperEntity entity) {
        List<String> textHierarchy;
        try {
            Type type = new TypeToken<List<String>>() {}.getType();
            textHierarchy = gson.fromJson(entity.textHierarchy, type);
        } catch (Exception e) {
            textHierarchy = Collections.emptyList(); // fallback
        }

        return new Wallpaper(
                entity.wallpaperId,
                entity.collectionId,
                parseTheme(entity.theme),
                new Wallpaper.GridDimensions(entity.rows, entity.cols),
                textHierarchy,
                entity.cloudinaryUrl,
                entity.thumbnailUrl,
                entity.localFileUrl,
                entity.createdAt,
                new Wallpaper.WallpaperMetadata(
                        entity.resolution,
                        entity.format,
                        entity.fileSize
                )
        );
    }

    // Helper: Convert string back to enum safely
    private static Wallpaper.Theme parseTheme(String themeName) {
        try {
            return Wallpaper.Theme.valueOf(themeName);
        } catch (IllegalArgumentException | NullPointerException e) {
            return Wallpaper.Theme.CLEAN_WHITE; // fallback theme
        }
    }
}

