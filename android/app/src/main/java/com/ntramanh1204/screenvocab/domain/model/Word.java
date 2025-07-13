package com.ntramanh1204.screenvocab.domain.model;

import java.util.UUID;

public class Word {
    private final String wordId;
    private final String primaryText;
    private final String secondaryText;
    private final String tertiaryText;
    private final String language;
    private final int position;
    private final long createdAt;
    private final String collectionId;

    public Word(String wordId, String primaryText, String secondaryText, String tertiaryText,
                String language, int position, long createdAt, String collectionId) {
        this.wordId = wordId;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.tertiaryText = tertiaryText;
        this.language = language;
        this.position = position;
        this.createdAt = createdAt;
        this.collectionId = collectionId;
    }

    // Factory method for creating new words
    public static Word create(String primaryText, String secondaryText, String tertiaryText,
                              String language, int position, String collectionId) {
        return new Word(
                UUID.randomUUID().toString(),
                primaryText,
                secondaryText,
                tertiaryText,
                language,
                position,
                System.currentTimeMillis(),
                collectionId
        );
    }

    // Factory method for creating word with updated text
    public Word updateText(String primaryText, String secondaryText, String tertiaryText) {
        return new Word(
                this.wordId,
                primaryText,
                secondaryText,
                tertiaryText,
                this.language,
                this.position,
                this.createdAt,
                this.collectionId
        );
    }

    // Factory method for updating position
    public Word updatePosition(int newPosition) {
        return new Word(
                this.wordId,
                this.primaryText,
                this.secondaryText,
                this.tertiaryText,
                this.language,
                newPosition,
                this.createdAt,
                this.collectionId
        );
    }

    // Getters
    public String getWordId() { return wordId; }
    public String getPrimaryText() { return primaryText; }
    public String getSecondaryText() { return secondaryText; }
    public String getTertiaryText() { return tertiaryText; }
    public String getLanguage() { return language; }
    public int getPosition() { return position; }
    public long getCreatedAt() { return createdAt; }
    public String getCollectionId() { return collectionId; }

    // Business logic methods
    public boolean isValid() {
        return primaryText != null && !primaryText.trim().isEmpty() &&
                secondaryText != null && !secondaryText.trim().isEmpty() &&
                language != null && !language.trim().isEmpty();
    }

    public boolean hasThreeFields() {
        return tertiaryText != null && !tertiaryText.trim().isEmpty();
    }

    public String getDisplayText() {
        if (hasThreeFields()) {
            return primaryText + " • " + secondaryText + " • " + tertiaryText;
        } else {
            return primaryText + " • " + secondaryText;
        }
    }

    public boolean matchesSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }

        String lowerQuery = query.toLowerCase().trim();
        return primaryText.toLowerCase().contains(lowerQuery) ||
                secondaryText.toLowerCase().contains(lowerQuery) ||
                (tertiaryText != null && tertiaryText.toLowerCase().contains(lowerQuery));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return wordId.equals(word.wordId);
    }

    @Override
    public int hashCode() {
        return wordId.hashCode();
    }

    @Override
    public String toString() {
        return "Word{" +
                "wordId='" + wordId + '\'' +
                ", primaryText='" + primaryText + '\'' +
                ", secondaryText='" + secondaryText + '\'' +
                ", tertiaryText='" + tertiaryText + '\'' +
                ", language='" + language + '\'' +
                ", position=" + position +
                ", createdAt=" + createdAt +
                ", collectionId='" + collectionId + '\'' +
                '}';
    }
}