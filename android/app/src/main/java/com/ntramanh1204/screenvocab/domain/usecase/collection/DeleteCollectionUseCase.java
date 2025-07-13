package com.ntramanh1204.screenvocab.domain.usecase.collection;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.data.mapper.CollectionMapper;

import io.reactivex.rxjava3.core.Completable;

public class DeleteCollectionUseCase {

    private final CollectionRepository collectionRepository;

    public DeleteCollectionUseCase(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public Completable execute(Collection collection) {
        if (collection == null) return Completable.error(new IllegalArgumentException("Collection cannot be null"));
        return collectionRepository.deleteCollection(CollectionMapper.toEntity(collection));
    }
}