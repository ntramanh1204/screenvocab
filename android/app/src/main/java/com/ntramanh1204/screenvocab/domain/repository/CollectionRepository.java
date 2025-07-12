package com.ntramanh1204.screenvocab.domain.repository;

import com.ntramanh1204.screenvocab.data.local.entities.CollectionEntity;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface CollectionRepository {
    Completable insertCollection(CollectionEntity collection);
    Completable updateCollection(CollectionEntity collection);
    Completable deleteCollection(CollectionEntity collection);
    Single<List<CollectionEntity>> getCollectionsByUser(String userId);
}
