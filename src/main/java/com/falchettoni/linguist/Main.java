package com.falchettoni.linguist;

import com.falchettoni.linguist.repository.VocabularyRepository;
import com.falchettoni.linguist.service.FileService;
import com.falchettoni.linguist.ui.ConsoleUI;

public class Main { // Application entry point
    public static void main(String[] args) {// Main method
        VocabularyRepository repo = new FileService("resources/vocabulary.csv"); // Initialize repository with file service
        var app = new ConsoleUI(repo); // Create an instance of ConsoleUI
        app.start(); // Start the console user interface
    }
}