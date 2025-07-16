package com.ntramanh1204.screenvocab.domain.repository;

import com.ntramanh1204.screenvocab.domain.model.Word;

import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface WordRepository {
    Completable insertWord(Word word);
    Completable updateWord(Word word);
    Completable deleteWord(Word word);
    Single<List<Word>> getWordsByCollection(String collectionId);
}

