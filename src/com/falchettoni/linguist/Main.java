package com.falchettoni.linguist;

import com.falchettoni.linguist.repository.VocabularyRepository;
import com.falchettoni.linguist.service.FileService;
import com.falchettoni.linguist.ui.ConsoleUI;

class Main { // Application entry point
    static void main() {// Main method
        VocabularyRepository repo = new FileService("vocabulary.csv"); // Initialize repository with file service
        var app = new ConsoleUI(repo); // Create an instance of ConsoleUI
        app.start(); // Start the console user interface
    }
}