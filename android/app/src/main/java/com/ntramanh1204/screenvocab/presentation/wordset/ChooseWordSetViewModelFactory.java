package com.ntramanh1204.screenvocab.presentation.wordset;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionsByUserUseCase;

public class ChooseWordSetViewModelFactory implements ViewModelProvider.Factory {
    private final GetCollectionsByUserUseCase useCase;
    private final String userId;

    public ChooseWordSetViewModelFactory(GetCollectionsByUserUseCase useCase, String userId) {
        this.useCase = useCase;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChooseWordSetViewModel.class)) {
            return (T) new ChooseWordSetViewModel(useCase, userId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}