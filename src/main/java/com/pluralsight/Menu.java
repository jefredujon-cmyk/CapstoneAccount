package com.pluralsight;

import java.util.Scanner;


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

    // --- Option Handlers with Input ---
    private static void handlePayment() {
        System.out.println("\n--- Payment Section ---");
        System.out.print("Enter payment amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // clear buffer

        System.out.print("Enter payment description: ");
        String description = scanner.nextLine();

        System.out.println("✅ Payment of $" + amount + " for \"" + description + "\" has been processed successfully!");
    }

    private static void handleLedger() {
        System.out.println("\n--- Ledger Section ---");
        System.out.println("Here you would normally view your past deposits, payments, and transactions.");
        System.out.println("📘 (Feature under development: will display saved ledger entries)");
    }

    private static void handleDeposit() {
        System.out.println("\n--- Deposit Section ---");
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter deposit source or note: ");
        String source = scanner.nextLine();

        System.out.println("💰 Deposit of $" + amount + " from \"" + source + "\" recorded successfully!");
    }

    private static void handleTransaction() {
        System.out.println("\n--- Transaction Section ---");
        System.out.print("Enter transaction type (payment/deposit): ");
        String type = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter transaction note: ");
        String note = scanner.nextLine();

        System.out.println("📄 Transaction recorded: " + type.toUpperCase() +
                " of $" + amount + " | Note: \"" + note + "\"");
    }
}
