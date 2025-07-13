package com.ntramanh1204.screenvocab.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class WordEntity {
    @PrimaryKey
    @NonNull
    public String wordId;
    public String primaryText;
    public String secondaryText;
    public String tertiaryText;
    public String language;
    public int position;
    public long createdAt;
    public String collectionId;
    public String userId;
    public String syncStatus;

    public WordEntity() {
    }

    @Ignore
    public WordEntity(@NonNull String wordId, String primaryText, String secondaryText, String tertiaryText, String language, int position, long createdAt, String collectionId, String userId, String syncStatus) {
        this.wordId = wordId;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.tertiaryText = tertiaryText;
        this.language = language;
        this.position = position;
        this.createdAt = createdAt;
        this.collectionId = collectionId;
        this.userId = userId;
        this.syncStatus = syncStatus;
    }

    @NonNull
    public String getWordId() {
        return wordId;
    }

    public void setWordId(@NonNull String wordId) {
        this.wordId = wordId;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public String getTertiaryText() {
        return tertiaryText;
    }

    public void setTertiaryText(String tertiaryText) {
        this.tertiaryText = tertiaryText;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
}