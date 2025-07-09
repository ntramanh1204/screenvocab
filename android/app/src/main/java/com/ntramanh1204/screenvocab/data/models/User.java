package com.ntramanh1204.screenvocab.data.models;

public class User {
    private String userId;
    private String displayName;
    private String email;
    private boolean isGuest;
    private long createdAt;
    private long lastSyncAt;
    
    // Default constructor required for Firebase
    public User() {}
    
    public User(String userId, String displayName, String email, boolean isGuest) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.isGuest = isGuest;
        this.createdAt = System.currentTimeMillis();
        this.lastSyncAt = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
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