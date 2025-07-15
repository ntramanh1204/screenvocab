package com.ntramanh1204.screenvocab.domain.usecase.word;

import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.core.utils.ValidationUtils;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Completable;

public class CreateWordUseCase {

    private final WordRepository wordRepository;
    private final CollectionRepository collectionRepository;

    public CreateWordUseCase(WordRepository wordRepository, CollectionRepository collectionRepository) {
        this.wordRepository = wordRepository;
        this.collectionRepository = collectionRepository;
    }

    public static class Params {
        private final String term;
        private final String pronunciation;
        private final String definition;
        private final String language;
        private final String collectionId;
        private final String userId;

        public static Params of(Word word, String userId) {
            return new Params(
                    word.getTerm(),
                    word.getPronunciation(),
                    word.getDefinition(),
                    word.getLanguage(),
                    word.getCollectionId(),
                    userId
            );
        }
        public Params(String term, String pronunciation, String definition,
                      String language, String collectionId, String userId) {
            this.term = term;
            this.pronunciation = pronunciation;
            this.definition = definition;
            this.language = language;
            this.collectionId = collectionId;
            this.userId = userId;
        }

        // getters
        public String getTerm() { return term; }
        public String getPronunciation() { return pronunciation; }
        public String getDefinition() { return definition; }
        public String getLanguage() { return language; }
        public String getCollectionId() { return collectionId; }
        public String getUserId() { return userId; }
    }

    public Single<Word> execute(Params params) {
        return validateInput(params)
                .andThen(checkCollectionExists(params.getCollectionId(), params.getUserId()))
                .andThen(checkCollectionSize(params.getCollectionId()))
                .andThen(checkDuplicateWord(params))
                .andThen(createAndSaveWord(params));
    }

    private Completable validateInput(Params params) {
        return Completable.fromAction(() -> {
            if (ValidationUtils.isEmpty(params.getTerm())) {
                throw new IllegalArgumentException("Primary text cannot be empty");
            }
            if (ValidationUtils.isEmpty(params.getPronunciation())) {
                throw new IllegalArgumentException("Secondary text cannot be empty");
            }
            if (ValidationUtils.isEmpty(params.getLanguage())) {
                throw new IllegalArgumentException("Language cannot be empty");
            }
            if (ValidationUtils.isEmpty(params.getCollectionId())) {
                throw new IllegalArgumentException("Collection ID cannot be empty");
            }
            if (ValidationUtils.isEmpty(params.getUserId())) {
                throw new IllegalArgumentException("User ID cannot be empty");
            }
            if (params.getTerm().length() > 100) {
                throw new IllegalArgumentException("Term text is too long (max 100 chars)");
            }
            if (params.getPronunciation().length() > 100) {
                throw new IllegalArgumentException("Pronunciation text is too long (max 100 chars)");
            }
            if (params.getDefinition() != null && params.getDefinition().length() > 100) {
                throw new IllegalArgumentException("Definition text is too long (max 100 chars)");
            }
        });
    }

    private Completable checkCollectionExists(String collectionId, String userId) {
        return collectionRepository.getCollectionsByUser(userId)
                .flatMapCompletable(collections -> {
                    boolean exists = collections.stream()
                            .anyMatch(c -> c.getCollectionId().equals(collectionId));
                    if (!exists) {
                        return Completable.error(new IllegalArgumentException("Collection not found or does not belong to user"));
                    }
                    return Completable.complete();
                });
    }

    private Completable checkCollectionSize(String collectionId) {
        return wordRepository.getWordsByCollection(collectionId)
                .flatMapCompletable(words -> {
                    if (words.size() >= 150) {
                        return Completable.error(new IllegalStateException("Collection reached max 150 words"));
                    }
                    return Completable.complete();
                });
    }

    private Completable checkDuplicateWord(Params params) {
        return wordRepository.getWordsByCollection(params.getCollectionId())
                .flatMapCompletable(words -> {
                    boolean duplicate = words.stream()
                            .anyMatch(w ->
                                    w.getTerm().equalsIgnoreCase(params.getTerm().trim()) &&
                                            w.getPronunciation().equalsIgnoreCase(params.getPronunciation().trim())
                            );
                    if (duplicate) {
                        return Completable.error(new IllegalArgumentException("Word already exists in collection"));
                    }
                    return Completable.complete();
                });
    }

    private Single<Word> createAndSaveWord(Params params) {
        return wordRepository.getWordsByCollection(params.getCollectionId())
                .map(words -> words.size()) // vị trí mới = size hiện tại
                .flatMap(position -> {
                    Word newWord = Word.create(
                            params.getTerm().trim(),
                            params.getPronunciation().trim(),
                            params.getDefinition() != null ? params.getDefinition().trim() : null,
                            params.getLanguage().trim(),
                            position,
                            params.getCollectionId()
                    );
                    return wordRepository.insertWord(newWord)
                            .andThen(Single.just(newWord));
                });
    }
}
