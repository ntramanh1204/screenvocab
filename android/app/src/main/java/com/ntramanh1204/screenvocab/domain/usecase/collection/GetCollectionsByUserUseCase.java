package com.ntramanh1204.screenvocab.domain.usecase.collection;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.data.mapper.CollectionMapper;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;

public class GetCollectionsByUserUseCase {

    private final CollectionRepository collectionRepository;

    public GetCollectionsByUserUseCase(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public Single<List<Collection>> execute(String userId) {
        return collectionRepository.getCollectionsByUser(userId)
                .map(entities -> entities.stream()
                        .map(CollectionMapper::toDomain)
                        .collect(Collectors.toList())
                );
    }
}