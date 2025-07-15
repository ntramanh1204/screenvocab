package com.ntramanh1204.screenvocab.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

@Entity(tableName = "wallpapers")
public class WallpaperEntity {
    @PrimaryKey
    @NonNull
    public String wallpaperId;
    public String collectionId;
    public String theme;
    public int rows;
    public int cols;
    public String textHierarchy; // JSON or CSV string
    public String cloudinaryUrl;
    public String thumbnailUrl;
    public String localFileUrl;
    public long createdAt;
    public String resolution;
    public String format;
    public long fileSize;

    public WallpaperEntity() {
    }

    @Ignore
    public WallpaperEntity(@NonNull String wallpaperId, String collectionId, String theme, int rows, int cols, String textHierarchy, String cloudinaryUrl, String thumbnailUrl, String localFileUrl, long createdAt, String resolution, String format, long fileSize) {
        this.wallpaperId = wallpaperId;
        this.collectionId = collectionId;
        this.theme = theme;
        this.rows = rows;
        this.cols = cols;
        this.textHierarchy = textHierarchy;
        this.cloudinaryUrl = cloudinaryUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.localFileUrl = localFileUrl;
        this.createdAt = createdAt;
        this.resolution = resolution;
        this.format = format;
        this.fileSize = fileSize;
    }
    @NonNull
    public String getWallpaperId() {
        return wallpaperId;
    }

    public void setWallpaperId(@NonNull String wallpaperId) {
        this.wallpaperId = wallpaperId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public String getTextHierarchy() {
        return textHierarchy;
    }

    public void setTextHierarchy(String textHierarchy) {
        this.textHierarchy = textHierarchy;
    }

    public String getCloudinaryUrl() {
        return cloudinaryUrl;
    }

    public void setCloudinaryUrl(String cloudinaryUrl) {
        this.cloudinaryUrl = cloudinaryUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLocalFileUrl() {
        return localFileUrl;
    }

    public void setLocalFileUrl(String localFileUrl) {
        this.localFileUrl = localFileUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}