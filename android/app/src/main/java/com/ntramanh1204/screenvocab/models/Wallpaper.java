package com.ntramanh1204.screenvocab.models;

public class Wallpaper {
    private String id;
    private String imageUrl;
    private String thumbnailUrl;
    private String title;
    private String userId;
    private long createdAt;

    public Wallpaper() {
        // Required empty constructor for Firestore
    }

    public Wallpaper(String id, String imageUrl, String thumbnailUrl, String title, String userId, long createdAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}