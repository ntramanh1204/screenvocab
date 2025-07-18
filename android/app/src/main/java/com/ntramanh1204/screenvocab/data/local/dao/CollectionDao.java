package com.ntramanh1204.screenvocab.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.ntramanh1204.screenvocab.data.local.entities.CollectionEntity;
import com.ntramanh1204.screenvocab.data.local.entities.CollectionWithWords;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CollectionDao {
    @Insert
    Completable insert(CollectionEntity collection);
    @Update
    Completable update(CollectionEntity collection);
    @Delete
    Completable delete(CollectionEntity collection);
    @Query("SELECT * FROM collections WHERE userId = :userId ORDER BY createdAt DESC")
    Single<List<CollectionEntity>> getCollectionsByUser(String userId);
    @Query("SELECT * FROM collections WHERE syncStatus = 'PENDING'")
    Single<List<CollectionEntity>> getPendingCollections();
    @Query("SELECT * FROM collections WHERE collectionId = :collectionId")
    Single<CollectionEntity> getCollectionById(String collectionId);

    @Transaction
    @Query("SELECT * FROM collections WHERE collectionId = :collectionId")
    Single<CollectionWithWords> getCollectionWithWordsById(String collectionId);

}