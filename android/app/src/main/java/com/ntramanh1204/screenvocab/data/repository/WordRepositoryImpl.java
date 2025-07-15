package com.ntramanh1204.screenvocab.data.repository;

import android.util.Log;

import com.ntramanh1204.screenvocab.data.local.dao.WordDao;
import com.ntramanh1204.screenvocab.data.local.entities.WordEntity;
import com.ntramanh1204.screenvocab.data.mapper.WordMapper;
import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;

import java.util.ArrayList;
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
        Log.d("WordRepository", "Inserting word: " + word.toString());
        WordEntity entity = WordMapper.toEntity(word);
        return wordDao.insert(entity)
                .doOnComplete(() -> Log.d("WordRepository", "Word inserted successfully: " + word.getTerm()))
                .doOnError(throwable -> Log.e("WordRepository", "Error inserting word: " + throwable.getMessage(), throwable));
    }

    @Override
    public Completable updateWord(Word word) {
        Log.d("WordRepository", "Updating word: " + word.toString());
        WordEntity entity = WordMapper.toEntity(word);
        return wordDao.update(entity)
                .doOnComplete(() -> Log.d("WordRepository", "Word updated successfully: " + word.getTerm()))
                .doOnError(throwable -> Log.e("WordRepository", "Error updating word: " + throwable.getMessage(), throwable));
    }

    @Override
    public Completable deleteWord(Word word) {
        Log.d("WordRepository", "Deleting word: " + word.toString());
        WordEntity entity = WordMapper.toEntity(word);
        return wordDao.delete(entity)
                .doOnComplete(() -> Log.d("WordRepository", "Word deleted successfully: " + word.getTerm()))
                .doOnError(throwable -> Log.e("WordRepository", "Error deleting word: " + throwable.getMessage(), throwable));
    }

    @Override
    public Single<List<Word>> getWordsByCollection(String collectionId) {
        Log.d("WordRepository", "Fetching words for collection: " + collectionId);
        return wordDao.getWordsByCollection(collectionId)
                .map(wordEntities -> {
                    List<Word> words = wordEntities.stream()
                            .map(WordMapper::toDomain)
                            .collect(Collectors.toList());
                    Log.d("WordRepository", "Fetched " + words.size() + " words for collection: " + collectionId);
                    return words;
                })
                .doOnError(throwable -> Log.e("WordRepository", "Error fetching words: " + throwable.getMessage(), throwable));
    }
}
