package com.ntramanh1204.screenvocab.data.repository;

import com.ntramanh1204.screenvocab.data.local.dao.WordDao;
import com.ntramanh1204.screenvocab.data.local.entities.WordEntity;
import com.ntramanh1204.screenvocab.data.mapper.WordMapper;
import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class WordRepositoryImpl implements WordRepository {
    private final WordDao wordDao;

    public WordRepositoryImpl(WordDao wordDao) {
        this.wordDao = wordDao;
    }

    @Override
    public Completable insertWord(Word word) {
        WordEntity entity = WordMapper.toEntity(word);
        return wordDao.insert(entity);
    }

    @Override
    public Completable updateWord(Word word) {
        WordEntity entity = WordMapper.toEntity(word);
        return wordDao.update(entity);
    }

    @Override
    public Completable deleteWord(Word word) {
        WordEntity entity = WordMapper.toEntity(word);
        return wordDao.delete(entity);
    }

    @Override
    public Single<List<Word>> getWordsByCollection(String collectionId) {
        return wordDao.getWordsByCollection(collectionId)
                .map(wordEntities -> wordEntities.stream()
                        .map(WordMapper::toDomain)
                        .collect(Collectors.toList()));
    }
}
