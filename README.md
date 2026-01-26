# Linguist Notebook

A structured Java application designed for systematic vocabulary management and linguistic data persistence.

## Project Overview
* **Purpose**
  * Centralized storage for multilingual vocabulary.
  * Classification of entries by grammatical category and proficiency levels.
  * Persistence management via local file systems.
* **Technical Foundation**
  * Language: Java 17.
  * Architecture: Layered Package Pattern (N-Tier simplified).
  * Data Format: Semicolon-separated values (CSV).

## Key Functionalities
* **Vocabulary Management**
  * Entry Creation: Addition of new words with English/German mappings.
  * Duplicate Prevention: Logic-based validation to ensure data integrity.
  * Global Reset: Functionality to clear the entire local database safely.
* **Search and Filtering**
  * Categorical Filtering: Retrieval of entries based on word type (Nouns, Verbs, etc.).
  * Difficulty Grading: Filtering by proficiency levels (1 to 5).
* **System Resilience**
  * Custom Exception Hierarchy: Domain-specific error handling.
  * Data Safety: Automatic file synchronization after every modification.

## System Architecture
* **Logic Layers**
  * **Model Layer**
    * Defines the `VocabularyEntry` record as the primary data carrier.
  * **Service / Repository Layer**
    * Manages file I/O operations and business rules.
    * Implements the `VocabularyRepository` interface for decoupled logic.
  * **UI Layer**
    * Console-based interface managing user input and data display.
* **Exception Hierarchy**
  * `VocabularyException` (Root)
    * `DatabaseException`: Handles I/O failures (Save/Load/Clear).
    * `DuplicateWordException`: Manages business rule violations regarding data redundancy.

## Usage Instructions
* **Prerequisites**
  * Java Development Kit (JDK) 17 or higher.
* **Execution**
  * Compilation:
    * `javac -d out -sourcepath src src/com/falchettoni/linguist/Main.java`
  * Running the Application:
    * `java -cp out com.falchettoni.linguist.Main`

## Development Roadmap
* **Short-term Improvements**
  * Integration of JUnit 5 for automated regression testing.
  * Implementation of a "Review Mode" for active recall practice.
* **Long-term Goals**
  * Migration from flat-file storage to an embedded SQL database (SQLite).
  * Development of a Graphical User Interface (GUI).
