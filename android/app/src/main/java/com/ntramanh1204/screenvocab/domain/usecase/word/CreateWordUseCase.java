package com.ntramanh1204.screenvocab.domain.usecase.word;

import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.data.mapper.WordMapper;
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

    // Input parameters for creating a word
    public static class Params {
        private final String primaryText;
        private final String secondaryText;
        private final String tertiaryText;
        private final String language;
        private final String collectionId;
        private final String userId;

        public Params(String primaryText, String secondaryText, String tertiaryText,
                      String language, String collectionId, String userId) {
            this.primaryText = primaryText;
            this.secondaryText = secondaryText;
            this.tertiaryText = tertiaryText;
            this.language = language;
            this.collectionId = collectionId;
            this.userId = userId;
        }

        public String getPrimaryText() { return primaryText; }
        public String getSecondaryText() { return secondaryText; }
        public String getTertiaryText() { return tertiaryText; }
        public String getLanguage() { return language; }
        public String getCollectionId() { return collectionId; }
        public String getUserId() { return userId; }
    }

    // Execute use case - REQ-VOCAB-001, REQ-VOCAB-007
    public Single<Word> execute(Params params) {
        return validateInput(params)
                .andThen(checkCollectionExists(params.getCollectionId(), params.getUserId()))
                .andThen(checkCollectionSize(params.getCollectionId()))
                .andThen(checkDuplicateWord(params))
                .andThen(createAndSaveWord(params));
    }

    // Validate input parameters - REQ-VOCAB-007
    private Completable validateInput(Params params) {
        return Completable.fromAction(() -> {
            if (ValidationUtils.isEmpty(params.getPrimaryText())) {
                throw new IllegalArgumentException("Primary text cannot be empty");
            }
            if (ValidationUtils.isEmpty(params.getSecondaryText())) {
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

            // Length validation
            if (params.getPrimaryText().length() > 100) {
                throw new IllegalArgumentException("Primary text is too long (max 100 characters)");
            }
            if (params.getSecondaryText().length() > 100) {
                throw new IllegalArgumentException("Secondary text is too long (max 100 characters)");
            }
            if (params.getTertiaryText() != null && params.getTertiaryText().length() > 100) {
                throw new IllegalArgumentException("Tertiary text is too long (max 100 characters)");
            }
        });
    }

    // Check if collection exists and belongs to user
    private Completable checkCollectionExists(String collectionId, String userId) {
        return collectionRepository.getCollectionsByUser(userId)
                .flatMapCompletable(collections -> {
                    boolean collectionExists = collections.stream()
                            .anyMatch(collection -> collection.getCollectionId().equals(collectionId));

                    if (!collectionExists) {
                        return Completable.error(new IllegalArgumentException("Collection not found or does not belong to user"));
                    }
                    return Completable.complete();
                });
    }

    // Check collection size limit - REQ-VOCAB-003 (max 150 words)
    private Completable checkCollectionSize(String collectionId) {
        return wordRepository.getWordsByCollection(collectionId)
                .flatMapCompletable(words -> {
                    if (words.size() >= 150) {
                        return Completable.error(new IllegalStateException("Collection has reached maximum limit of 150 words"));
                    }
                    return Completable.complete();
                });
    }

    // Check for duplicate words in collection - REQ-VOCAB-007
    private Completable checkDuplicateWord(Params params) {
        // TODO: CHECK TRUNG TU CHAT HON
//        return wordRepository.getWordsByCollection(params.getCollectionId())
//                .flatMapCompletable(words -> {
//                    boolean hasDuplicate = words.stream()
//                            .anyMatch(wordEntity ->
//                                    wordEntity.getPrimaryText().equalsIgnoreCase(params.getPrimaryText().trim()) ||
//                                            wordEntity.getSecondaryText().equalsIgnoreCase(params.getSecondaryText().trim())
//                            );
//
//                    if (hasDuplicate) {
//                        return Completable.error(new IllegalArgumentException("Word already exists in collection"));
//                    }
//                    return Completable.complete();
//                });
        return Completable.complete(); // Bỏ kiểm tra trùng từ để cho phép trùng primaryText và secondaryText
    }

    // Create and save word
    private Single<Word> createAndSaveWord(Params params) {
        return wordRepository.getWordsByCollection(params.getCollectionId())
                .map(words -> words.size()) // Get next position
                .flatMap(nextPosition -> {
                    // Create new word using factory method
                    Word newWord = Word.create(
                            params.getPrimaryText().trim(),
                            params.getSecondaryText().trim(),
                            params.getTertiaryText() != null ? params.getTertiaryText().trim() : null,
                            params.getLanguage().trim(),
                            nextPosition,
                            params.getCollectionId()
                    );

                    // Convert to entity and save
                    return wordRepository.insertWord(WordMapper.toEntity(newWord))
                            .andThen(Single.just(newWord));
                });
    }
}