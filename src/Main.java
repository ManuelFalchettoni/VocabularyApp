import java.util.Scanner; // We need this tool to read your keyboard input

public class Main {
    public static void main(String[] args) {
        // 1. Initialize the Scanner
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- GERMAN VOCABULARY MANAGER v1.0 ---");
        System.out.println("Ready to add a new word?\n");

        // 2. Capture German Word
        System.out.print("Enter German word (e.g., 'Das Haus'): ");
        String germanWord = scanner.nextLine();

        // 3. Capture English Translation (C1 Practice!)
        System.out.print("Enter English translation: ");
        String englishWord = scanner.nextLine();

        // 4. Capture Word Type
        System.out.print("Word type (Noun, Verb, Adjective): ");
        String wordType = scanner.nextLine();

        //5. Capture Level of Difficulty
        System.out.print("Level of Difficulty (1 to 5): ");
        Integer levelDifficulty = scanner.nextInt();

        // 5. Display the result in a clean format
        System.out.println("\n-------------------------------------");
        System.out.println("WORD SAVED SUCCESSFULLY!");
        System.out.println("Category: [" + wordType.toUpperCase() + "]");
        System.out.println("German: " + germanWord);
        System.out.println("English: " + englishWord);
        System.out.println("Level: " + levelDifficulty);
        System.out.println("-------------------------------------");

        // Close the scanner to be efficient with resources
        scanner.close();
    }
}