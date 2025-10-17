package com.pluralsight.LedgerApp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
 * Features exposes user-driven operations: searching, sorting, adding deposits/payments,
 * and a basic login. This file uses Transaction.readTransactions / printTransactions / saveTransactions.
 */
public class Features {
    private static final String FILE = "transactions.csv";

    /*
     * Custom search: prompts user for filters and prints matching transactions.
     */
    public static void customSearch() {
        List<Transaction> allTransactions = Transaction.readTransactions(FILE);
        boolean found = false;
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!found) {
            List<Transaction> results = new ArrayList<>();

            System.out.print("Enter start date (yyyy-MM-dd) or leave empty: ");
            String startIn = LedgarApp.scan.nextLine().trim();
            System.out.print("Enter end date (yyyy-MM-dd) or leave empty: ");
            String endIn = LedgarApp.scan.nextLine().trim();
            System.out.print("Enter description or leave empty: ");
            String descIn = LedgarApp.scan.nextLine().trim();
            System.out.print("Enter vendor or leave empty: ");
            String vendorIn = LedgarApp.scan.nextLine().trim();
            System.out.print("Enter amount or leave empty: ");
            String amtIn = LedgarApp.scan.nextLine().trim();

            LocalDate start = null, end = null;
            Double amount = null;
            try {
                if (!startIn.isEmpty()) start = LocalDate.parse(startIn, formatDate);
                if (!endIn.isEmpty()) end = LocalDate.parse(endIn, formatDate);
                if (!amtIn.isEmpty()) amount = Double.parseDouble(amtIn);
            } catch (Exception e) {
                System.out.println("Invalid date or amount format. Try again.");
                continue;
            }

            for (Transaction t : allTransactions) {
                if (start != null && t.getDate().isBefore(start)) continue;
                if (end != null && t.getDate().isAfter(end)) continue;
                if (!descIn.isEmpty() && !t.getDescription().toLowerCase().contains(descIn.toLowerCase()))
                    continue;
                if (!vendorIn.isEmpty() && !t.getVendor().toLowerCase().contains(vendorIn.toLowerCase()))
                    continue;
                if (amount != null && Double.compare(amount, t.getAmount()) != 0) continue;

                results.add(t);
            }

            if (results.isEmpty()) {
                System.out.println("No transactions found — try different filters.");
            } else {
                found = true;
                Transaction.printTransactions(results);
            }
        }
    }

    /*
     * Prompt for vendor and print transactions that match.
     */
    public static void sortByVendor() {
        List<Transaction> all = Transaction.readTransactions(FILE);

        // if previous Scanner action left a newline, this clears it
        // (safe here even if there isn't a leftover newline)
        // LedgarApp.scan.nextLine();

        boolean found = false;
        while (!found) {
            System.out.print("Enter a vendor to search: ");
            String vendor = LedgarApp.scan.nextLine().trim();
            List<Transaction> hits = new ArrayList<>();
            for (Transaction t : all) {
                if (t.getVendor().toLowerCase().contains(vendor.toLowerCase())) hits.add(t);
            }
            if (hits.isEmpty()) {
                System.out.println("No transactions found for vendor: " + vendor + ". Try again.");
            } else {
                found = true;
                Transaction.printTransactions(hits);
            }
        }
    }

    /*
     * Show previous calendar year transactions.
     */
    public static void sortPreviousYear() {
        List<Transaction> all = Transaction.readTransactions(FILE);
        List<Transaction> out = new ArrayList<>();
        int year = LocalDate.now().minusYears(1).getYear();
        for (Transaction t : all) if (t.getDate().getYear() == year) out.add(t);
        Transaction.printTransactions(out);
    }

    /*
     * Show previous calendar month transactions.
     */
    public static void sortPreviousMonth() {
        List<Transaction> all = Transaction.readTransactions(FILE);
        List<Transaction> out = new ArrayList<>();
        LocalDate prev = LocalDate.now().minusMonths(1);
        int month = prev.getMonthValue();
        int year = prev.getYear();
        for (Transaction t : all) if (t.getDate().getMonthValue() == month && t.getDate().getYear() == year) out.add(t);
        Transaction.printTransactions(out);
    }

    /**
     * Year to date (current year up to today).
     */
    public static void sortYearToDate() {
        List<Transaction> all = Transaction.readTransactions(FILE);
        List<Transaction> out = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        for (Transaction t : all) if (t.getDate().getYear() == year && !t.getDate().isAfter(today)) out.add(t);
        Transaction.printTransactions(out);
    }

    /*
     * Month to date (current month up to today).
     */
    public static void sortMonthToDate() {
        List<Transaction> all = Transaction.readTransactions(FILE);
        List<Transaction> out = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();
        for (Transaction t : all) if (t.getDate().getMonthValue() == month && t.getDate().getYear() == year && !t.getDate().isAfter(today)) out.add(t);
        Transaction.printTransactions(out);
    }

    /*
     * Print only payments (negative amounts), newest first.
     */
    public static void sortByPayments() {
        List<Transaction> all = Transaction.readTransactions(FILE);
        List<Transaction> payments = new ArrayList<>();
        for (Transaction t : all) if (t.getAmount() < 0) payments.add(t);

        payments.sort(Comparator.comparing(Transaction::getDate)
                .thenComparing(Transaction::getTime)
                .reversed());

        Transaction.printTransactions(payments);
    }

    /**
     * Print only deposits (positive amounts).
     */
    public static void sortByDeposits() {
        List<Transaction> all = Transaction.readTransactions(FILE);
        List<Transaction> deposits = new ArrayList<>();
        for (Transaction t : all) if (t.getAmount() > 0) deposits.add(t);
        Transaction.printTransactions(deposits);
    }

    /*
     * Add a new payment (appends to CSV).
     * Presents prompts and does simple validation.
     */
    public static void userPayment() {
        System.out.println("Make a payment (debit).");

        System.out.print("Enter description: ");
        String desc = LedgarApp.scan.nextLine().trim();
        while (desc.isEmpty()) {
            System.out.print("Description required — try again: ");
            desc = LedgarApp.scan.nextLine().trim();
        }

        System.out.print("Enter vendor: ");
        String vendor = LedgarApp.scan.nextLine().trim();
        while (vendor.isEmpty()) {
            System.out.print("Vendor required — try again: ");
            vendor = LedgarApp.scan.nextLine().trim();
        }

        System.out.print("Enter amount (positive number): ");
        double amt;
        try {
            amt = Double.parseDouble(LedgarApp.scan.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid number. Aborting payment.");
            return;
        }
        if (amt <= 0) {
            System.out.println("Amount must be > 0. Aborting.");
            return;
        }

        // store as negative to indicate payment
        amt = -Math.abs(amt);

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Append formatted row to CSV
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            DateTimeFormatter dateF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeF = DateTimeFormatter.ofPattern("HH:mm:ss");
            String line = String.format("\n%-12s| %-11s| %-50s| %-22s|%10.2f",
                    date.format(dateF), time.format(timeF), desc, vendor, amt);
            bw.write(line);
            System.out.println("Payment saved.");
        } catch (IOException e) {
            System.out.println("Error saving payment: " + e.getMessage());
        }
    }

    /**
     * Add a deposit and persist all transactions (appends via read+save approach).
     */
    public static void userDeposit() {
        System.out.println("Add a deposit (credit).");

        System.out.print("Enter description: ");
        String desc = LedgarApp.scan.nextLine().trim();
        while (desc.isEmpty()) {
            System.out.print("Description required — try again: ");
            desc = LedgarApp.scan.nextLine().trim();
        }

        System.out.print("Enter vendor: ");
        String vendor = LedgarApp.scan.nextLine().trim();
        while (vendor.isEmpty()) {
            System.out.print("Vendor required — try again: ");
            vendor = LedgarApp.scan.nextLine().trim();
        }

        System.out.print("Enter deposit amount (>= 0): ");
        double amt;
        try {
            amt = Double.parseDouble(LedgarApp.scan.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid number. Aborting deposit.");
            return;
        }
        if (amt < 0) {
            System.out.println("Amount must be >= 0. Aborting.");
            return;
        }

        Transaction deposit = new Transaction(LocalDate.now(), LocalTime.now(), desc, vendor, amt);
        List<Transaction> all = Transaction.readTransactions(FILE);
        all.add(deposit);
        Transaction.saveTransactions(all, FILE);
        System.out.println("Deposit saved.");
    }

    /**
     * Display the full ledger using Transaction helper.
     */
    public static void displayFullLedger() {
        List<Transaction> all = Transaction.readTransactions(FILE);
        Transaction.printTransactions(all);
    }

    /**
     * Basic demo login (3 attempts each).
     */
    public static void userLogin() {
        String validUser = "Jefre123";
        String validPass = "JDPass";
        System.out.println("Please log in to your account.");

        // Username attempts
        for (int attempts = 3; attempts > 0; attempts--) {
            System.out.print("Username: ");
            String user = LedgarApp.scan.nextLine().trim();
            if (user.equalsIgnoreCase(validUser)) break;
            if (attempts == 1) {
                System.out.println("You shall not pass!");
                System.exit(0);
            }
            System.out.println("Incorrect username — attempts left: " + (attempts - 1));
        }

        // Password attempts
        for (int attempts = 3; attempts > 0; attempts--) {
            System.out.print("Password: ");
            String pass = LedgarApp.scan.nextLine().trim();
            if (pass.equals(validPass)) break;
            if (attempts == 1) {
                System.out.println(!Boolean.parseBoolean("You aint Valid"));
                System.exit(0);
            }
            System.out.println("Incorrect password — attempts left: " + (attempts - 1));
        }

        System.out.println("What up Jefre!")
    }
}
