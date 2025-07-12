package com.ntramanh1204.screenvocab.data.repository;

import com.ntramanh1204.screenvocab.data.local.dao.WordDao;
import com.ntramanh1204.screenvocab.data.local.entities.WordEntity;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;

import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class WordRepositoryImpl implements WordRepository {
    private final WordDao wordDao;

    public WordRepositoryImpl(WordDao wordDao) {
        this.wordDao = wordDao;
    }

    public Completable insertWord(WordEntity word) {
        return wordDao.insert(word);
    }

    public Completable updateWord(WordEntity word) {
        return wordDao.update(word);
    }

    public Completable deleteWord(WordEntity word) {
        return wordDao.delete(word);
    }

    public Single<List<WordEntity>> getWordsByCollection(String collectionId) {
        return wordDao.getWordsByCollection(collectionId);
    }
}
