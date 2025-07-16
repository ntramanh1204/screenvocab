package com.ntramanh1204.screenvocab.presentation.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntramanh1204.screenvocab.domain.model.Wallpaper;
import com.ntramanh1204.screenvocab.domain.repository.AuthRepository;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.domain.usecase.wallpaper.GetWallpapersByUserUseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private static final int PAGE_SIZE = 9; // 3x3 grid per page

    private final CollectionRepository collectionRepository;
    private final GetWallpapersByUserUseCase getWallpapersByUserUseCase;
    private final AuthRepository authRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    // LiveData
    private final MutableLiveData<Integer> collectionsCount = new MutableLiveData<>(0);
    private final MutableLiveData<List<Wallpaper>> wallpapers = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isLoadingMore = new MutableLiveData<>(false);

    // Pagination state
    private int currentPage = 0;
    private boolean isLastPage = false;
    private String currentUserId = null;
    private final List<Wallpaper> allWallpapers = new ArrayList<>();

    public HomeViewModel(CollectionRepository collectionRepository,
                         GetWallpapersByUserUseCase getWallpapersByUserUseCase,
                         AuthRepository authRepository) {
        this.collectionRepository = collectionRepository;
        this.getWallpapersByUserUseCase = getWallpapersByUserUseCase;
        this.authRepository = authRepository;
    }

    // Getters
    public LiveData<Integer> getCollectionsCount() { return collectionsCount; }
    public LiveData<List<Wallpaper>> getWallpapers() { return wallpapers; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getIsLoadingMore() { return isLoadingMore; }

    /**
     * Load initial data (first page)
     */
    public void loadData() {
        isLoading.setValue(true);
        error.setValue(null);
        resetPagination();

        compositeDisposable.add(
                authRepository.getCurrentUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    currentUserId = user != null ? user.getUserId() : "guest";
                                    loadDataForUser(currentUserId);
                                },
                                throwable -> {
                                    error.setValue("Failed to get user: " + throwable.getMessage());
                                    isLoading.setValue(false);
                                    collectionsCount.setValue(0);
                                    wallpapers.setValue(new ArrayList<>());
                                }
                        )
        );
    }

    /**
     * Load data for specific collection (if needed)
     */
    public void loadData(String collectionId) {
        // For now, we'll load all user wallpapers
        // You can extend this to filter by collection if needed
        loadData();
    }

    /**
     * Load more wallpapers (for infinite scroll)
     */
    public void loadMoreWallpapers() {
        if (isLastPage || isLoadingMore.getValue() == Boolean.TRUE || currentUserId == null) {
            return;
        }

        isLoadingMore.setValue(true);
        error.setValue(null);

        compositeDisposable.add(
                getWallpapersByUserUseCase.execute(currentUserId, currentPage + 1, PAGE_SIZE)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                newWallpapers -> {
                                    if (newWallpapers.isEmpty()) {
                                        isLastPage = true;
                                    } else {
                                        currentPage++;
                                        allWallpapers.addAll(newWallpapers);
                                        wallpapers.setValue(new ArrayList<>(allWallpapers));
                                    }
                                    isLoadingMore.setValue(false);
                                },
                                throwable -> {
                                    error.setValue("Failed to load more wallpapers: " + throwable.getMessage());
                                    isLoadingMore.setValue(false);
                                }
                        )
        );
    }

    /**
     * Refresh wallpapers (pull to refresh)
     */
    public void refreshWallpapers() {
        loadData();
    }

    /**
     * Check if can load more wallpapers
     */
    public boolean canLoadMore() {
        return !isLastPage && isLoadingMore.getValue() != Boolean.TRUE;
    }

    private void loadDataForUser(String userId) {
        // Load collections count
        loadCollectionsCount(userId);

        // Load first page of wallpapers
        loadWallpapersFirstPage(userId);
    }

    private void loadCollectionsCount(String userId) {
        compositeDisposable.add(
                collectionRepository.getCollectionsByUser(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                collections -> collectionsCount.setValue(collections.size()),
                                throwable -> {
                                    error.setValue("Failed to load collections: " + throwable.getMessage());
                                    collectionsCount.setValue(0);
                                }
                        )
        );
    }

    private void loadWallpapersFirstPage(String userId) {
        compositeDisposable.add(
                getWallpapersByUserUseCase.execute(userId, 0, PAGE_SIZE)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                wallpaperList -> {
                                    allWallpapers.clear();
                                    allWallpapers.addAll(wallpaperList);
                                    wallpapers.setValue(new ArrayList<>(allWallpapers));

                                    // Check if it's the last page
                                    if (wallpaperList.size() < PAGE_SIZE) {
                                        isLastPage = true;
                                    }

                                    isLoading.setValue(false);
                                },
                                throwable -> {
                                    error.setValue("Failed to load wallpapers: " + throwable.getMessage());
                                    wallpapers.setValue(new ArrayList<>());
                                    isLoading.setValue(false);
                                }
                        )
        );
    }

    private void resetPagination() {
        currentPage = 0;
        isLastPage = false;
        allWallpapers.clear();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}