package com.ntramanh1204.screenvocab.domain.model;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Wallpaper {
    private final String wallpaperId;
    private final String collectionId;
    private final Theme theme;
    private final GridDimensions gridDimensions;
    private final List<String> textHierarchy;
    private final String cloudinaryUrl;
    private final String thumbnailUrl;
    private final String localFileUrl;
    private final long createdAt;
    private final WallpaperMetadata metadata;

    public Wallpaper(String wallpaperId, String collectionId, Theme theme,
                     GridDimensions gridDimensions, List<String> textHierarchy,
                     String cloudinaryUrl, String thumbnailUrl, String localFileUrl,
                     long createdAt, WallpaperMetadata metadata) {
        this.wallpaperId = wallpaperId;
        this.collectionId = collectionId;
        this.theme = theme;
        this.gridDimensions = gridDimensions;
        this.textHierarchy = textHierarchy;
        this.cloudinaryUrl = cloudinaryUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.localFileUrl = localFileUrl;
        this.createdAt = createdAt;
        this.metadata = metadata;
    }

    // Factory method for creating new wallpapers
    public static Wallpaper create(String collectionId, Theme theme,
                                   GridDimensions gridDimensions, List<String> textHierarchy) {
        return new Wallpaper(
                UUID.randomUUID().toString(),
                collectionId,
                theme,
                gridDimensions,
                textHierarchy,
                null, // cloudinaryUrl set after upload
                null, // thumbnailUrl set after upload
                null, // localFileUrl set after save
                System.currentTimeMillis(),
                WallpaperMetadata.createDefault()
        );
    }

    // Factory method for updating URLs after upload/save
    public Wallpaper updateUrls(String cloudinaryUrl, String thumbnailUrl, String localFileUrl) {
        return new Wallpaper(
                this.wallpaperId,
                this.collectionId,
                this.theme,
                this.gridDimensions,
                this.textHierarchy,
                cloudinaryUrl,
                thumbnailUrl,
                localFileUrl,
                this.createdAt,
                this.metadata
        );
    }

    // Factory method for updating metadata
    public Wallpaper updateMetadata(WallpaperMetadata metadata) {
        return new Wallpaper(
                this.wallpaperId,
                this.collectionId,
                this.theme,
                this.gridDimensions,
                this.textHierarchy,
                this.cloudinaryUrl,
                this.thumbnailUrl,
                this.localFileUrl,
                this.createdAt,
                metadata
        );
    }

    // Getters
    public String getWallpaperId() { return wallpaperId; }
    public String getCollectionId() { return collectionId; }
    public Theme getTheme() { return theme; }
    public GridDimensions getGridDimensions() { return gridDimensions; }
    public List<String> getTextHierarchy() { return textHierarchy; }
    public String getCloudinaryUrl() { return cloudinaryUrl; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getLocalFileUrl() { return localFileUrl; }
    public long getCreatedAt() { return createdAt; }
    public WallpaperMetadata getMetadata() { return metadata; }

    // Business logic methods
    public boolean isValid() {
        return collectionId != null && !collectionId.trim().isEmpty() &&
                theme != null && gridDimensions != null &&
                textHierarchy != null && !textHierarchy.isEmpty();
    }

    public boolean hasCloudUrl() {
        return cloudinaryUrl != null && !cloudinaryUrl.trim().isEmpty();
    }

    public boolean hasThumbnail() {
        return thumbnailUrl != null && !thumbnailUrl.trim().isEmpty();
    }

    public boolean hasLocalFile() {
        return localFileUrl != null && !localFileUrl.trim().isEmpty();
    }

    public boolean isUploaded() {
        return hasCloudUrl() && hasThumbnail();
    }

    public boolean isSavedLocally() {
        return hasLocalFile();
    }

    public String getDisplayName() {
        return theme.getDisplayName() + " • " + gridDimensions.toString();
    }

    public String getDisplayInfo() {
        return metadata.getResolution() + " • " + metadata.getFormat().toUpperCase() +
                " • " + formatFileSize(metadata.getFileSize());
    }

    public String getBestAvailableUrl() {
        if (hasCloudUrl()) {
            return cloudinaryUrl;
        } else if (hasLocalFile()) {
            return localFileUrl;
        }
        return null;
    }

    public String getBestThumbnailUrl() {
        if (hasThumbnail()) {
            return thumbnailUrl;
        } else if (hasCloudUrl()) {
            return cloudinaryUrl;
        } else if (hasLocalFile()) {
            return localFileUrl;
        }
        return null;
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    // Enums and inner classes
    public enum Theme {
        HSK_PINK("HSK Pink", "#FFB6C1", "#FFFFFF", "#333333"),
        CLEAN_WHITE("Clean White", "#FFFFFF", "#F5F5F5", "#333333"),
        SOFT_GRAY("Soft Gray", "#F0F0F0", "#E0E0E0", "#333333"),
        NIGHT_MODE("Night Mode", "#1A1A1A", "#2D2D2D", "#FFFFFF");

        private final String displayName;
        private final String backgroundColor;
        private final String cardColor;
        private final String textColor;

        Theme(String displayName, String backgroundColor, String cardColor, String textColor) {
            this.displayName = displayName;
            this.backgroundColor = backgroundColor;
            this.cardColor = cardColor;
            this.textColor = textColor;
        }

        public String getDisplayName() { return displayName; }
        public String getBackgroundColor() { return backgroundColor; }
        public String getCardColor() { return cardColor; }
        public String getTextColor() { return textColor; }
    }

    public static class GridDimensions {
        private final int rows;
        private final int cols;

        public GridDimensions(int rows, int cols) {
            this.rows = Math.max(1, rows);
            this.cols = Math.max(1, cols);
        }

        public int getRows() { return rows; }
        public int getCols() { return cols; }
        public int getTotalCells() { return rows * cols; }

        public boolean canFit(int wordCount) {
            return getTotalCells() >= wordCount;
        }

        @Override
        public String toString() {
            return rows + "×" + cols;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GridDimensions that = (GridDimensions) o;
            return rows == that.rows && cols == that.cols;
        }

        @Override
        public int hashCode() {
            return rows * 31 + cols;
        }
    }

    public static class WallpaperMetadata {
        private final String resolution;
        private final String format;
        private final long fileSize;

        public WallpaperMetadata(String resolution, String format, long fileSize) {
            this.resolution = resolution;
            this.format = format;
            this.fileSize = fileSize;
        }

        public static WallpaperMetadata createDefault() {
            return new WallpaperMetadata("1080x2340", "PNG", 0);
        }

        public String getResolution() { return resolution; }
        public String getFormat() { return format; }
        public long getFileSize() { return fileSize; }

        @Override
        public String toString() {
            return resolution + " • " + format;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallpaper wallpaper = (Wallpaper) o;
        return wallpaperId.equals(wallpaper.wallpaperId);
    }

    @Override
    public int hashCode() {
        return wallpaperId.hashCode();
    }

    @Override
    public String toString() {
        return "Wallpaper{" +
                "wallpaperId='" + wallpaperId + '\'' +
                ", collectionId='" + collectionId + '\'' +
                ", theme=" + theme +
                ", gridDimensions=" + gridDimensions +
                ", textHierarchy=" + textHierarchy +
                ", hasCloudUrl=" + hasCloudUrl() +
                ", hasLocalFile=" + hasLocalFile() +
                ", createdAt=" + createdAt +
                ", metadata=" + metadata +
                '}';
    }
}