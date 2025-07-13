package com.ntramanh1204.screenvocab.data.mapper;

import com.ntramanh1204.screenvocab.data.local.entities.WordEntity;
import com.ntramanh1204.screenvocab.domain.model.Word;

public class WordMapper {

    /**
     * Convert Word domain model to WordEntity
     */
    public static WordEntity toEntity(Word word) {
        if (word == null) return null;

        return new WordEntity(
                word.getWordId(),
                word.getPrimaryText(),
                word.getSecondaryText(),
                word.getTertiaryText(),
                word.getLanguage(),
                word.getPosition(),
                word.getCreatedAt(),
                word.getCollectionId(),
                word.getCollectionId(), // userId lấy từ collectionId tạm thời, cần sửa nếu có userId riêng
                "PENDING"
        );
    }

    /**
     * Convert WordEntity to Word domain model
     */
    public static Word toDomain(WordEntity entity) {
        if (entity == null) return null;

        return new Word(
                entity.getWordId(),
                entity.getPrimaryText(),
                entity.getSecondaryText(),
                entity.getTertiaryText(),
                entity.getLanguage(),
                entity.getPosition(),
                entity.getCreatedAt(),
                entity.getCollectionId()
        );
    }
}