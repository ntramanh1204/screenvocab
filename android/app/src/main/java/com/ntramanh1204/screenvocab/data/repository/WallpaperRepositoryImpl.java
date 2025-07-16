package com.ntramanh1204.screenvocab.data.repository;

import android.content.Context;
import android.util.Log;

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

        // Debug database structure khi khởi tạo
        debugDatabaseStructure();
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByCollection(String collectionId) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG getWallpapersByCollection ===");
        Log.d("WallpaperRepositoryImpl", "Looking for collectionId: " + collectionId);

        return wallpaperDao.getWallpapersByCollection(collectionId)
                .doOnSuccess(entities -> {
                    Log.d("WallpaperRepositoryImpl", "DAO returned " + entities.size() + " entities");
                    for (WallpaperEntity entity : entities) {
                        Log.d("WallpaperRepositoryImpl", "Entity - ID: " + entity.getWallpaperId() +
                                ", Collection: " + entity.getCollectionId() +
                                ", User: " + entity.getUserId());
                    }
                })
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error from DAO", error))
                .map(entities -> {
                    List<Wallpaper> wallpapers = entities.stream()
                            .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                            .collect(Collectors.toList());
                    Log.d("WallpaperRepositoryImpl", "Final wallpapers list size: " + wallpapers.size());
                    Log.d("WallpaperRepositoryImpl", "=====================================");
                    return wallpapers;
                });
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByUser(String userId) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG getWallpapersByUser ===");
        Log.d("WallpaperRepositoryImpl", "Looking for userId: " + userId);

        return wallpaperDao.getWallpapersByUser(userId)
                .doOnSuccess(entities -> {
                    Log.d("WallpaperRepositoryImpl", "DAO returned " + entities.size() + " entities for user: " + userId);
                    for (WallpaperEntity entity : entities) {
                        Log.d("WallpaperRepositoryImpl", "Entity - ID: " + entity.getWallpaperId() +
                                ", Collection: " + entity.getCollectionId() +
                                ", User: " + entity.getUserId());
                    }
                })
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error getting wallpapers by user", error))
                .map(entities -> {
                    List<Wallpaper> wallpapers = entities.stream()
                            .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                            .collect(Collectors.toList());
                    Log.d("WallpaperRepositoryImpl", "Final wallpapers list size: " + wallpapers.size());
                    Log.d("WallpaperRepositoryImpl", "=====================================");
                    return wallpapers;
                });
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByUserPaginated(String userId, int page, int pageSize) {
        int offset = page * pageSize;
        Log.d("WallpaperRepositoryImpl", "=== DEBUG getWallpapersByUserPaginated ===");
        Log.d("WallpaperRepositoryImpl", "UserId: " + userId + ", Page: " + page + ", PageSize: " + pageSize + ", Offset: " + offset);

        return wallpaperDao.getWallpapersByUserPaginated(userId, pageSize, offset)
                .doOnSuccess(entities -> {
                    Log.d("WallpaperRepositoryImpl", "DAO returned " + entities.size() + " entities");
                })
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error in paginated query", error))
                .map(entities -> entities.stream()
                        .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByUserWithOffset(String userId, int offset, int limit) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG getWallpapersByUserWithOffset ===");
        Log.d("WallpaperRepositoryImpl", "UserId: " + userId + ", Offset: " + offset + ", Limit: " + limit);

        return wallpaperDao.getWallpapersByUserPaginated(userId, limit, offset)
                .doOnSuccess(entities -> {
                    Log.d("WallpaperRepositoryImpl", "DAO returned " + entities.size() + " entities");
                })
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error in offset query", error))
                .map(entities -> entities.stream()
                        .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<Integer> getWallpaperCountByUser(String userId) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG getWallpaperCountByUser ===");
        Log.d("WallpaperRepositoryImpl", "UserId: " + userId);

        return wallpaperDao.getWallpaperCountByUser(userId)
                .doOnSuccess(count -> {
                    Log.d("WallpaperRepositoryImpl", "Count result: " + count);
                })
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error getting count", error));
    }

    @Override
    public Single<List<Wallpaper>> getWallpapersByUserAndCollection(String userId, String collectionId, int page, int pageSize) {
        int offset = page * pageSize;
        Log.d("WallpaperRepositoryImpl", "=== DEBUG getWallpapersByUserAndCollection ===");
        Log.d("WallpaperRepositoryImpl", "UserId: " + userId + ", CollectionId: " + collectionId +
                ", Page: " + page + ", PageSize: " + pageSize + ", Offset: " + offset);

        return wallpaperDao.getWallpapersByUserAndCollection(userId, collectionId, pageSize, offset)
                .doOnSuccess(entities -> {
                    Log.d("WallpaperRepositoryImpl", "DAO returned " + entities.size() + " entities");
                })
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error in user+collection query", error))
                .map(entities -> entities.stream()
                        .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)))
                        .collect(Collectors.toList()));
    }

    @Override
    public Completable insertWallpaper(WallpaperEntity wallpaper) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG insertWallpaper ===");
        Log.d("WallpaperRepositoryImpl", "Inserting wallpaper: " + wallpaper.getWallpaperId() +
                ", Collection: " + wallpaper.getCollectionId() +
                ", User: " + wallpaper.getUserId());

        return wallpaperDao.insertWallpaper(wallpaper)
                .doOnComplete(() -> Log.d("WallpaperRepositoryImpl", "Wallpaper inserted successfully"))
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error inserting wallpaper", error));
    }

    @Override
    public Completable updateWallpaper(WallpaperEntity wallpaper) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG updateWallpaper ===");
        Log.d("WallpaperRepositoryImpl", "Updating wallpaper: " + wallpaper.getWallpaperId());

        return wallpaperDao.updateWallpaper(wallpaper)
                .doOnComplete(() -> Log.d("WallpaperRepositoryImpl", "Wallpaper updated successfully"))
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error updating wallpaper", error));
    }

    @Override
    public Completable deleteWallpaper(String wallpaperId) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG deleteWallpaper ===");
        Log.d("WallpaperRepositoryImpl", "Deleting wallpaper: " + wallpaperId);

        return wallpaperDao.deleteWallpaper(wallpaperId)
                .doOnComplete(() -> Log.d("WallpaperRepositoryImpl", "Wallpaper deleted successfully"))
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error deleting wallpaper", error));
    }

    @Override
    public Completable deleteWallpaper(WallpaperEntity wallpaper) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG deleteWallpaper (entity) ===");
        Log.d("WallpaperRepositoryImpl", "Deleting wallpaper entity: " + wallpaper.getWallpaperId());

        return wallpaperDao.delete(wallpaper)
                .doOnComplete(() -> Log.d("WallpaperRepositoryImpl", "Wallpaper entity deleted successfully"))
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error deleting wallpaper entity", error));
    }

    @Override
    public Single<Wallpaper> getWallpaperById(String wallpaperId) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG getWallpaperById ===");
        Log.d("WallpaperRepositoryImpl", "Looking for wallpaper ID: " + wallpaperId);

        return wallpaperDao.getWallpaperById(wallpaperId)
                .doOnSuccess(entity -> {
                    Log.d("WallpaperRepositoryImpl", "Found wallpaper: " + entity.getWallpaperId() +
                            ", Collection: " + entity.getCollectionId());
                })
                .doOnError(error -> Log.e("WallpaperRepositoryImpl", "Error getting wallpaper by ID", error))
                .map(entity -> enhanceWithLocalPath(wallpaperMapper.toDomain(entity)));
    }

    /**
     * Enhance wallpaper with local file path from internal storage
     * Priority: localFileUrl > internal storage > thumbnailUrl > cloudinaryUrl
     */
    private Wallpaper enhanceWithLocalPath(Wallpaper wallpaper) {
        // DEBUG: Thêm log
        Log.d("WallpaperRepositoryImpl", "=== DEBUG enhanceWithLocalPath ===");
        Log.d("WallpaperRepositoryImpl", "Wallpaper ID: " + wallpaper.getWallpaperId());
        Log.d("WallpaperRepositoryImpl", "Original localFileUrl: " + wallpaper.getLocalFileUrl());

        // Try to find file in internal storage
        String internalPath = getInternalStoragePath(wallpaper.getWallpaperId());
        Log.d("WallpaperRepositoryImpl", "Internal path found: " + internalPath);

        if (internalPath != null && new File(internalPath).exists()) {
            Log.d("WallpaperRepositoryImpl", "File exists, using internal path");
            return wallpaper.toBuilder()
                    .localFileUrl(internalPath)
                    .build();
        }

        Log.d("WallpaperRepositoryImpl", "File not found, using fallback");

        // Fallback to thumbnailUrl or cloudinaryUrl
        String fallbackUrl = wallpaper.getThumbnailUrl() != null ?
                wallpaper.getThumbnailUrl() : wallpaper.getCloudinaryUrl();

        return wallpaper.toBuilder()
                .localFileUrl(fallbackUrl)
                .build();
    }

    /**
     * Get internal storage path for wallpaper
     * Expected path: /data/data/com.ntramanh1204.screenvocab/files/wallpapers/{wallpaperId}.png
     */
    private String getInternalStoragePath(String wallpaperId) {
        File wallpapersDir = new File(context.getFilesDir(), "wallpapers");
        if (!wallpapersDir.exists()) {
            return null;
        }

        // Try different file extensions
        String[] extensions = {".png", ".jpg", ".jpeg"};
        for (String ext : extensions) {
            File file = new File(wallpapersDir, wallpaperId + ext);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    /**
     * Debug method to check database structure and data
     */
    private void debugDatabaseStructure() {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG DATABASE STRUCTURE ===");

        try {
            // Kiểm tra tổng số wallpapers bằng cách get tất cả users
            wallpaperDao.getWallpapersByUser("") // Empty string might get all or none
                    .subscribe(
                            entities -> {
                                Log.d("WallpaperRepositoryImpl", "Total wallpapers found: " + entities.size());

                                // Log available collections
                                entities.stream()
                                        .map(WallpaperEntity::getCollectionId)
                                        .distinct()
                                        .forEach(collectionId ->
                                                Log.d("WallpaperRepositoryImpl", "Available collection: " + collectionId)
                                        );

                                // Log available users
                                entities.stream()
                                        .map(WallpaperEntity::getUserId)
                                        .distinct()
                                        .forEach(userId ->
                                                Log.d("WallpaperRepositoryImpl", "Available user: " + userId)
                                        );
                            },
                            error -> Log.e("WallpaperRepositoryImpl", "Error getting all wallpapers", error)
                    );

        } catch (Exception e) {
            Log.e("WallpaperRepositoryImpl", "Error debugging database structure", e);
        }

        Log.d("WallpaperRepositoryImpl", "===============================");
    }

    /**
     * Insert sample data for testing
     */
    public void insertSampleData() {
        Log.d("WallpaperRepositoryImpl", "=== INSERTING SAMPLE DATA ===");

        try {
            long currentTime = System.currentTimeMillis();

            // Tạo sample wallpapers với constructor đơn giản
            WallpaperEntity sample1 = new WallpaperEntity(
                    "sample_1",
                    "default_collection",
                    "guest_user",
                    "https://sample1.jpg",
                    "https://thumb1.jpg",
                    null, // localFileUrl
                    currentTime,
                    currentTime,
                    "Sample Wallpaper 1"
            );

            WallpaperEntity sample2 = new WallpaperEntity(
                    "sample_2",
                    "default_collection",
                    "guest_user",
                    "https://sample2.jpg",
                    "https://thumb2.jpg",
                    null, // localFileUrl
                    currentTime,
                    currentTime,
                    "Sample Wallpaper 2"
            );

            WallpaperEntity sample3 = new WallpaperEntity(
                    "sample_3",
                    "default_c", // Test với collection_id khác
                    "guest_user",
                    "https://sample3.jpg",
                    "https://thumb3.jpg",
                    null, // localFileUrl
                    currentTime,
                    currentTime,
                    "Sample Wallpaper 3"
            );

            // Insert sample data
            insertWallpaper(sample1)
                    .andThen(insertWallpaper(sample2))
                    .andThen(insertWallpaper(sample3))
                    .subscribe(
                            () -> {
                                Log.d("WallpaperRepositoryImpl", "Sample data inserted successfully");
                                // Verify data was inserted
                                getWallpapersByCollection("default_collection")
                                        .subscribe(
                                                wallpapers -> Log.d("WallpaperRepositoryImpl",
                                                        "Verification: Found " + wallpapers.size() + " wallpapers in default_collection"),
                                                error -> Log.e("WallpaperRepositoryImpl",
                                                        "Error verifying sample data", error)
                                        );
                            },
                            error -> Log.e("WallpaperRepositoryImpl", "Error inserting sample data", error)
                    );

        } catch (Exception e) {
            Log.e("WallpaperRepositoryImpl", "Error in insertSampleData", e);
        }

        Log.d("WallpaperRepositoryImpl", "=============================");
    }

    /**
     * Get all wallpapers for debugging
     */
    public Single<List<Wallpaper>> getAllWallpapers() {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG getAllWallpapers ===");

        return wallpaperDao.getWallpapersByUser("") // Empty string to get all
                .doOnSuccess(entities -> {
                    Log.d("WallpaperRepositoryImpl", "Found " + entities.size() + " total wallpapers");
                    for (WallpaperEntity entity : entities) {
                        Log.d("WallpaperRepositoryImpl", "- ID: " + entity.getWallpaperId() +
                                ", Collection: " + entity.getCollectionId() +
                                ", User: " + entity.getUserId());
                    }
                })
                .map(entities -> entities.stream()
                        .map(entity -> wallpaperMapper.toDomain(entity))
                        .collect(Collectors.toList()));
    }

    /**
     * Utility method to get wallpapers by collection for guest users
     */
    public Single<List<Wallpaper>> getWallpapersForGuest(String collectionId) {
        Log.d("WallpaperRepositoryImpl", "=== DEBUG getWallpapersForGuest ===");
        Log.d("WallpaperRepositoryImpl", "Collection: " + collectionId);

        return getWallpapersByCollection(collectionId)
                .doOnSuccess(wallpapers -> {
                    Log.d("WallpaperRepositoryImpl", "Found " + wallpapers.size() + " wallpapers for guest");
                });
    }
}