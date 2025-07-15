package com.ntramanh1204.screenvocab.presentation.collection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionDetailUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.GetWordsByCollectionUseCase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WordSetDetailViewModel extends ViewModel {
    public final MutableLiveData<Collection> collection = new MutableLiveData<>();
    public final MutableLiveData<List<Word>> words = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private final GetCollectionDetailUseCase getCollectionDetailUseCase;
    private final GetWordsByCollectionUseCase getWordsByCollectionUseCase;

    public WordSetDetailViewModel(
            GetCollectionDetailUseCase getCollectionDetailUseCase,
            GetWordsByCollectionUseCase getWordsByCollectionUseCase
    ) {
        this.getCollectionDetailUseCase = getCollectionDetailUseCase;
        this.getWordsByCollectionUseCase = getWordsByCollectionUseCase;
    }

    public void loadCollectionDetails(String collectionId) {
        isLoading.setValue(true);

        getCollectionDetailUseCase.execute(collectionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(collection -> {
                    this.collection.setValue(collection);
                    return getWordsByCollectionUseCase.execute(collectionId);
                })
                .subscribe(wordList -> {
                    words.setValue(wordList);
                    isLoading.setValue(false);
                }, error -> {
                    isLoading.setValue(false);
                });
    }
}
