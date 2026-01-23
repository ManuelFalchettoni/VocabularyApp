import java.util.Scanner; // We need this tool to read your keyboard input//
import java.util.ArrayList;

/**
 * A 'Record' is a modern, concise way to model data in Java.
 * It automatically handles fields, getters, and the constructor.
 * This specific record stores our language learning data.
 */
record VocabularyEntry(String german, String english, String type, Integer level) {
    @Override
    public String toString() {
        // Formats the output: [NOUN] Das Haus -> The House
        return String.format("[%s] %s -> %s (Level: %d)", type.toUpperCase(), german, english, level);
    }
}

/* We no longer need 'public static void main(String[] args)'.*/
void main() {
    // 'var' allows the compiler to infer the type, making the code cleaner.
    var scanner = new Scanner(System.in);

    // This ArrayList will store our VocabularyEntry objects in memory.
    var myNotebook = new ArrayList<VocabularyEntry>();

    System.out.println("--- WELCOME TO YOUR LANGUAGES NOTEBOOK (Java 25) ---");

    var flag = true;
    // The 'while(true)' loop keeps the program running until the user decides to exit.
    while (flag) {
        System.out.println("\nOptions: Add word (1) - View list (2) - Exit (3)");
        System.out.print("Select an option: ");
        var option = scanner.nextLine();
        int optionInt;
        try {
            optionInt = Integer.parseInt(option);
        } catch (NumberFormatException e) {
            optionInt = -1; // Invalid option
        }
        // Check if the user wants to terminate the program

        switch (optionInt) {
            case 1 -> {// STEP 1: Data Acquisition
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
                // STEP 2: Object Creation & Storage
                // We instantiate the record and add it to our collection
                myNotebook.add(new VocabularyEntry(de, en, type, level));
                System.out.println("Word successfully saved to your list!");
            }
            case 2 -> {
                // STEP 3: Data Retrieval
                if (myNotebook.isEmpty()) {
                    System.out.println("Your notebook is currently empty.");
                } else {
                    System.out.println("\n--- CURRENT VOCABULARY LIST ---");
                    // Using a method reference to print each item in the list
                    myNotebook.forEach(System.out::println);
                }
            }
            case 3 -> {
                System.out.println("Exiting the program...");
                flag = false; // This will break the loop and end the program
            }
            default ->
                // Handling invalid inputs
                System.out.println("Invalid option. Please try again.");

        }
    }

    // Clean up: closing the scanner to prevent resource leaks
    System.out.println("Happy learning! Bis bald!");
    scanner.close();
}
