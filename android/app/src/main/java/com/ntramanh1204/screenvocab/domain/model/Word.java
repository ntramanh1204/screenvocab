package com.ntramanh1204.screenvocab.domain.model;

import java.util.UUID;

public class Word {
    private final String wordId;
    private final String term;
    private final String pronunciation;
    private final String definition;
    private final String language;
    private final int position;
    private final long createdAt;
    private final String collectionId;

    public Word(String wordId, String term, String pronunciation, String definition,
                String language, int position, long createdAt, String collectionId) {
        this.wordId = wordId;
        this.term = term;
        this.pronunciation = pronunciation;
        this.definition = definition;
        this.language = language;
        this.position = position;
        this.createdAt = createdAt;
        this.collectionId = collectionId;
    }

    // Factory method for creating new words
    public static Word create(String term, String pronunciation, String definition,
                              String language, int position, String collectionId) {
        return new Word(
                UUID.randomUUID().toString(),
                term,
                pronunciation,
                definition,
                language,
                position,
                System.currentTimeMillis(),
                collectionId
        );
    }

    // Factory method for creating word with updated text
    public Word updateText(String term, String pronunciation, String definition) {
        return new Word(
                this.wordId,
                term,
                pronunciation,
                definition,
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
                this.term,
                this.pronunciation,
                this.definition,
                this.language,
                newPosition,
                this.createdAt,
                this.collectionId
        );
    }

    // Getters
    public String getWordId() { return wordId; }
    public String getTerm() { return term; }
    public String getPronunciation() { return pronunciation; }
    public String getDefinition() { return definition; }
    public String getLanguage() { return language; }
    public int getPosition() { return position; }
    public long getCreatedAt() { return createdAt; }
    public String getCollectionId() { return collectionId; }

    // Business logic methods
    public boolean isValid() {
        return term != null && !term.trim().isEmpty() &&
                pronunciation != null && !pronunciation.trim().isEmpty() &&
                language != null && !language.trim().isEmpty();
    }

    public boolean hasThreeFields() {
        return definition != null && !definition.trim().isEmpty();
    }

    public String getDisplayText() {
        if (hasThreeFields()) {
            return term + " • " + pronunciation + " • " + definition;
        } else {
            return term + " • " + pronunciation;
        }
    }

    public boolean matchesSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }

        String lowerQuery = query.toLowerCase().trim();
        return term.toLowerCase().contains(lowerQuery) ||
                pronunciation.toLowerCase().contains(lowerQuery) ||
                (definition != null && definition.toLowerCase().contains(lowerQuery));
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
                ", term='" + term + '\'' +
                ", pronunciation='" + pronunciation + '\'' +
                ", definition='" + definition + '\'' +
                ", language='" + language + '\'' +
                ", position=" + position +
                ", createdAt=" + createdAt +
                ", collectionId='" + collectionId + '\'' +
                '}';
    }
}