package com.ntramanh1204.screenvocab.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ntramanh1204.screenvocab.data.local.entities.WordEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface WordDao {
    @Insert
    Completable insert(WordEntity word);
    @Update
    Completable update(WordEntity word);
    @Delete
    Completable delete(WordEntity word);
    @Query("SELECT * FROM words WHERE collectionId = :collectionId")
    Single<List<WordEntity>> getWordsByCollection(String collectionId);
}
