package com.ntramanh1204.screenvocab.presentation.wallpaper;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionByIdUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionWithWordsByIdUseCase;

public class EditPreviewViewModelFactory implements ViewModelProvider.Factory {
    private final GetCollectionWithWordsByIdUseCase getCollectionWithWordsByIdUseCase;

    public EditPreviewViewModelFactory(GetCollectionWithWordsByIdUseCase useCase) {
        this.getCollectionWithWordsByIdUseCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditPreviewViewModel.class)) {
            return (T) new EditPreviewViewModel(getCollectionWithWordsByIdUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

