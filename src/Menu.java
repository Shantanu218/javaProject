import java.util.Scanner;

/**
 * Base class for all CLI menus.  Provides:
 * 1) displayMenu()/handleOption(int) abstract methods  
 * 2) a run() loop that shows the menu, reads a choice, and dispatches  
 * 3) a protected Scanner for subclasses to reuse
 */
public abstract class Menu {
    private final Scanner scanner = new Scanner(System.in);

    /** Show the menu options (implemented by subclasses). */
    public abstract void displayMenu();

    /** Handle the user's choice (implemented by subclasses). */
    public abstract void handleOption(int option);

    /**
     * Continuously display the menu and process input until the user enters 0.
     */
    public void run() {
        int choice;
        do {
            displayMenu();
            String line = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                choice = -1;
            }
            handleOption(choice);
            // if handleOption(0) returned or exited, loop will end because choice == 0
        } while (choice != 0);
    }

    /**
     * Give subclasses access to the same Scanner instance.
     */
    protected Scanner getScanner() {
        return scanner;
    }
}
