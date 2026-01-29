package com.falchettoni.linguist.service;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5 assertions

import com.falchettoni.linguist.model.VocabularyEntry;
import org.junit.jupiter.api.Test; // JUnit 5 imports
import java.io.ByteArrayInputStream; // To simulate InputStream
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

class FileServiceTest {

    private final FileService fileService = new FileService("test_vocab.csv"); // Instance of FileService for testing

    @Test // @Test annotation indicates a test method
    void shouldParseValidLineCorrectly() {
        // 1. GIVEN (Simulate the input CSV data)
        String csvData = "Hund;Dog;Noun;1\n"; // Simulated CSV line
        InputStream is = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8)); // Convert string to InputStream

        // 2. WHEN (When we call the method to probe)
        List<VocabularyEntry> result = fileService.parseStream(is);

        // 3. THEN (Then we verify the results)
        assertFalse(result.isEmpty(), "The result should not be empty");
        assertEquals("Hund", result.getFirst().word());
        assertEquals("Dog", result.getFirst().translation());
        assertEquals("Noun", result.getFirst().type());
        assertEquals(1, result.getFirst().level());
    }

    @Test
    void shouldIgnoreInvalidLines(){
        // GIVEN: Line with missing fields
        String csvData = "Katze;Cat;Noun"; // Missing level field
        InputStream is = new ByteArrayInputStream(csvData.getBytes()); // Convert string to InputStream

        // WHEN: parseStream is called
        List<VocabularyEntry> result = fileService.parseStream(is);

        // THEN: Result should be empty as the line is invalid
        assertTrue(result.isEmpty(), "The result should be empty for invalid lines");
    }

    @Test
    void ShouldParseCorrectlyEvenWithWhiteSpaces(){
        // GIVEN: Line with extra spaces
        String csvData = " Vogel ; Bird ; Noun ; 2 \n"; // Line with extra spaces
        InputStream is = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8)); // Convert string to InputStream

        // WHEN: ParseStream is called
        List<VocabularyEntry> result = fileService.parseStream(is);

        //THEN: Result should contain the correctly parsed entry
        assertFalse(result.isEmpty(), "The result should not be empty");
        assertEquals("Vogel", result.getFirst().word());
        assertEquals("Bird", result.getFirst().translation());
        assertEquals("Noun", result.getFirst().type());
        assertEquals(2, result.getFirst().level());
    }
}