package com.ntramanh1204.screenvocab.presentation.collection;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.ntramanh1204.screenvocab.core.di.AppContainer;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionsByUserUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.DeleteCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.repository.AuthRepository;
import com.ntramanh1204.screenvocab.domain.usecase.collection.UpdateCollectionUseCase;

public class WordSetListViewModelFactory implements ViewModelProvider.Factory {
    private final GetCollectionsByUserUseCase getCollectionsByUserUseCase;
    private final UpdateCollectionUseCase updateCollectionUseCase;
    private final DeleteCollectionUseCase deleteCollectionUseCase;
    private final AuthRepository authRepository;

    public WordSetListViewModelFactory(AppContainer appContainer) {
        this.getCollectionsByUserUseCase = appContainer.getGetCollectionsByUserUseCase();
        this.deleteCollectionUseCase = appContainer.getDeleteCollectionUseCase();
        this.authRepository = appContainer.getAuthRepository();
        this.updateCollectionUseCase = appContainer.getUpdateCollectionUseCase();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WordSetListViewModel.class)) {
            return (T) new WordSetListViewModel(
                    getCollectionsByUserUseCase,
                    deleteCollectionUseCase,
                    updateCollectionUseCase,
                    authRepository
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}