package com.pluralsight;

import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n==== Ledger Application Menu ====");
            System.out.println("1. Payment");
            System.out.println("2. Ledger");
            System.out.println("3. Deposit");
            System.out.println("4. Transaction");
            System.out.println("5. Main / Exit");
            System.out.print("Choose an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear input buffer

            switch (choice) {
                case 1:
                    handlePayment();
                    break;
                case 2:
                    handleLedger();
                    break;
                case 3:
                    handleDeposit();
                    break;
                case 4:
                    handleTransaction();
                    break;
                case 5:
                    System.out.println("Returning to main or exiting program...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select between 1–5.");
            }
        }

        System.out.println("Program ended. Goodbye!");
    }

    // --- Example Handlers ---
    private static void handlePayment() {
        System.out.println("You selected: Payment");
        // Add payment logic here
    }

    private static void handleLedger() {
        System.out.println("You selected: Ledger");
        // Add ledger display or management logic here
    }

    private static void handleDeposit() {
        System.out.println("You selected: Deposit");
        // Add deposit logic here
    }

    private static void handleTransaction() {
        System.out.println("You selected: Transaction");
        // Add transaction logic here
    }
}
