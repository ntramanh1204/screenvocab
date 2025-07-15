package com.ntramanh1204.screenvocab.presentation.collection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionDetailUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.GetWordsByCollectionUseCase;

public class WordSetDetailViewModelFactory implements ViewModelProvider.Factory {
    private final GetCollectionDetailUseCase getCollectionDetailUseCase;
    private final GetWordsByCollectionUseCase getWordsByCollectionUseCase;

    public WordSetDetailViewModelFactory(
            GetCollectionDetailUseCase getCollectionDetailUseCase,
            GetWordsByCollectionUseCase getWordsByCollectionUseCase
    ) {
        this.getCollectionDetailUseCase = getCollectionDetailUseCase;
        this.getWordsByCollectionUseCase = getWordsByCollectionUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WordSetDetailViewModel(
                getCollectionDetailUseCase,
                getWordsByCollectionUseCase
        );
    }
}
