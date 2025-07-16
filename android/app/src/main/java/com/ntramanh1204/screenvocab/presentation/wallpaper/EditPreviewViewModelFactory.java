package com.ntramanh1204.screenvocab.presentation.wallpaper;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionWithWordsByIdUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.wallpaper.SaveWallpaperUseCase;

public class EditPreviewViewModelFactory implements ViewModelProvider.Factory {
    private final GetCollectionWithWordsByIdUseCase getCollectionWithWordsByIdUseCase;
    private final SaveWallpaperUseCase saveWallpaperUseCase;

    public EditPreviewViewModelFactory(GetCollectionWithWordsByIdUseCase useCase, SaveWallpaperUseCase saveWallpaperUseCase) {
        this.getCollectionWithWordsByIdUseCase = useCase;
        this.saveWallpaperUseCase = saveWallpaperUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditPreviewViewModel.class)) {
            return (T) new EditPreviewViewModel(getCollectionWithWordsByIdUseCase,
                    saveWallpaperUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

