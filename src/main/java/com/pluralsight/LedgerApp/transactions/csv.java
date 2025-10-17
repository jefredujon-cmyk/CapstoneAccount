package com.pluralsight.LedgerApp.transactions;


import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class TransactionManager {

        private static final String FILE_NAME = "transactions.csv";

        // Method to record a transaction
        public static void recordTransaction(String username, String type, double amount) {
            try {
                // Count existing lines to set transaction ID
                int id = countLines(FILE_NAME);
                FileWriter fw = new FileWriter(FILE_NAME, true); // true = append mode
                BufferedWriter bw = new BufferedWriter(fw);

                // Write new transaction
                bw.write(id + "," + username + "," + type + "," + amount + "," + LocalDate.now());
                bw.newLine();

                bw.close();
                System.out.println("✅ Transaction recorded successfully!");
            } catch (IOException e) {
                System.out.println("Error writing transaction: " + e.getMessage());
            }
        }

        // Helper method to count lines for unique IDs
        private static int countLines(String fileName) {
            int lines = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                while (reader.readLine() != null) lines++;
            } catch (IOException e) {
                // ignore, just means file might not exist yet
            }
            return lines; // line count (including header)
        }

        // Example usage
        public static <Scanner> void main(String[] args) {
            var input = new Scanner();

            System.out.print("Enter your username: ");
            String username = input.toString();

            System.out.print("Enter transaction type (Deposit or Withdrawal): ");
            String type = input.toString();

            System.out.print("Enter amount: ");
            double amount = ((java.util.Scanner) input).nextDouble();

            recordTransaction(username, type, amount);
        }
    }


