package com.ntramanh1204.screenvocab.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "collections")
public class CollectionEntity {
    @PrimaryKey
    @NonNull
    public String collectionId;
    public String name;
    public long createdAt;
    public long updatedAt;
    public String userId;
    public String syncStatus; // PENDING, SYNCED

    public CollectionEntity() {
    }

    @Ignore
    public CollectionEntity(@NonNull String collectionId, String name, long createdAt, long updatedAt, String userId, String syncStatus) {
        this.collectionId = collectionId;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.syncStatus = syncStatus;
    }

    @NonNull
    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(@NonNull String collectionId) {
        this.collectionId = collectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
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