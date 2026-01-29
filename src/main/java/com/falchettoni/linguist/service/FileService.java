package com.falchettoni.linguist.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


import com.falchettoni.linguist.exceptions.DatabaseException;
import com.falchettoni.linguist.exceptions.DuplicateWordException;
import com.falchettoni.linguist.exceptions.WordNotFoundException;
import com.falchettoni.linguist.model.VocabularyEntry;
import com.falchettoni.linguist.repository.VocabularyRepository;

public class FileService implements VocabularyRepository {
    private final String fileName;

    // Constructor to initialize file path
    public FileService(String fileName) {
        this.fileName = fileName; // Store the file name for later use
    }

    // Method to save vocabulary to file
    @Override
    public void saveVocabulary(List<VocabularyEntry> vocabulary) {

        File file = new File(fileName); // Create a File object for the specified file name

        if (file.getParentFile() != null && !file.getParentFile().exists()) { // Ensure parent directories exist
            if (!file.getParentFile().mkdirs()) { // Create parent directories if they don't exist
                throw new DatabaseException("Failed to create directories for the file path", null); // Throw exception if directory creation fails
            }
        }

        try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) { // Use PrintWriter to write to the file with UTF-8 encoding
            for (VocabularyEntry e : vocabulary) { // Iterate through each vocabulary entry
                writer.printf("%s;%s;%s;%d%n", e.word(), e.translation(), e.type(), e.level()); // Write each entry in the specified format
            }
        } catch (IOException e) {
            throw new DatabaseException("Failed to save vocabulary to file", e);
        }
    }

    // Method to load vocabulary from file
    @Override
    public List<VocabularyEntry> loadVocabulary() {

        File externalFile = new File(fileName); // Create a File object for the specified file name
        // If external file exists, load from there
        if (externalFile.exists()) {
            try (InputStream is = new FileInputStream(externalFile)) { // Use FileInputStream to read the external file
                return parseStream(is); // Parse the input stream and return the vocabulary list
            } catch (IOException e) {
                throw new DatabaseException("Failed to load vocabulary from file", e);
            }
        }

        // If external file does not exist, load from classpath
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName); // Load file from resources folder
        if (is != null) { // Check if the resource was found
            return parseStream(is);
        }
        return new ArrayList<>(); // Return empty list if no file found
    }

private List<VocabularyEntry> parseStream(InputStream is) {
    List<VocabularyEntry> vocabulary = new ArrayList<>();
    String line;
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) { // Put is in try-with-resources to ensure it gets closed
        while ((line = reader.readLine()) != null) { // Read each line
            String[] parts = line.split(";");
            if (parts.length == 4) {
                String de = parts[0];
                String en = parts[1];
                String type = parts[2];
                int level = Integer.parseInt(parts[3].trim()); // Trim to avoid whitespace issues
                vocabulary.add(new VocabularyEntry(de, en, type, level)); // Create and add the entry
            }
        }
        return vocabulary; // Return the loaded vocabulary
    } catch (IOException e) {
        throw new DatabaseException("Failed to load vocabulary from file", e);
    }
}

//Method to add a new word to the vocabulary
public void addWord(VocabularyEntry entry, List<VocabularyEntry> vocabulary) {
    boolean duplicated = isDuplicate(entry.word(), vocabulary); // Check for duplicates
    if (duplicated) {
        throw new DuplicateWordException(entry.word());
    } else {
        vocabulary.add(entry); // Add the new entry to the vocabulary
        saveVocabulary(new ArrayList<>(vocabulary));
    }// Save the updated vocabulary
}

// Method to delete a word from the vocabulary
@Override
public boolean deleteWord(String word, List<VocabularyEntry> vocabulary) {
    boolean removed = vocabulary.removeIf(entry -> entry.word().equalsIgnoreCase(word)); // Remove entry if it matches the german word
    if (removed) {
        try {
            saveVocabulary(new ArrayList<>(vocabulary)); // Save the updated vocabulary
            System.out.println("✅ Word '" + word + "' successfully deleted.");
        } catch (Exception e) {
            throw new DatabaseException("Error saving the file after deletion", e);
        }
    }
    return removed;
}

// Method to search for a word in the vocabulary
@Override
public VocabularyEntry findByWord(String word, List<VocabularyEntry> vocabulary) {
    return vocabulary.stream()
            .filter(e -> e.word().equalsIgnoreCase(word))
            .findFirst()
            .orElseThrow(() -> new WordNotFoundException(word));
}

//Method to update a word in the vocabulary
@Override
public void updateWord(String word, VocabularyEntry updatedEntry, List<VocabularyEntry> vocabulary) {
    boolean found = false;
    for (int i = 0; i < vocabulary.size(); i++) {
        if (vocabulary.get(i).word().equalsIgnoreCase(word)) {
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
            throw new DatabaseException("Error saving the file after update", e);
        }
    } else {
        throw new WordNotFoundException(word);
    }
}

//Method to search for duplicates in the vocabulary
@Override
public boolean isDuplicate(String word, List<VocabularyEntry> currentList) { // Check for duplicates
    return currentList.stream()
            .anyMatch(entry -> entry.word().equalsIgnoreCase(word.trim()));// Check for duplicates ignoring case and whitespace
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
        throw new DatabaseException("Error clearing the vocabulary file", e);
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
