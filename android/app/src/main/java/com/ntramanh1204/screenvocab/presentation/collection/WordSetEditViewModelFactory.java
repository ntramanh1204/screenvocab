package com.ntramanh1204.screenvocab.presentation.collection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ntramanh1204.screenvocab.domain.usecase.collection.DeleteCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionDetailUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.UpdateCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.CreateWordUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.DeleteWordUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.GetWordsByCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.UpdateWordUseCase;

public class WordSetEditViewModelFactory implements ViewModelProvider.Factory {
    private final GetCollectionDetailUseCase getCollectionDetailUseCase;
    private final GetWordsByCollectionUseCase getWordsByCollectionUseCase;
    private final UpdateCollectionUseCase updateCollectionUseCase;
    private final DeleteCollectionUseCase deleteCollectionUseCase;
    private final CreateWordUseCase createWordUseCase;
    private final UpdateWordUseCase updateWordUseCase;
    private final DeleteWordUseCase deleteWordUseCase;

    public WordSetEditViewModelFactory(GetCollectionDetailUseCase getCollectionDetailUseCase,
                                       GetWordsByCollectionUseCase getWordsByCollectionUseCase,
                                       UpdateCollectionUseCase updateCollectionUseCase,
                                       DeleteCollectionUseCase deleteCollectionUseCase,
                                       CreateWordUseCase createWordUseCase,
                                       UpdateWordUseCase updateWordUseCase,
                                       DeleteWordUseCase deleteWordUseCase) {
        this.getCollectionDetailUseCase = getCollectionDetailUseCase;
        this.getWordsByCollectionUseCase = getWordsByCollectionUseCase;
        this.updateCollectionUseCase = updateCollectionUseCase;
        this.deleteCollectionUseCase = deleteCollectionUseCase;
        this.createWordUseCase = createWordUseCase;
        this.updateWordUseCase = updateWordUseCase;
        this.deleteWordUseCase = deleteWordUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WordSetEditViewModel.class)) {
            return (T) new WordSetEditViewModel(getCollectionDetailUseCase,
                    getWordsByCollectionUseCase, updateCollectionUseCase,
                    deleteCollectionUseCase, createWordUseCase,
                    updateWordUseCase, deleteWordUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

