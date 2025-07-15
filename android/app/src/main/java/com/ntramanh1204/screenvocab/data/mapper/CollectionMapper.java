package com.ntramanh1204.screenvocab.data.mapper;

import com.ntramanh1204.screenvocab.data.local.entities.CollectionEntity;
import com.ntramanh1204.screenvocab.domain.model.Collection;

import java.util.ArrayList;

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
}