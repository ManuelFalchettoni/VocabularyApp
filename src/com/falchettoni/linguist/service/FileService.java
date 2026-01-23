package com.falchettoni.linguist.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.falchettoni.linguist.model.VocabularyEntry;

public class FileService {
    private final String filePath;

    // Constructor to initialize file path
    public FileService(String filePath) {
        this.filePath = filePath; // Store the file path for later use
    }

    // Method to save vocabulary to file
    public void saveVocabulary(ArrayList<VocabularyEntry> vocabulary) {
        try (PrintWriter writer = new PrintWriter(filePath)) { // Create PrintWriter to write to the file
            for (VocabularyEntry entry : vocabulary) {
                writer.printf("%s;%s;%s;%d%n", entry.german(), entry.english(), entry.type(), entry.level()); // Write each entry in the specified format
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error saving vocabulary: " + e.getMessage());
        }
    }

    // Method to load vocabulary from file
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
    public void deleteWord(String germanWord, List<VocabularyEntry> vocabulary) {
        List<VocabularyEntry> updatedVocabulary = vocabulary.stream() // Create a stream from the vocabulary list
                .filter(entry -> !entry.german().equalsIgnoreCase(germanWord))// Remove the entry with the specified German word
                .toList(); // Collect the remaining entries into a new list
        try {
            saveVocabulary(new ArrayList<>(updatedVocabulary));// Convert back to ArrayList
            System.out.println("Word '" + germanWord + "' deleted from vocabulary if it existed.");
        } catch (Exception e) {
            System.err.println("Error saving the file: " + e.getMessage());
        }

    }

    // Method to search for a word in the vocabulary
    public VocabularyEntry searchWord(String germanWord, List<VocabularyEntry> vocabulary) {
        for (VocabularyEntry entry : vocabulary) {
            if (entry.german().equalsIgnoreCase(germanWord)) {
                return entry; // Word found
            }
        }
        return null; // Word not found
    }

    //Method to update a word in the vocabulary
    public void updateWord(String germanWord, VocabularyEntry updatedEntry, List<VocabularyEntry> vocabulary) {
        boolean found = false;
        for (int i = 0; i < vocabulary.size(); i++) {
            if (vocabulary.get(i).german().equalsIgnoreCase(germanWord)) {
                vocabulary.set(i, updatedEntry); // Replace the old entry with the updated one
                found = true;
                break;
            }
        }
        if (found) {
            try {
                saveVocabulary(new ArrayList<>(vocabulary));
                System.out.println("✅ Word '" + germanWord + "' successfully updated.");
            } catch (Exception e) {
                System.err.println("Error saving the file: " + e.getMessage());
            }
        } else {
            System.out.println("Word '" + germanWord + "' not found in vocabulary. No update performed.");
        }
    }

    //Method to search for duplicates in the vocabulary
    public boolean isDuplicate(String germanWord, List<VocabularyEntry> currentList) { // Check for duplicates
        return currentList.stream()
                .anyMatch(entry -> entry.german().equalsIgnoreCase(germanWord.trim()));// Check for duplicates ignoring case and whitespace
    }

    //Method to get all words of a specific level
    public ArrayList<VocabularyEntry> getWordsByLevel(int level, List<VocabularyEntry> vocabulary) {
        ArrayList<VocabularyEntry> filteredWords = new ArrayList<>();
        for (VocabularyEntry entry : vocabulary) {// Iterate through the vocabulary list
            if (entry.level() == level) {
                filteredWords.add(entry); // Add entry if it matches the specified level
            }
        }
        return filteredWords; // Return the list of filtered words
    }

    //Method to get all words of a specific type
    public ArrayList<VocabularyEntry> getWordsByType(String type, List<VocabularyEntry> vocabulary) {
        ArrayList<VocabularyEntry> filteredWords = new ArrayList<>();
        for (VocabularyEntry entry : vocabulary) {
            if (entry.type().equalsIgnoreCase(type.trim())) {// Iterate through the vocabulary list
                filteredWords.add(entry); // Add entry if it matches the specified type
            }
        }
        return filteredWords; // Return the list of filtered words
    }


    //Method to clear the entire vocabulary
    public void clearVocabulary() {
        try {
            saveVocabulary(new ArrayList<>()); // Save an empty list to the file
        } catch (Exception e) {
            System.err.println("Error clearing vocabulary: " + e.getMessage());
        }

    }

    //Method to count total words in the vocabulary
    public int countTotalWords(List<VocabularyEntry> vocabulary) {
        return vocabulary.size(); // Return the size of the vocabulary list
    }

    //Method to get a random word from the vocabulary
    public VocabularyEntry getRandomWord(List<VocabularyEntry> vocabulary) {
        if (vocabulary.isEmpty()) {
            return null; // Return null if the vocabulary is empty
        }
        int randomIndex = (int) (Math.random() * vocabulary.size()); // Generate a random index
        return vocabulary.get(randomIndex); // Return the random entry
    }
}