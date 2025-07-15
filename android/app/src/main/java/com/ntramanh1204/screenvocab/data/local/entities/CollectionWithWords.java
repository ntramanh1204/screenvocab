// CollectionWithWords.java
package com.ntramanh1204.screenvocab.data.local.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CollectionWithWords {
    @Embedded
    public CollectionEntity collection;

    @Relation(
            parentColumn = "collectionId",
            entityColumn = "collectionId"
    )
    public List<WordEntity> words;
}
