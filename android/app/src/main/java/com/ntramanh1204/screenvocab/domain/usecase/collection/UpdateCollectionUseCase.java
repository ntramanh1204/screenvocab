package com.ntramanh1204.screenvocab.domain.usecase.collection;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.data.mapper.CollectionMapper;
import com.ntramanh1204.screenvocab.core.utils.ValidationUtils;

import io.reactivex.rxjava3.core.Completable;

public class UpdateCollectionUseCase {

    private final CollectionRepository collectionRepository;

    public UpdateCollectionUseCase(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public Completable execute(Collection collection) {
        return validateInput(collection)
                .andThen(collectionRepository.updateCollection(CollectionMapper.toEntity(collection)));
    }

    private Completable validateInput(Collection collection) {
        return Completable.fromAction(() -> {
            if (collection == null) throw new IllegalArgumentException("Collection cannot be null");
            if (ValidationUtils.isEmpty(collection.getName()))
                throw new IllegalArgumentException("Collection name cannot be empty");
        });
    }
}