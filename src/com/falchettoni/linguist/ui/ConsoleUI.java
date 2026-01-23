package com.falchettoni.linguist.ui;

import com.falchettoni.linguist.model.VocabularyEntry;
import com.falchettoni.linguist.service.FileService;

import java.util.Scanner;

public class ConsoleUI {
    public void start() {
        // 'var' allows the compiler to infer the type, making the code cleaner.
        var scanner = new Scanner(System.in);

        // Initialize FileService with the desired file path
        var fileService = new FileService("vocabulary.csv");

        // This ArrayList will store our VocabularyEntry objects in memory.
        var myNotebook = fileService.loadVocabulary();

        System.out.println("--- WELCOME TO YOUR LANGUAGES NOTEBOOK (Java 25) ---");

        // Flag to control the main loop
        var flag = true;
        // The 'while(true)' loop keeps the program running until the user decides to exit.
        while (flag) { //
            System.out.println("\nOptions: Add word (1) - View list (2) - Update (3)");
            System.out.println("\nOptions: Delete (4) - Search (5) - Exit (6)");
            System.out.println("\nOptions: Clear all (7)");
            System.out.print("Select an option: ");
            var option = scanner.nextLine();
            // Parse String to Integer
            int optionInt;
            try {
                optionInt = Integer.parseInt(option);
            } catch (NumberFormatException e) {
                optionInt = -1; // Invalid option
            }

            switch (optionInt) {
                case 1 -> {//Data Acquisition
                    System.out.print("Enter the German word: ");
                    var de = scanner.nextLine();

                    System.out.print("Enter the English translation: ");
                    var en = scanner.nextLine();

                    System.out.print("Enter the word type (e.g., Noun, Verb): ");
                    var type = scanner.nextLine();

                    System.out.println("Enter the level of difficulty (1-5): ");
                    var lvl = scanner.nextLine();
                    int level;
                    try {
                        level = Integer.parseInt(lvl);
                        if (level < 1 || level > 5) {
                            System.out.println("Level must be between 1 and 5. Setting level to 1 by default.");
                            level = 1;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for level. Setting level to 1 by default.");
                        level = 1;
                    }
                    // Object Creation & Storage
                    // We instantiate the record and add it to our collection
                    if (fileService.isDuplicate(de, myNotebook)) {
                        System.out.println("This word already exists in your vocabulary list. Duplicate entries are not allowed.");
                        // Skip adding this word
                    } else {
                        myNotebook.add(new VocabularyEntry(de, en, type, level));
                        System.out.println("Word successfully saved to your list!");
                    }
                }
                case 2 -> {
                    //Data Retrieval
                    if (myNotebook.isEmpty()) {
                        System.out.println("Your vocabulary list is currently empty.");
                    } else {
                        System.out.println("\n--- CURRENT VOCABULARY LIST ---");
                        // Using a method reference to print each item in the list
                        myNotebook.forEach(System.out::println);
                    }
                    System.out.println("Number of words in your notebook: " + fileService.countTotalWords(myNotebook));
                    System.out.println("--- END OF LIST ---");
                }
                case 3 -> {
                    System.out.println("Enter the German word you want to update: ");
                    var germanWord = scanner.nextLine();// Input for the German word to update
                    var entry = fileService.searchWord(germanWord, myNotebook);// Search for the word in the vocabulary
                    if (entry != null) {// If the word is found
                        System.out.println("Current entry: " + entry);
                        System.out.print("Enter the new English translation (leave blank to keep current): ");
                        var newEnglish = scanner.nextLine(); // Input for new English translation
                        System.out.print("Enter the new word type (leave blank to keep current): ");
                        var newType = scanner.nextLine(); // Input for new word type
                        System.out.print("Enter the new level of difficulty (1-5, leave blank to keep current): ");
                        var newLevelInput = scanner.nextLine(); // Input for new level
                        int newLevel = entry.level(); // Default to current level
                        if (!newLevelInput.isBlank()) {
                            try {
                                newLevel = Integer.parseInt(newLevelInput);// Parse new level
                                if (newLevel < 1 || newLevel > 5) {
                                    System.out.println("Level must be between 1 and 5. Keeping current level.");
                                    newLevel = entry.level();// Revert to current level if out of range
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input for level. Keeping current level.");
                                newLevel = entry.level();// Revert to current level on parse error
                            }
                        }
                        // Create updated entry with new values or existing ones
                        entry = new VocabularyEntry(
                                germanWord,
                                newEnglish.isBlank() ? entry.english() : newEnglish,// Keep current if blank
                                newType.isBlank() ? entry.type() : newType,// Keep current if blank
                                newLevel
                        );
                        // Update the vocabulary list
                        fileService.updateWord(germanWord, entry, myNotebook);
                        System.out.println("Word successfully updated!");
                    } else { // If the word is not found
                        System.out.println("Word not found in your vocabulary list.");
                    }
                }
                case 4 -> {
                    System.out.println("Enter the German word you want to delete: ");
                    var germanWordToDelete = scanner.nextLine(); // Input for the German word to delete
                    fileService.deleteWord(germanWordToDelete, myNotebook); // Delete the word from the vocabulary
                }
                case 5 -> {
                    System.out.println("Search for word (1) - Search for level (2) - Search for type (3) - Exit search (4): ");
                    var searchOption = scanner.nextLine();
                    int searchOptionInt;
                    try {
                        searchOptionInt = Integer.parseInt(searchOption);
                    } catch (NumberFormatException e) {
                        searchOptionInt = -1; // Invalid option
                    }
                    switch (searchOptionInt) {
                        case 1 -> {
                            System.out.println("Enter the German word you want to search for: ");
                            var germanWordToSearch = scanner.nextLine(); // Input for the German word to search
                            var foundEntry = fileService.searchWord(germanWordToSearch, myNotebook); //
                            if (foundEntry != null) { // If the word is found
                                System.out.println("Found entry: " + foundEntry);
                            } else { // If the word is not found
                                System.out.println("Word not found in your vocabulary list.");
                            }
                        }
                        case 2 -> {
                            System.out.println("Enter the level (1-5) you want to search for: ");
                            var levelInput = scanner.nextLine(); // Input for the level to search
                            int levelToSearch;
                            try {
                                levelToSearch = Integer.parseInt(levelInput);
                                if (levelToSearch < 1 || levelToSearch > 5) {
                                    System.out.println("Level must be between 1 and 5.");
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input for level.");
                                break;
                            }
                            var resultsByLevel = fileService.getWordsByLevel(levelToSearch, myNotebook);
                            if (resultsByLevel.isEmpty()) {
                                System.out.println("No words found at level " + levelToSearch + ".");
                            } else {
                                System.out.println("Words at level " + levelToSearch + ":");
                                resultsByLevel.forEach(System.out::println);
                            }
                        }
                        case 3 -> {
                            System.out.println("Enter the word type you want to search for (e.g., Noun, Verb): ");
                            var typeToSearch = scanner.nextLine(); // Input for the type to search
                            var resultsByType = fileService.getWordsByType(typeToSearch, myNotebook);
                            if (resultsByType.isEmpty()) {
                                System.out.println("No words found of type '" + typeToSearch + "'.");
                            } else {
                                System.out.println("Words of type '" + typeToSearch + "':");
                                resultsByType.forEach(System.out::println);
                            }
                        }
                        case 4 -> System.out.println("Exiting search menu.");
                        default -> System.out.println("Invalid search option. Please try again.");
                    }
                }
                case 6 -> {
                    System.out.println("Exiting the program...");
                    fileService.saveVocabulary(myNotebook); // Save vocabulary before exiting
                    flag = false; // This will break the loop and end the program
                }
                case 7 -> {
                    System.out.println("Are you sure you want to clear all entries? Type 'YES' to confirm: ");
                    var confirmation = scanner.nextLine(); // Input for confirmation
                    if (confirmation.equalsIgnoreCase("YES")) {
                        fileService.clearVocabulary(); // Clear the in-memory list
                        System.out.println("All entries have been cleared from your vocabulary list.");
                        myNotebook = fileService.loadVocabulary(); // Save the cleared list to the file
                    } else {
                        System.out.println("Clear all operation cancelled.");
                    }
                }
                default ->
                    // Handling invalid inputs
                        System.out.println("Invalid option. Please try again.");

            }
        }

        // closing the scanner to prevent resource leaks
        System.out.println("Happy learning! Bis bald!");
        scanner.close();
    }
}
