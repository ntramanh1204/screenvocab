package com.ntramanh1204.screenvocab.domain.repository;

import com.ntramanh1204.screenvocab.data.local.entities.CollectionEntity;
import com.ntramanh1204.screenvocab.domain.model.Collection;

import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface CollectionRepository {
    Completable insertCollection(CollectionEntity collection);
    Completable updateCollection(CollectionEntity collection);
    Completable deleteCollection(CollectionEntity collection);
    Single<List<CollectionEntity>> getCollectionsByUser(String userId);
    Single<CollectionEntity> getCollectionById(String collectionId);
    Single<Collection> getCollectionWithWordsById(String id);
}
