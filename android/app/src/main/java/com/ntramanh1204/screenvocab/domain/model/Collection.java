package com.ntramanh1204.screenvocab.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Collection {
    private final String collectionId;
    private final String name;
    private final long createdAt;
    private final long updatedAt;
    private final String userId;
    private final List<Word> words;

    public Collection(String collectionId, String name, long createdAt, long updatedAt,
                      String userId, List<Word> words) {
        this.collectionId = collectionId;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.words = words != null ? new ArrayList<>(words) : new ArrayList<>();
    }

    // Factory method for creating new collections
    public static Collection create(String name, String userId) {
        return new Collection(
                UUID.randomUUID().toString(),
                name,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                userId,
                new ArrayList<>()
        );
    }

    // Factory method for creating collection with updated name
    public Collection updateName(String newName) {
        return new Collection(
                this.collectionId,
                newName,
                this.createdAt,
                System.currentTimeMillis(),
                this.userId,
                this.words
        );
    }

    // Factory method for creating collection with updated words
    public Collection updateWords(List<Word> newWords) {
        return new Collection(
                this.collectionId,
                this.name,
                this.createdAt,
                System.currentTimeMillis(),
                this.userId,
                newWords
        );
    }

    // Getters
    public String getCollectionId() { return collectionId; }
    public String getName() { return name; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public String getUserId() { return userId; }
    public List<Word> getWords() { return Collections.unmodifiableList(words); }

    // Business logic methods
    public boolean isValid() {
        return name != null && !name.trim().isEmpty();
    }

    public int getWordCount() {
        return words.size();
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public boolean isFull() {
        return words.size() >= 150; // As per SRS requirement
    }

    public boolean canAddWord() {
        return !isFull();
    }

    public boolean canGenerateWallpaper() {
        return !isEmpty() && words.size() <= 150;
    }

    public List<String> getSupportedLanguages() {
        return words.stream()
                .map(Word::getLanguage)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }

    public String getPrimaryLanguage() {
        return words.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Word::getLanguage,
                        java.util.stream.Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse("en");
    }

    public String getDisplayName() {
        return name != null ? name : "Untitled Collection";
    }

    public String getDisplayInfo() {
        return getWordCount() + " words • " + getPrimaryLanguage().toUpperCase();
    }

    public boolean containsWord(String primaryText, String secondaryText) {
        return words.stream().anyMatch(word ->
                word.getPrimaryText().equalsIgnoreCase(primaryText) &&
                        word.getSecondaryText().equalsIgnoreCase(secondaryText)
        );
    }

    public List<Word> searchWords(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getWords();
        }

        return words.stream()
                .filter(word -> word.matchesSearch(query))
                .collect(java.util.stream.Collectors.toList());
    }

    // Grid calculation for wallpaper generation
    public GridDimensions calculateOptimalGrid() {
        int wordCount = getWordCount();
        if (wordCount == 0) {
            return new GridDimensions(1, 1);
        }

        // Calculate optimal grid dimensions
        int cols = (int) Math.ceil(Math.sqrt(wordCount));
        int rows = (int) Math.ceil((double) wordCount / cols);

        // Adjust for better aspect ratio (portrait wallpaper)
        if (rows < cols) {
            int temp = rows;
            rows = cols;
            cols = temp;
        }

        return new GridDimensions(rows, cols);
    }

    // Inner class for grid dimensions
    public static class GridDimensions {
        private final int rows;
        private final int cols;

        public GridDimensions(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        public int getRows() { return rows; }
        public int getCols() { return cols; }
        public int getTotalCells() { return rows * cols; }

        @Override
        public String toString() {
            return rows + "x" + cols;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return collectionId.equals(that.collectionId);
    }

    @Override
    public int hashCode() {
        return collectionId.hashCode();
    }

    @Override
    public String toString() {
        return "Collection{" +
                "collectionId='" + collectionId + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userId='" + userId + '\'' +
                ", wordCount=" + getWordCount() +
                '}';
    }
}