package com.ntramanh1204.screenvocab.presentation.wallpaper;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionWithWordsByIdUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.wallpaper.SaveWallpaperUseCase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class EditPreviewViewModel extends ViewModel {

//    private final GetCollectionByIdUseCase getCollectionByIdUseCase;
private final GetCollectionWithWordsByIdUseCase getCollectionWithWordsByIdUseCase;
private final SaveWallpaperUseCase saveWallpaperUseCase;

    private final MutableLiveData<Collection> selectedCollection = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public EditPreviewViewModel(GetCollectionWithWordsByIdUseCase getCollectionWithWordsByIdUseCase, SaveWallpaperUseCase saveWallpaperUseCase) {
        this.getCollectionWithWordsByIdUseCase = getCollectionWithWordsByIdUseCase;
        this.saveWallpaperUseCase = saveWallpaperUseCase;
    }

    public void loadCollectionById(String id) {
        getCollectionWithWordsByIdUseCase.execute(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        collection -> selectedCollection.setValue(collection),
                        throwable -> Log.e("EditPreviewVM", "Error loading collection", throwable)
                );
    }


    public LiveData<Collection> getSelectedCollection() {
        return selectedCollection;
    }

    public Completable saveWallpaper(WallpaperEntity wallpaperEntity) {
        return saveWallpaperUseCase.execute(wallpaperEntity);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
