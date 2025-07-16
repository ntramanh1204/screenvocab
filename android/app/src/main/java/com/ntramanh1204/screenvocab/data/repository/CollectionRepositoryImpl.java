package com.ntramanh1204.screenvocab.data.repository;

import com.ntramanh1204.screenvocab.data.local.dao.CollectionDao;
import com.ntramanh1204.screenvocab.data.local.entities.CollectionEntity;
import com.ntramanh1204.screenvocab.data.local.entities.CollectionWithWords;
import com.ntramanh1204.screenvocab.data.mapper.CollectionMapper;
import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;

import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class CollectionRepositoryImpl implements CollectionRepository {
    private final CollectionDao collectionDao;

    public CollectionRepositoryImpl(CollectionDao collectionDao) {
        this.collectionDao = collectionDao;
    }

    public Completable insertCollection(CollectionEntity collection) {
        return collectionDao.insert(collection);
    }

    public Completable updateCollection(CollectionEntity collection) {
        return collectionDao.update(collection);
    }

    public Completable deleteCollection(CollectionEntity collection) {
        return collectionDao.delete(collection);
    }

    public Single<List<CollectionEntity>> getCollectionsByUser(String userId) {
        return collectionDao.getCollectionsByUser(userId);
    }

    @Override
    public Single<CollectionEntity> getCollectionById(String collectionId) {
        return collectionDao.getCollectionById(collectionId);
    }

    @Override
    public Single<Collection> getCollectionWithWordsById(String id) {
        return collectionDao.getCollectionWithWordsById(id)
                .map((CollectionWithWords cww) -> CollectionMapper.toDomain(cww));
    }

}
