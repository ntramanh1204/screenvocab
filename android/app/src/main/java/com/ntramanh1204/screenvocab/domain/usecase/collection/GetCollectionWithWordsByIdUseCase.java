package com.ntramanh1204.screenvocab.domain.usecase.collection;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;

import io.reactivex.rxjava3.core.Single;

public class GetCollectionWithWordsByIdUseCase {
    private final CollectionRepository repository;

    public GetCollectionWithWordsByIdUseCase(CollectionRepository repository) {
        this.repository = repository;
    }

    public Single<Collection> execute(String id) {
        return repository.getCollectionWithWordsById(id);
    }
}
