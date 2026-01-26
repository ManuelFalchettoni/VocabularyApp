package com.falchettoni.linguist.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.falchettoni.linguist.model.VocabularyEntry;
import com.falchettoni.linguist.repository.VocabularyRepository;

public class FileService implements VocabularyRepository {
    private final String filePath;

    // Constructor to initialize file path
    public FileService(String filePath) {
        this.filePath = filePath; // Store the file path for later use
    }

    // Method to save vocabulary to file
    @Override
    public void saveVocabulary(List<VocabularyEntry> vocabulary) {
        try (PrintWriter writer = new PrintWriter(filePath)) { // Create PrintWriter to write to the file
            for (VocabularyEntry entry : vocabulary) {
                writer.printf("%s;%s;%s;%d%n", entry.german(), entry.english(), entry.type(), entry.level()); // Write each entry in the specified format
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error saving vocabulary: " + e.getMessage());
        }
    }

    // Method to load vocabulary from file
    @Override
    public ArrayList<VocabularyEntry> loadVocabulary() {
        ArrayList<VocabularyEntry> vocabulary = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) { // Check if the file exists
            System.out.println("No existing vocabulary file found. Starting with an empty notebook.");
            return vocabulary; // Return empty list if file doesn't exist
        }
        try (var scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) { // Read until the end of the file
                String line = scanner.nextLine(); // Read each line
                String[] parts = line.split(";"); // Split line into parts
                if (parts.length == 4) { // Ensure there are exactly 4 parts
                    String de = parts[0];
                    String en = parts[1];
                    String type = parts[2];
                    int level = Integer.parseInt(parts[3].trim()); // Trim to avoid whitespace issues
                    vocabulary.add(new VocabularyEntry(de, en, type, level)); // Create and add the entry
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error loading vocabulary: " + e.getMessage());
        }
        return vocabulary;
    }

    // Method to delete a word from the vocabulary
    @Override
    public boolean deleteWord(String word, List<VocabularyEntry> vocabulary) {
        boolean removed = vocabulary.removeIf(entry -> entry.german().equalsIgnoreCase(word)); // Remove entry if it matches the german word
        if (removed) {
            try {
                saveVocabulary(new ArrayList<>(vocabulary)); // Save the updated vocabulary
                System.out.println("✅ Word '" + word + "' successfully deleted.");
            } catch (Exception e) {
                System.err.println("Error saving the file: " + e.getMessage());
            }
        }
        return removed;
    }

    // Method to search for a word in the vocabulary
    @Override
    public VocabularyEntry findByWord(String word, List<VocabularyEntry> vocabulary) {
        return vocabulary.stream()
                .filter(e -> e.german().equalsIgnoreCase(word))
                .findFirst()
                .orElse(null);
    }

    //Method to update a word in the vocabulary
    @Override
    public void updateWord(String word, VocabularyEntry updatedEntry, List<VocabularyEntry> vocabulary) {
        boolean found = false;
        for (int i = 0; i < vocabulary.size(); i++) {
            if (vocabulary.get(i).german().equalsIgnoreCase(word)) {
                vocabulary.set(i, updatedEntry); // Replace the old entry with the updated one
                found = true;
                break;
            }
        }
        if (found) {
            try {
                saveVocabulary(new ArrayList<>(vocabulary));
                System.out.println("✅ Word '" + word + "' successfully updated.");
            } catch (Exception e) {
                System.err.println("Error saving the file: " + e.getMessage());
            }
        } else {
            System.out.println("Word '" + word + "' not found in vocabulary. No update performed.");
        }
    }

    //Method to search for duplicates in the vocabulary
    @Override
    public boolean isDuplicate(String word, List<VocabularyEntry> currentList) { // Check for duplicates
        return currentList.stream()
                .anyMatch(entry -> entry.german().equalsIgnoreCase(word.trim()));// Check for duplicates ignoring case and whitespace
    }

    //Method to get all words of a specific level
    @Override
    public List<VocabularyEntry> findByLevel(int level, List<VocabularyEntry> vocabulary) {
        return vocabulary.stream()
                .filter(e -> e.level() == level)
                .toList();
    }

    //Method to get all words of a specific type
    @Override
    public List<VocabularyEntry> findByType(String type, List<VocabularyEntry> vocabulary) {
        return vocabulary.stream()
                .filter(e -> e.type().equalsIgnoreCase(type))
                .toList();
    }


    //Method to clear the entire vocabulary
    @Override
    public void clearVocabulary() {
        try {
            saveVocabulary(new ArrayList<>()); // Save an empty list to the file
        } catch (Exception e) {
            System.err.println("Error clearing vocabulary: " + e.getMessage());
        }

    }

    //Method to count total words in the vocabulary
    @Override
    public int countTotal(List<VocabularyEntry> vocabulary) {
        return vocabulary.size(); // Return the size of the vocabulary list
    }

    // Method to count total words by level
    @Override
    public int countByLevel(int level, List<VocabularyEntry> vocabulary) {
        return (int) vocabulary.stream()
                .filter(e -> e.level() == level)
                .count();

    }

    //Method to count by type
    @Override
    public int countByType(String type, List<VocabularyEntry> vocabulary) {
        return (int) vocabulary.stream()
                .filter(e -> e.type().equalsIgnoreCase(type))
                .count();
    }
}
