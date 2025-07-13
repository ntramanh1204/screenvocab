package com.ntramanh1204.screenvocab.domain.usecase.word;

import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;
import com.ntramanh1204.screenvocab.data.mapper.WordMapper;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;

public class GetWordsByCollectionUseCase {

    private final WordRepository wordRepository;

    public GetWordsByCollectionUseCase(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public Single<List<Word>> execute(String collectionId) {
        return wordRepository.getWordsByCollection(collectionId)
                .map(wordEntities -> wordEntities.stream()
                        .map(WordMapper::toDomain)
                        .collect(Collectors.toList())
                );
    }
}