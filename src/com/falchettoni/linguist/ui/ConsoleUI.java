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

        var flag = true;
        // The 'while(true)' loop keeps the program running until the user decides to exit.
        while (flag) {
            System.out.println("\nOptions: Add word (1) - View list (2) - Exit (3)");
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
                    myNotebook.add(new VocabularyEntry(de, en, type, level));
                    System.out.println("Word successfully saved to your list!");
                }
                case 2 -> {
                    //Data Retrieval
                    if (myNotebook.isEmpty()){
                        System.out.println("Your vocabulary list is currently empty.");
                    } else {
                        System.out.println("\n--- CURRENT VOCABULARY LIST ---");
                        // Using a method reference to print each item in the list
                        myNotebook.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    System.out.println("Exiting the program...");
                    fileService.saveVocabulary(myNotebook); // Save vocabulary before exiting
                    flag = false; // This will break the loop and end the program
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
