import java.util.Scanner; // We need this tool to read your keyboard input//
import java.util.ArrayList;
/**
 * A 'Record' is a modern, concise way to model data in Java.
 * It automatically handles fields, getters, and the constructor.
 * This specific record stores our language learning data.
 */
record VocabularyEntry(String german, String english, String type) {
    @Override
    public String toString() {
        // Formats the output: [NOUN] Das Haus -> The House
        return String.format("[%s] %s -> %s", type.toUpperCase(), german, english);
    }
}
/* We no longer need 'public static void main(String[] args)'.*/
void main() {
    // 'var' allows the compiler to infer the type, making the code cleaner.
    var scanner = new Scanner(System.in);

    // This ArrayList will store our VocabularyEntry objects in memory.
    var myNotebook = new ArrayList<VocabularyEntry>();

    System.out.println("--- WELCOME TO YOUR LANGUAGES NOTEBOOK (Java 25) ---");

    // The 'while(true)' loop keeps the program running until the user decides to exit.
    while (true) {
        System.out.println("\nOptions: (1) Add word (2) View list (3) Exit");
        System.out.print("Select an option: ");
        var option = scanner.nextLine();

        // Check if the user wants to terminate the program
        if (option.equals("3")) {
            System.out.println("Exiting the program...");
            break;
        }

        if (option.equals("1")) {

            // STEP 1: Data Acquisition
            System.out.print("Enter the German word: ");
            var de = scanner.nextLine();

            System.out.print("Enter the English translation: ");
            var en = scanner.nextLine();

            System.out.print("Enter the word type (e.g., Noun, Verb): ");
            var type = scanner.nextLine();

            // STEP 2: Object Creation & Storage
            // We instantiate the record and add it to our collection
            myNotebook.add(new VocabularyEntry(de, en, type));
            System.out.println("Word successfully saved to your list!");

        } else if (option.equals("2")) {
            // STEP 3: Data Retrieval
            if (myNotebook.isEmpty()) {
                System.out.println("Your notebook is currently empty.");
            } else {
                System.out.println("\n--- CURRENT VOCABULARY LIST ---");
                // Using a method reference to print each item in the list
                myNotebook.forEach(System.out::println);
            }
        } else {
            // Handling invalid inputs
            System.out.println("Invalid option. Please try again.");
        }
    }

    // Clean up: closing the scanner to prevent resource leaks
    System.out.println("Happy learning! Bis bald!");
    scanner.close();
}
