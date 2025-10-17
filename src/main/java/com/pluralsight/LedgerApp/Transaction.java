package com.pluralsight.LedgerApp;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Lightweight Transaction model and helpers for reading/writing the CSV.
 * CSV format (human-friendly): Date | Time | Description | Vendor | Amount
 */
public class Transaction {
    private final LocalDate date;
    private final LocalTime time;
    private final String description;
    private final String vendor;
    private final double amount;

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // getters
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getDescription() { return description; }
    public String getVendor() { return vendor; }
    public double getAmount() { return amount; }

    // Read transactions from file (skips header or malformed lines)
    public static List<Transaction> readTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        DateTimeFormatter dateF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeF = DateTimeFormatter.ofPattern("HH:mm:ss");

        File f = new File(filePath);
        if (!f.exists()) {
            // no file yet — return empty list
            return transactions;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                // ignore empty lines and header lines
                if (line.trim().isEmpty() || line.trim().toLowerCase().startsWith("date")) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 5) continue; // skip malformed

                try {
                    LocalDate date = LocalDate.parse(parts[0].trim(), dateF);
                    LocalTime time = LocalTime.parse(parts[1].trim(), timeF);
                    String description = parts[2].trim();
                    String vendor = parts[3].trim();
                    double amount = Double.parseDouble(parts[4].trim());
                    transactions.add(new Transaction(date, time, description, vendor, amount));
                } catch (Exception ex) {
                    // skip malformed row silently (could log)
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions file: " + e.getMessage());
        }

        return transactions;
    }

    // Pretty-print a list of transactions sorted reverse-chronological
    public static void printTransactions(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("\n(No transactions to show)\n");
            return;
        }

        transactions.sort(Comparator.comparing(Transaction::getDate)
                .thenComparing(Transaction::getTime)
                .reversed());

        DateTimeFormatter dateF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeF = DateTimeFormatter.ofPattern("HH:mm:ss");

        String header = "\nDate\t\t| Time\t\t | Description\t\t\t\t\t   | Vendor\t\t\t   |   Amount\n";
        System.out.print(header);
        for (Transaction t : transactions) {
            String d = t.getDate().format(dateF);
            String ti = t.getTime().format(timeF);
            System.out.printf("%-12s| %-11s| %-60s| %-22s|%10.2f\n",
                    d, ti, t.getDescription(), t.getVendor(), t.getAmount());
        }
    }

    // Save all transactions to file (overwrites). Writes a header row.
    public static void saveTransactions(List<Transaction> allTransactions, String filePath) {
        DateTimeFormatter dateF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeF = DateTimeFormatter.ofPattern("HH:mm:ss");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            String header = "Date\t\t| Time\t\t | Description\t\t\t\t\t   | Vendor\t\t\t   |   Amount\n";
            bw.write(header);
            for (Transaction t : allTransactions) {
                String line = String.format("%-12s| %-11s| %-60s| %-22s|%10.2f\n",
                        t.getDate().format(dateF),
                        t.getTime().format(timeF),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                bw.write(line);
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions to file: " + e.getMessage());
        }
    }
}


