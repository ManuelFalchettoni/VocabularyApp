Linguist Notebook
A robust Java-based vocabulary management system designed for language learners. This project focuses on Clean Architecture, Object-Oriented Programming (OOP), and Custom Exception Handling.

Key Features
-- Vocabulary Management: Add, list, and filter vocabulary entries.

Data Persistence: Automatic saving and loading using CSV format.

Advanced Filtering: Search for words by Type (Noun, Verb, etc.) and Difficulty Level (1-5).

Robust Error Handling: Custom exception hierarchy to manage duplicate entries and file I/O issues.

Clean CLI: Interactive console interface with input validation.

Architecture & Design
The project follows a Package-by-Layer organization to ensure separation of concerns:

model: Contains the VocabularyEntry record.

repository: Defines the data access contracts (Interfaces).

service: Implements the business logic and file persistence.

ui: Handles user interaction.

exceptions: Custom hierarchy for domain-specific error handling.

Custom Exception Hierarchy
I implemented a robust error-handling system to separate technical failures from business rule violations:

VocabularyException (Base class)

DatabaseException (File I/O failures)

DuplicateWordException (Preventing redundant entries)
