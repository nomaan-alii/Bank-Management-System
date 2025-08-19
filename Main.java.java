import java.util.Scanner;
import java.util.Random; // For generating account numbers

// This class contains the main logic for the Bank Management System.
// All methods are static to avoid explicit OOP concepts for accounts.
public class Main {

    // --- Data Storage (using fixed-size arrays) ---
    // We define a maximum number of accounts the system can support.
    private static final int MAX_ACCOUNTS = 100; // Maximum number of accounts the bank can hold

    // These arrays act as parallel arrays to store account information.
    // Each index in these arrays corresponds to a single bank account.
    private static String[] accountHolderNames = new String[MAX_ACCOUNTS];
    private static int[] accountNumbers = new int[MAX_ACCOUNTS];
    private static double[] accountBalances = new double[MAX_ACCOUNTS];
    private static String[] accountPINs = new String[MAX_ACCOUNTS]; // Store PINs as strings

    // This variable keeps track of the current number of accounts created.
    // It also indicates the next available index for a new account.
    private static int accountCount = 0;

    // Scanner object for reading user input from the console
    private static Scanner scanner = new Scanner(System.in);
    // Random object for generating unique account numbers
    private static Random random = new Random();

    // --- Main method: Entry point of the application ---
    public static void main(String[] args) {
        System.out.println("----------------------------------------------");
        System.out.println("   Welcome to the Simple Bank Management System");
        System.out.println("----------------------------------------------");

        int choice;
        // Main loop to keep the application running until the user exits
        do {
            displayMenu(); // Show the main menu options
            System.out.print("Enter your choice: ");

            // Input validation for menu choice
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number from the menu.");
                scanner.next(); // Consume the invalid input
                System.out.print("Enter your choice: ");
            }
            choice = scanner.nextInt(); // Read user's choice
            scanner.nextLine(); // Consume newline left-over

            // Process user's choice using a switch statement
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    checkBalance();
                    break;
                case 5:
                    transferFunds();
                    break;
                case 6:
                    viewAllAccounts(); // This is typically for admin/demo purposes
                    break;
                case 7:
                    System.out.println("Thank you for using the Bank Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\n----------------------------------------------\n"); // Separator for better readability
        } while (choice != 7); // Continue loop until choice is 7 (Exit)

        scanner.close(); // Close the scanner to release resources
    }

    // --- Method to display the main menu options to the user ---
    private static void displayMenu() {
        System.out.println("1. Create New Account");
        System.out.println("2. Deposit Funds");
        System.out.println("3. Withdraw Funds");
        System.out.println("4. Check Balance");
        System.out.println("5. Transfer Funds");
        System.out.println("6. View All Accounts (Admin)");
        System.out.println("7. Exit");
    }

    // --- Method to create a new bank account ---
    private static void createAccount() {
        System.out.println("\n--- Create New Account ---");

        // Check if the maximum number of accounts has been reached
        if (accountCount >= MAX_ACCOUNTS) {
            System.out.println("Cannot create more accounts. Bank has reached its maximum capacity.");
            return;
        }

        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();

        double initialDeposit;
        while (true) {
            System.out.print("Enter Initial Deposit Amount: $");
            if (scanner.hasNextDouble()) {
                initialDeposit = scanner.nextDouble();
                if (initialDeposit >= 0) {
                    break;
                } else {
                    System.out.println("Initial deposit cannot be negative. Please enter a valid amount.");
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric value for the deposit.");
                scanner.next(); // Consume the invalid input
            }
        }
        scanner.nextLine(); // Consume the newline character left by nextDouble()

        String pin;
        while (true) {
            System.out.print("Set a 4-digit PIN for your account: ");
            pin = scanner.nextLine();
            if (pin.matches("\\d{4}")) { // Regex to check if it's exactly 4 digits
                break;
            } else {
                System.out.println("PIN must be exactly 4 digits. Please try again.");
            }
        }

        // Generate a unique 6-digit account number
        int newAccountNumber;
        boolean isUnique;
        do {
            newAccountNumber = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
            isUnique = true;
            // Check if the generated account number already exists
            for (int i = 0; i < accountCount; i++) {
                if (accountNumbers[i] == newAccountNumber) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique); // Continue generating until a unique number is found

        // Add the new account details to our parallel arrays at the current accountCount index
        accountHolderNames[accountCount] = name;
        accountNumbers[accountCount] = newAccountNumber;
        accountBalances[accountCount] = initialDeposit;
        accountPINs[accountCount] = pin;

        accountCount++; // Increment the count of active accounts

        System.out.println("Account created successfully!");
        System.out.println("Account Holder: " + name);
        System.out.println("Account Number: " + newAccountNumber);
        System.out.println("Initial Balance: $" + String.format("%.2f", initialDeposit));
        System.out.println("Please remember your account number for future transactions.");
    }

    // --- Method to deposit funds into an existing account ---
    private static void deposit() {
        System.out.println("\n--- Deposit Funds ---");
        System.out.print("Enter Account Number: ");

        // Input validation for account number
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a numeric account number.");
            scanner.next(); // Consume the invalid input
            System.out.print("Enter Account Number: ");
        }
        int accNum = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Find the index of the account number
        int index = findAccountIndex(accNum);

        if (index != -1) { // Account found
            double amount;
            while (true) {
                System.out.print("Enter Amount to Deposit: $");
                if (scanner.hasNextDouble()) {
                    amount = scanner.nextDouble();
                    if (amount > 0) {
                        break;
                    } else {
                        System.out.println("Deposit amount must be positive. Please enter a valid amount.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a numeric value for the amount.");
                    scanner.next(); // Consume invalid input
                }
            }
            scanner.nextLine(); // Consume newline

            // Update the balance at the found index in the array
            double currentBalance = accountBalances[index];
            accountBalances[index] = currentBalance + amount;
            System.out.println("Deposit successful!");
            System.out.println("New Balance: $" + String.format("%.2f", accountBalances[index]));
        } else {
            System.out.println("Account not found. Please check the account number.");
        }
    }

    // --- Method to withdraw funds from an existing account ---
    private static void withdraw() {
        System.out.println("\n--- Withdraw Funds ---");
        System.out.print("Enter Account Number: ");

        // Input validation for account number
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a numeric account number.");
            scanner.next(); // Consume the invalid input
            System.out.print("Enter Account Number: ");
        }
        int accNum = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        int index = findAccountIndex(accNum);

        if (index != -1) { // Account found
            System.out.print("Enter PIN: ");
            String pinAttempt = scanner.nextLine();

            if (pinAttempt.equals(accountPINs[index])) { // PIN matches
                double amount;
                while (true) {
                    System.out.print("Enter Amount to Withdraw: $");
                    if (scanner.hasNextDouble()) {
                        amount = scanner.nextDouble();
                        if (amount > 0) {
                            break;
                        } else {
                            System.out.println("Withdrawal amount must be positive. Please enter a valid amount.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a numeric value for the amount.");
                        scanner.next(); // Consume invalid input
                    }
                }
                scanner.nextLine(); // Consume newline

                double currentBalance = accountBalances[index];

                if (currentBalance >= amount) { // Check if sufficient balance
                    accountBalances[index] = currentBalance - amount;
                    System.out.println("Withdrawal successful!");
                    System.out.println("New Balance: $" + String.format("%.2f", accountBalances[index]));
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Incorrect PIN. Withdrawal failed.");
            }
        } else {
            System.out.println("Account not found. Please check the account number.");
        }
    }

    // --- Method to check the balance of an account ---
    private static void checkBalance() {
        System.out.println("\n--- Check Balance ---");
        System.out.print("Enter Account Number: ");

        // Input validation for account number
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a numeric account number.");
            scanner.next(); // Consume the invalid input
            System.out.print("Enter Account Number: ");
        }
        int accNum = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        int index = findAccountIndex(accNum);

        if (index != -1) { // Account found
            System.out.print("Enter PIN: ");
            String pinAttempt = scanner.nextLine();

            if (pinAttempt.equals(accountPINs[index])) { // PIN matches
                System.out.println("Account Holder: " + accountHolderNames[index]);
                System.out.println("Account Number: " + accountNumbers[index]);
                System.out.println("Current Balance: $" + String.format("%.2f", accountBalances[index]));
            } else {
                System.out.println("Incorrect PIN. Balance inquiry failed.");
            }
        } else {
            System.out.println("Account not found. Please check the account number.");
        }
    }

    // --- Method to transfer funds between two accounts ---
    private static void transferFunds() {
        System.out.println("\n--- Transfer Funds ---");

        System.out.print("Enter Your Account Number (Source): ");
        // Input validation for source account number
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a numeric account number.");
            scanner.next(); // Consume the invalid input
            System.out.print("Enter Your Account Number (Source): ");
        }
        int sourceAccNum = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        int sourceIndex = findAccountIndex(sourceAccNum);

        if (sourceIndex == -1) {
            System.out.println("Source account not found. Transfer failed.");
            return;
        }

        System.out.print("Enter Your PIN: ");
        String sourcePinAttempt = scanner.nextLine();

        if (!sourcePinAttempt.equals(accountPINs[sourceIndex])) {
            System.out.println("Incorrect PIN for source account. Transfer failed.");
            return;
        }

        System.out.print("Enter Destination Account Number: ");
        // Input validation for destination account number
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a numeric account number.");
            scanner.next(); // Consume the invalid input
            System.out.print("Enter Destination Account Number: ");
        }
        int destAccNum = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        int destIndex = findAccountIndex(destAccNum);

        if (destIndex == -1) {
            System.out.println("Destination account not found. Transfer failed.");
            return;
        }

        if (sourceIndex == destIndex) {
            System.out.println("Cannot transfer funds to the same account. Transfer failed.");
            return;
        }

        double amount;
        while (true) {
            System.out.print("Enter Amount to Transfer: $");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                if (amount > 0) {
                    break;
                } else {
                    System.out.println("Transfer amount must be positive. Please enter a valid amount.");
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric value for the amount.");
                scanner.next(); // Consume invalid input
            }
        }
        scanner.nextLine(); // Consume newline

        double sourceBalance = accountBalances[sourceIndex];

        if (sourceBalance >= amount) {
            // Deduct from source
            accountBalances[sourceIndex] = sourceBalance - amount;
            // Add to destination
            accountBalances[destIndex] = accountBalances[destIndex] + amount;

            System.out.println("Funds transferred successfully!");
            System.out.println("Your New Balance: $" + String.format("%.2f", accountBalances[sourceIndex]));
        } else {
            System.out.println("Insufficient balance in source account. Transfer failed.");
        }
    }

    // --- Method to view all accounts (for demonstration/admin purposes) ---
    private static void viewAllAccounts() {
        System.out.println("\n--- All Bank Accounts ---");
        if (accountCount == 0) { // Check if there are any accounts created
            System.out.println("No accounts created yet.");
            return;
        }

        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-20s %-15s %-15s %-10s\n", "Account Holder", "Account Number", "Balance", "PIN");
        System.out.println("------------------------------------------------------------------");

        // Iterate through all active accounts using their index up to accountCount
        for (int i = 0; i < accountCount; i++) {
            System.out.printf("%-20s %-15d %-15.2f %-10s\n",
                    accountHolderNames[i],
                    accountNumbers[i],
                    accountBalances[i],
                    accountPINs[i]); // Note: In a real system, PINs wouldn't be displayed!
        }
        System.out.println("------------------------------------------------------------------");
    }

    // --- Helper method to find the index of an account given its account number ---
    // Returns the index if found, otherwise returns -1.
    private static int findAccountIndex(int accountNumber) {
        // Iterate only through the active accounts
        for (int i = 0; i < accountCount; i++) {
            if (accountNumbers[i] == accountNumber) {
                return i; // Account found at this index
            }
        }
        return -1; // Account not found
    }
}
