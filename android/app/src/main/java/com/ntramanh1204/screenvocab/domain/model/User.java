package com.ntramanh1204.screenvocab.domain.model;

public class User {
    private final String userId;
    private final String displayName;
    private final String email;
    private final boolean isGuest;
    private final long createdAt;

    public User(String userId, String displayName, String email, boolean isGuest, long createdAt) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.isGuest = isGuest;
        this.createdAt = createdAt;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public boolean isGuest() { return isGuest; }
    public long getCreatedAt() { return createdAt; }

    // Business logic
    public boolean canCreateCollection() { return !isGuest; }
    public String getDisplayNameOrDefault() {
        return displayName != null ? displayName : "Guest User";
    }
}