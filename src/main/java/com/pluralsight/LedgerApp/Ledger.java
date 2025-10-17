package com.pluralsight.LedgerApp;


import java.util.Scanner;

/*
 * Application entry point and shared resources.
 * Keeps a single Scanner instance used across the app.
 */
class LedgarApp {
    // single shared Scanner used by Features and other classes
    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        // Simple demo runner to exercise a few features
        System.out.println("Ledger app demo (Features).");
        Features.userLogin();            // login demo
        Features.displayFullLedger();    // prints current ledger
        // You can call other Features.* methods here to demo them
        System.out.println("Demo complete. Exiting.");
        // close scanner on exit
        scan.close();
    }
}

public class Ledger {
}
