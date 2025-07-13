package com.ntramanh1204.screenvocab.domain.usecase.word;

import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;
import com.ntramanh1204.screenvocab.data.mapper.WordMapper;

import io.reactivex.rxjava3.core.Completable;

public class DeleteWordUseCase {

    private final WordRepository wordRepository;

    public DeleteWordUseCase(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public Completable execute(Word word) {
        if (word == null) return Completable.error(new IllegalArgumentException("Word cannot be null"));
        return wordRepository.deleteWord(WordMapper.toEntity(word));
    }
}