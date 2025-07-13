package com.ntramanh1204.screenvocab.presentation.model;

import java.util.UUID;

public class WordItem {
    private final String id; // unique for UI, không phải wordId trong DB
    private String term;
    private String pronunciation;
    private String definition;

    public WordItem() {
        this("", "", "");
    }

    public WordItem(String term, String pronunciation, String definition) {
        this.id = UUID.randomUUID().toString();
        this.term = term;
        this.pronunciation = pronunciation;
        this.definition = definition;
    }

    public String getId() { return id; }
    public String getTerm() { return term; }
    public String getPronunciation() { return pronunciation; }
    public String getDefinition() { return definition; }

    public void setTerm(String term) { this.term = term; }
    public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; }
    public void setDefinition(String definition) { this.definition = definition; }

    // Tạo bản sao với trường mới (immutable style)
    public WordItem withTerm(String term) {
        return new WordItem(term, this.pronunciation, this.definition);
    }
    public WordItem withPronunciation(String pronunciation) {
        return new WordItem(this.term, pronunciation, this.definition);
    }
    public WordItem withDefinition(String definition) {
        return new WordItem(this.term, this.pronunciation, definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordItem)) return false;
        WordItem that = (WordItem) o;
        return id.equals(that.id);
    }

    public boolean isValid() {
        return term != null && !term.trim().isEmpty()
                && definition != null && !definition.trim().isEmpty();
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}