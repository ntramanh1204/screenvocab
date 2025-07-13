package com.ntramanh1204.screenvocab.domain.usecase.word;

import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;
import com.ntramanh1204.screenvocab.data.mapper.WordMapper;
import com.ntramanh1204.screenvocab.core.utils.ValidationUtils;

import io.reactivex.rxjava3.core.Completable;

public class UpdateWordUseCase {

    private final WordRepository wordRepository;

    public UpdateWordUseCase(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public static class Params {
        private final Word word;

        public Params(Word word) {
            this.word = word;
        }

        public Word getWord() { return word; }
    }

    public Completable execute(Params params) {
        return validateInput(params)
                .andThen(wordRepository.updateWord(WordMapper.toEntity(params.getWord())));
    }

    private Completable validateInput(Params params) {
        return Completable.fromAction(() -> {
            if (params.getWord() == null) throw new IllegalArgumentException("Word cannot be null");
            if (ValidationUtils.isEmpty(params.getWord().getPrimaryText()))
                throw new IllegalArgumentException("Primary text cannot be empty");
            if (ValidationUtils.isEmpty(params.getWord().getSecondaryText()))
                throw new IllegalArgumentException("Secondary text cannot be empty");
        });
    }
}