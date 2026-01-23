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
            System.out.println("Vocabulary successfully saved to " + filePath);
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
            System.out.println("Vocabulary successfully loaded from " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Error loading vocabulary: " + e.getMessage());
        }
        return vocabulary;
    }

    // Method to delete a word from the vocabulary
    public void deleteWord(String germanWord) {
        ArrayList<VocabularyEntry> vocabulary = loadVocabulary();// Load existing vocabulary
        List<VocabularyEntry> updatedVocabulary = vocabulary.stream() // Create a stream from the vocabulary list
                .filter(entry -> !entry.german().equalsIgnoreCase(germanWord))// Remove the entry with the specified German word
                .toList(); // Collect the remaining entries into a new list
        saveVocabulary(new ArrayList<>(updatedVocabulary));// Convert back to ArrayList
        System.out.println("Word '" + germanWord + "' deleted from vocabulary if it existed.");
    }

    // Method to search for a word in the vocabulary
    public VocabularyEntry searchWord(String germanWord) {
        ArrayList<VocabularyEntry> vocabulary = loadVocabulary();
        for (VocabularyEntry entry : vocabulary) {
            if (entry.german().equalsIgnoreCase(germanWord)) {
                return entry; // Word found
            }
        }
        return null; // Word not found
    }

    //Method to update a word in the vocabulary
    public void updateWord(String germanWord, VocabularyEntry updatedEntry) {
        ArrayList<VocabularyEntry> vocabulary = loadVocabulary(); // Load existing vocabulary
        ArrayList<VocabularyEntry> updatedVocabulary = new ArrayList<>(); // New list to hold updated entries
        for (VocabularyEntry entry : vocabulary) {
            if (entry.german().equalsIgnoreCase(germanWord)) {
                updatedVocabulary.add(updatedEntry); // Add the updated entry
            } else {
                updatedVocabulary.add(entry); // Keep the existing entry
            }
        }
        saveVocabulary(updatedVocabulary); // Save the updated vocabulary back to the file
        System.out.println("Word '" + germanWord + "' updated in vocabulary if it existed.");
    }


}
