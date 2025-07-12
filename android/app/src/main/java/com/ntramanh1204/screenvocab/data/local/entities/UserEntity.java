package com.ntramanh1204.screenvocab.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String userId;
    private String displayName;
    private String email;
    private boolean isGuest;
    private long createdAt;
    private long lastSyncAt;
    
    // Default constructor required for Firebase
    public UserEntity() {}

    @Ignore
    public UserEntity(@NonNull String userId, String displayName, String email, boolean isGuest) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.isGuest = isGuest;
        this.createdAt = System.currentTimeMillis();
        this.lastSyncAt = System.currentTimeMillis();
    }
    
    // Getters and Setters
    @NonNull
    public String getUserId() { return userId; }
    public void setUserId(@NonNull String userId) { this.userId = userId; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean isGuest() { return isGuest; }
    public void setGuest(boolean guest) { isGuest = guest; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getLastSyncAt() { return lastSyncAt; }
    public void setLastSyncAt(long lastSyncAt) { this.lastSyncAt = lastSyncAt; }
}