package com.ntramanh1204.screenvocab.data.dto;

public class UserDto {
    private final String userId;
    private final String displayName;
    private final String email;
    private final boolean isGuest;
    private final long createdAt;
    private final String profileImageUrl; // Có thể null
    private final long lastLogin;

    public UserDto(String userId, String displayName, String email, boolean isGuest, long createdAt, String profileImageUrl, long lastLogin) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.isGuest = isGuest;
        this.createdAt = createdAt;
        this.profileImageUrl = profileImageUrl;
        this.lastLogin = lastLogin;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public long getLastLogin() {
        return lastLogin;
    }
}