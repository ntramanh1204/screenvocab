package com.ntramanh1204.screenvocab.domain.usecase.collection;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.data.mapper.CollectionMapper;

import io.reactivex.rxjava3.core.Single;

public class GetCollectionByIdUseCase {

    private final CollectionRepository collectionRepository;

    public GetCollectionByIdUseCase(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public Single<Collection> execute(String collectionId) {
        return collectionRepository.getCollectionById(collectionId)
                .map(CollectionMapper::toDomain);
    }
}
