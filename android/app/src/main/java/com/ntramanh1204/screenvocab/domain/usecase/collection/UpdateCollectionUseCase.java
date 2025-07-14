package com.ntramanh1204.screenvocab.domain.usecase.collection;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.data.mapper.CollectionMapper;
import com.ntramanh1204.screenvocab.core.utils.ValidationUtils;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class UpdateCollectionUseCase {
    private final CollectionRepository collectionRepository;

    public UpdateCollectionUseCase(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public Single<Collection> execute(Collection collection) {
        return validateInput(collection)
                .andThen(checkDuplicateName(collection))
                .andThen(collectionRepository.updateCollection(CollectionMapper.toEntity(collection))
                        .andThen(Single.just(collection)));
    }

    private Completable validateInput(Collection collection) {
        return Completable.fromAction(() -> {
            if (collection == null) {
                throw new IllegalArgumentException("Collection cannot be null");
            }
            if (ValidationUtils.isEmpty(collection.getName())) {
                throw new IllegalArgumentException("Collection name cannot be empty");
            }
            if (collection.getName().length() > 50) {
                throw new IllegalArgumentException("Collection name too long");
            }
        });
    }

    private Completable checkDuplicateName(Collection collection) {
        return collectionRepository.getCollectionsByUser(collection.getUserId())
                .flatMapCompletable(collections -> {
                    boolean hasDuplicate = collections.stream()
                            .anyMatch(c -> c.getName().equalsIgnoreCase(collection.getName()) &&
                                    !c.getCollectionId().equals(collection.getCollectionId()));
                    if (hasDuplicate) {
                        return Completable.error(new IllegalArgumentException("Collection name already exists"));
                    }
                    return Completable.complete();
                });
    }
}