package com.ntramanh1204.screenvocab.presentation.collection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.ntramanh1204.screenvocab.core.di.AppContainer;

public class CreateWordSetViewModelFactory implements ViewModelProvider.Factory {
    private final AppContainer appContainer;

    public CreateWordSetViewModelFactory(AppContainer appContainer) {
        this.appContainer = appContainer;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreateWordSetViewModel.class)) {
            return (T) new CreateWordSetViewModel(
                    appContainer.getCreateCollectionUseCase(),
                    appContainer.getUpdateCollectionUseCase(),
                    appContainer.getCreateWordUseCase(),
                    appContainer.getUpdateWordUseCase(),
                    appContainer.getDeleteWordUseCase(),
                    appContainer.getGetWordsByCollectionUseCase(),
                    appContainer.getAuthRepository()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}