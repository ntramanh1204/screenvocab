package com.ntramanh1204.screenvocab.domain.usecase.wallpaper;

import com.ntramanh1204.screenvocab.domain.model.Wallpaper;
import com.ntramanh1204.screenvocab.domain.repository.WallpaperRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class GetWallpapersByUserUseCase {

    private final WallpaperRepository wallpaperRepository;

    public GetWallpapersByUserUseCase(WallpaperRepository wallpaperRepository) {
        this.wallpaperRepository = wallpaperRepository;
    }

    /**
     * Get all wallpapers for a user (for simple cases)
     */
    public Single<List<Wallpaper>> execute(String userId) {
        return wallpaperRepository.getWallpapersByUser(userId);
    }

    /**
     * Get paginated wallpapers for a user (for infinite scroll)
     * @param userId User ID
     * @param page Page number (starts from 0)
     * @param pageSize Number of items per page (recommend 9 for 3x3 grid)
     * @return Single with list of wallpapers
     */
    public Single<List<Wallpaper>> execute(String userId, int page, int pageSize) {
        return wallpaperRepository.getWallpapersByUserPaginated(userId, page, pageSize);
    }

    /**
     * Get wallpapers with offset and limit (alternative pagination approach)
     * @param userId User ID
     * @param offset Number of items to skip
     * @param limit Maximum number of items to return
     * @return Single with list of wallpapers
     */
    public Single<List<Wallpaper>> executeWithOffset(String userId, int offset, int limit) {
        return wallpaperRepository.getWallpapersByUserWithOffset(userId, offset, limit);
    }

    /**
     * Get total count of wallpapers for a user (useful for pagination UI)
     */
    public Single<Integer> getTotalCount(String userId) {
        return wallpaperRepository.getWallpaperCountByUser(userId);
    }
}