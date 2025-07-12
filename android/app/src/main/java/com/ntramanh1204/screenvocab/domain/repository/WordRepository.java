package com.ntramanh1204.screenvocab.domain.repository;

import com.ntramanh1204.screenvocab.data.local.entities.WordEntity;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface WordRepository {
    Completable insertWord(WordEntity word);
    Completable updateWord(WordEntity word);
    Completable deleteWord(WordEntity word);
    Single<List<WordEntity>> getWordsByCollection(String collectionId);
}
