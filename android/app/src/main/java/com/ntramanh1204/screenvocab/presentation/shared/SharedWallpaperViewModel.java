package com.ntramanh1204.screenvocab.presentation.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedWallpaperViewModel extends ViewModel {
    private final MutableLiveData<Boolean> wallpaperSaved = new MutableLiveData<>(false);

    public LiveData<Boolean> getWallpaperSaved() {
        return wallpaperSaved;
    }

    public void setWallpaperSaved(boolean saved) {
        wallpaperSaved.setValue(saved);
    }
}
