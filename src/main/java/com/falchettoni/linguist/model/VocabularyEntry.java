package com.falchettoni.linguist.model;

/**
 * A 'Record' is a modern, concise way to model data in Java.
 * It automatically handles fields, getters, and the constructor.
 * This specific record stores our language learning data.
 */
public record VocabularyEntry(String word, String translation, String type, Integer level) {
    @Override
    public String toString() {
        // Formats the output: [NOUN] Das Haus -> The House
        return String.format("[%s] %s -> %s (Level: %d)", type.toUpperCase(), word, translation, level);
    }
}

