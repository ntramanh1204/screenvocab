package com.ntramanh1204.screenvocab.presentation.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.repository.AuthRepository;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.domain.repository.WallpaperRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final CollectionRepository collectionRepository;
    private final WallpaperRepository wallpaperRepository;
    private final AuthRepository authRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Integer> collectionsCount = new MutableLiveData<>(0);
    private final MutableLiveData<List<String>> wallpapers = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public HomeViewModel(CollectionRepository collectionRepository, WallpaperRepository wallpaperRepository, AuthRepository authRepository) {
        this.collectionRepository = collectionRepository;
        this.wallpaperRepository = wallpaperRepository;
        this.authRepository = authRepository;
    }

    public LiveData<Integer> getCollectionsCount() { return collectionsCount; }
    public LiveData<List<String>> getWallpapers() { return wallpapers; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void loadData() {
        isLoading.setValue(true);
        error.setValue(null);
        compositeDisposable.add(
                authRepository.getCurrentUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> loadDataForUser(user.getUserId()),
                                throwable -> {
                                    error.setValue("Failed to get user: " + throwable.getMessage());
                                    isLoading.setValue(false);
                                    collectionsCount.setValue(0);
                                    wallpapers.setValue(new ArrayList<>());
                                }
                        )
        );
    }

    public void loadData(String collectionId) {
        isLoading.setValue(true);
        error.setValue(null);
        compositeDisposable.add(
                authRepository.getCurrentUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    String userId = user != null ? user.getUserId() : "guest";
                                    loadDataForUser(userId);
                                    // Load wallpapers for collectionId
                                    compositeDisposable.add(
                                            wallpaperRepository.getWallpapersByCollection(collectionId)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(
                                                            wallpaperEntities -> {
                                                                List<String> wallpaperUrls = new ArrayList<>();
                                                                for (WallpaperEntity entity : wallpaperEntities) {
                                                                    wallpaperUrls.add(entity.getLocalFileUrl() != null ? entity.getLocalFileUrl() : entity.getThumbnailUrl());
                                                                }
                                                                wallpapers.setValue(wallpaperUrls);
                                                                isLoading.setValue(false);
                                                            },
                                                            throwable -> {
                                                                error.setValue("Failed to load wallpapers: " + throwable.getMessage());
                                                                wallpapers.setValue(new ArrayList<>());
                                                                isLoading.setValue(false);
                                                            }
                                                    )
                                    );
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


    private void loadDataForUser(String userId) {
        // Load collections count
        compositeDisposable.add(
                collectionRepository.getCollectionsByUser(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                collections -> {
                                    collectionsCount.setValue(collections.size());
                                    isLoading.setValue(false);
                                },
                                throwable -> {
                                    error.setValue("Failed to load collections: " + throwable.getMessage());
                                    collectionsCount.setValue(0);
                                    isLoading.setValue(false);
                                }
                        )
        );

        // Load wallpapers (placeholder for now)
//        List<String> placeholderWallpapers = new ArrayList<>();
//        for (int i = 0; i < 9; i++) {
//            placeholderWallpapers.add("demo_placeholder");
//        }
//        wallpapers.setValue(placeholderWallpapers);

        // TODO: Uncomment to load real wallpapers
//        /*
        compositeDisposable.add(
            wallpaperRepository.getWallpapersByCollection(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    wallpaperEntities -> {
                        List<String> wallpaperUrls = new ArrayList<>();
                        for (WallpaperEntity entity : wallpaperEntities) {
//                            wallpaperUrls.add(entity.getLocalFileUrl());
                            wallpaperUrls.add(entity.getLocalFileUrl() != null ? entity.getLocalFileUrl() : entity.getThumbnailUrl());
                        }
                        wallpapers.setValue(wallpaperUrls);
                        isLoading.setValue(false);
                    },
                    throwable -> {
                        error.setValue("Failed to load wallpapers: " + throwable.getMessage());
                        wallpapers.setValue(new ArrayList<>());
                        isLoading.setValue(false);
                    }
                )
        );
//        */
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}