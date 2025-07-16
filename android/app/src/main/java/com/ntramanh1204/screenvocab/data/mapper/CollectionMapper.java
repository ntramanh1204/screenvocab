package com.ntramanh1204.screenvocab.data.mapper;

import com.ntramanh1204.screenvocab.data.local.entities.CollectionEntity;
import com.ntramanh1204.screenvocab.data.local.entities.CollectionWithWords;
import com.ntramanh1204.screenvocab.data.local.entities.WordEntity;
import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.model.Word;

import java.util.ArrayList;
import java.util.List;

public class CollectionMapper {
    public static CollectionEntity toEntity(Collection collection) {
        return new CollectionEntity(
                collection.getCollectionId(),
                collection.getName(),
                collection.getDescription(),
                collection.getCreatedAt(),
                collection.getUpdatedAt(),
                collection.getUserId(),
                "PENDING",
                collection.isPublic()
        );
    }

    public static Collection toDomain(CollectionEntity entity) {
        return new Collection(
                entity.getCollectionId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getUserId(),
                new ArrayList<>(),
                entity.isPublic());
    }

    public static Collection toDomain(CollectionWithWords cww) {
        List<Word> wordList = new ArrayList<>();
        if (cww.words != null) {
            for (WordEntity we : cww.words) {
                wordList.add(WordMapper.toDomain(we));
            }
        }

        return new Collection(
                cww.collection.getCollectionId(),
                cww.collection.getName(),
                cww.collection.getDescription(),
                cww.collection.getCreatedAt(),
                cww.collection.getUpdatedAt(),
                cww.collection.getUserId(),
                wordList,
                cww.collection.isPublic()
        );
    }
}