package com.falchettoni.linguist.ui;

import com.falchettoni.linguist.exceptions.DatabaseException;
import com.falchettoni.linguist.exceptions.DuplicateWordException;
import com.falchettoni.linguist.exceptions.WordNotFoundException;
import com.falchettoni.linguist.model.VocabularyEntry;
import com.falchettoni.linguist.repository.VocabularyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner; // Scanner for user input

    private final VocabularyRepository repository; // Vocabulary repository interface
    private List<VocabularyEntry> myNotebook; // Load existing vocabulary entries

    public ConsoleUI(VocabularyRepository repository) {
        this.scanner = new Scanner(System.in);
        this.repository = repository;

        try {
            this.myNotebook = repository.loadVocabulary(); // Load existing vocabulary entries
        } catch (Exception e) {
            System.out.println("Error loading vocabulary. Starting with an empty notebook.");
            this.myNotebook = new java.util.ArrayList<>(); // Initialize an empty list on error
        }
    }

    public void start() {

        System.out.println("--- WELCOME TO YOUR LANGUAGES NOTEBOOK (Java 25) ---");

        // The 'while(true)' loop keeps the program running until the user decides to exit.
        while (true) { //
            System.out.println("\nOptions: Add word (1) - View list (2) - Update (3)");
            System.out.println("\nDelete (4) - Search (5) - Exit (6)");
            System.out.println("\nClear all (7)");
            System.out.print("Select an option: ");
            var option = parseStringInput(6); // Default option is 6 (Exit)

            switch (option) {
                case 1 -> handleAddWord();//Data Acquisition
                case 2 -> handleViewList(); //Data Retrieval
                case 3 -> handleUpdateWord(); //Data Update
                case 4 -> handleDeleteWord(); //Data Deletion
                case 5 -> handleSearchList(); // Search Submenu
                case 6 -> {
                    handleExit();
                    return;
                } // Exit the program
                case 7 -> handleClearAll(); // Clear all entries
                default -> System.out.println("Invalid option. Please try again."); // Handling invalid inputs
            }
        }
    }

    //Case 1 add word
    private void handleAddWord() {
        System.out.print("Enter the German word: ");
        var word = scanner.nextLine();

        System.out.print("Enter the English translation: ");
        var en = scanner.nextLine();

        System.out.print("Enter the word type (e.g., Noun, Verb): ");
        var type = scanner.nextLine();

        System.out.println("Enter the level of difficulty (1-5): ");
        int level = parseLevelInput(1); // Default level is 1
        // Object Creation & Storage
        // We instantiate the record and add it to our collection
        var newEntry = new VocabularyEntry(word, en, type, level);
        try {
            repository.addWord(newEntry, myNotebook);
            System.out.println("Word successfully added to your vocabulary list!");
        } catch (DuplicateWordException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("An error occurred while saving the word. " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Technical cause: " + e.getCause().getMessage());
            }
        }
    }

    //Case 2 view list
    private void handleViewList() {
        //Data Retrieval
        if (myNotebook.isEmpty()) {
            System.out.println("Your vocabulary list is currently empty.");
        } else {
            System.out.println("\n--- CURRENT VOCABULARY LIST ---");
            // Using a method reference to print each item in the list
            myNotebook.forEach(System.out::println);
        }
        System.out.println("Number of words in your notebook: " + repository.countTotal(myNotebook));
        System.out.println("--- END OF LIST ---");
    }

    //Case 3 update word
    private void handleUpdateWord() {
        System.out.println("Enter the German word you want to update: ");
        var germanWord = scanner.nextLine();// Input for the German word to update
        try {
            var entry = repository.findByWord(germanWord, myNotebook);// Search for the word in the vocabulary

            System.out.println("Current entry: " + entry); // Display current entry
            // Prompt for new values
            System.out.print("Enter the new English translation (leave blank to keep current): ");
            var newEnglish = scanner.nextLine(); // Input for new English translation

            System.out.print("Enter the new word type (leave blank to keep current): ");
            var newType = scanner.nextLine(); // Input for new word type

            System.out.print("Enter the new level of difficulty (1-5, leave blank to keep current): ");
            var newLevelInput = parseLevelInput(entry.level()); // Input for new level

            // Create updated entry with new values or existing ones
            entry = new VocabularyEntry(
                    germanWord,
                    newEnglish.isBlank() ? entry.english() : newEnglish,// Keep current if blank
                    newType.isBlank() ? entry.type() : newType,// Keep current if blank
                    newLevelInput // Updated level
            );
            // Update the vocabulary list
            repository.updateWord(germanWord, entry, myNotebook);
            System.out.println("Word successfully updated!");
        } catch (WordNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    //Case 4 delete word
    private void handleDeleteWord() {
        System.out.println("Enter the German word you want to delete: ");
        var germanWordToDelete = scanner.nextLine(); // Input for the German word to delete
        boolean deleted = repository.deleteWord(germanWordToDelete, myNotebook); // Attempt to delete the word
        if (!deleted) { // If the word was not found and thus not deleted
            System.out.println("Word not found in your vocabulary list. No deletion performed.");
        }
    }

    //Case 5 - Search Submenu Handlers
    private void handleSearchWord() {
        System.out.println("Enter the German word you want to search for: ");
        var germanWordToSearch = scanner.nextLine(); // Input for the German word to search
        var foundEntry = repository.findByWord(germanWordToSearch, myNotebook); //
        if (foundEntry != null) { // If the word is found
            System.out.println("Found entry: " + foundEntry);
        } else { // If the word is not found
            System.out.println("Word not found in your vocabulary list.");
        }
    }

    private void handleViewByLevel() {
        System.out.println("Enter the level (1-5) you want to view: ");
        int levelToView = parseIntegerInput(1); // Default level is 1
        var resultsByLevel = repository.findByLevel(levelToView, myNotebook);
        if (resultsByLevel.isEmpty()) {
            System.out.println("No words found at level " + levelToView + ".");
        } else {
            System.out.println("Words at level " + levelToView + ":");
            resultsByLevel.forEach(System.out::println);
            System.out.println(repository.countByLevel(levelToView, myNotebook));
        }
    }

    private void handleViewByType() {
        System.out.println("Enter the word type you want to view (e.g., Noun, Verb): ");
        var typeToView = scanner.nextLine(); // Input for the type to view
        var resultsByType = repository.findByType(typeToView, myNotebook);
        if (resultsByType.isEmpty()) {
            System.out.println("No words found of type '" + typeToView + "'.");
        } else {
            System.out.println("Words of type '" + typeToView + "':");
            resultsByType.forEach(System.out::println);
            System.out.println(repository.countByType(typeToView, myNotebook));
        }
    }

    private void handleSearchList() {
        System.out.println("Search for word (1) - Search for level (2) - Search for type (3) - Exit search (4): ");
        var searchOption = parseIntegerInput(1); // Default search option is 1
        switch (searchOption) {
            case 1 -> handleSearchWord();
            case 2 -> handleViewByLevel();
            case 3 -> handleViewByType();
            case 4 -> System.out.println("Exiting search menu.");
            default -> System.out.println("Invalid search option. Please try again.");
        }
    }

    //Case 6 exit
    private void handleExit() {
        System.out.println("Exiting the program...");
        repository.saveVocabulary(new ArrayList<>(myNotebook));// Save vocabulary before exiting
        System.out.println("Happy learning! Bis bald!");
        // closing the scanner to prevent resource leaks
        scanner.close();
    }

    //Case 7 clear all
    private void handleClearAll() {
        repository.clearVocabulary(); // Clear the vocabulary list
        myNotebook.clear(); // Clear the in-memory list
        System.out.println("All entries have been cleared from your vocabulary list.");
    }

    //Helper methods for input parsing
    private int parseLevelInput(int currentLevel) {
        String input = scanner.nextLine();// Read user input
        if (input.isBlank()) return currentLevel; // Keep current level if input is blank

        try {
            int level = Integer.parseInt(input);
            return (level >= 1 && level <= 5) ? level : currentLevel; // Validate level range
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Keeping current level.");
            return currentLevel; // Keep current level on parse error
        }
    }

    private int parseIntegerInput(int currentValue) {
        String input = scanner.nextLine();// Read user input
        if (input.isBlank()) return currentValue; // Keep current value if input is blank

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Keeping current value.");
            return currentValue; // Keep current value on parse error
        }
    }

    private int parseStringInput(int currentValue) {
        String input = scanner.nextLine();// Read user input
        if (input.isBlank()) return currentValue; // Keep current value if input is blank

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Keeping current value.");
            return currentValue; // Keep current value on parse error
        }
    }
}
