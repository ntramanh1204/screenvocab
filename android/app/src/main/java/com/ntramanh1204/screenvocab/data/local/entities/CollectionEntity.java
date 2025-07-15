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
    public String description;
    public long createdAt;
    public long updatedAt;
    public String userId;
    public String syncStatus; // PENDING, SYNCED
    public boolean isPublic;

    public CollectionEntity() {
    }

    @Ignore
    public CollectionEntity(@NonNull String collectionId, String name, String description, long createdAt, long updatedAt, String userId, String syncStatus, boolean isPublic) {
        this.collectionId = collectionId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.syncStatus = syncStatus;
        this.isPublic = isPublic;
    }

    @NonNull
    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(@NonNull String collectionId) {
        this.collectionId = collectionId;
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

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

    public boolean isPublic() { return isPublic; }

    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

}