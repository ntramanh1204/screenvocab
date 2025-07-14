package com.ntramanh1204.screenvocab.domain.usecase.collection;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;
import com.ntramanh1204.screenvocab.data.mapper.CollectionMapper;
import io.reactivex.rxjava3.core.Completable;
import java.util.stream.Collectors;

public class DeleteCollectionUseCase {
    private final CollectionRepository collectionRepository;
    private final WordRepository wordRepository;

    public DeleteCollectionUseCase(CollectionRepository collectionRepository, WordRepository wordRepository) {
        this.collectionRepository = collectionRepository;
        this.wordRepository = wordRepository;
    }

    public Completable execute(Collection collection) {
        if (collection == null) {
            return Completable.error(new IllegalArgumentException("Collection cannot be null"));
        }
        return wordRepository.getWordsByCollection(collection.getCollectionId())
                .flatMapCompletable(words -> Completable.concat(
                        words.stream()
                                .map(word -> wordRepository.deleteWord(word))
                                .collect(Collectors.toList())
                ))
                .andThen(collectionRepository.deleteCollection(CollectionMapper.toEntity(collection)));
    }
}