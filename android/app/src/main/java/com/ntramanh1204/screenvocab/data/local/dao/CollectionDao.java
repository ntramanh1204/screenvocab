package com.ntramanh1204.screenvocab.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ntramanh1204.screenvocab.data.local.entities.CollectionEntity;

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
    @Query("SELECT * FROM collections WHERE userId = :userId")
    Single<List<CollectionEntity>> getCollectionsByUser(String userId);
    @Query("SELECT * FROM collections WHERE syncStatus = 'PENDING'")
    Single<List<CollectionEntity>> getPendingCollections();
}