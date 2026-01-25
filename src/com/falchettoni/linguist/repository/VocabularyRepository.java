package com.falchettoni.linguist.repository;

import com.falchettoni.linguist.model.VocabularyEntry; // Import the VocabularyEntry model

import java.util.List;

public interface VocabularyRepository {
    List<VocabularyEntry> loadVocabulary(); // Method to load vocabulary entries
    void saveVocabulary(List<VocabularyEntry> vocabulary); // Method to save vocabulary entries
    boolean deleteWord(String word, List<VocabularyEntry> vocabulary); // Method to delete a vocabulary entry by  word
    void update (String word,VocabularyEntry newEntry, List<VocabularyEntry> vocabulary); // Method to update a vocabulary entry by  word

    List<VocabularyEntry> findByType(String type, List<VocabularyEntry> vocabulary); // Method to find vocabulary entries by type
    List<VocabularyEntry> findByLevel(int level, List<VocabularyEntry> vocabulary); // Method to find vocabulary entries by level
    VocabularyEntry findByWord(String word, List<VocabularyEntry> vocabulary); // Method to find a vocabulary entry by  word

    int countByType(String type, List<VocabularyEntry> vocabulary); // Method to count vocabulary entries by type
    int countByLevel(int level, List<VocabularyEntry> vocabulary); // Method to count vocabulary entries by level
    int countTotal(List<VocabularyEntry> vocabulary); // Method to count total vocabulary entries
}