package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

     static ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    System.out.println("Potato Sensei wishes you good luck on your journey");
                    System.out.println("( ＾◡＾)っ ♡");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }


    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        String line;
//      make new trancation object
        // from parts to transcation object
        // save tranaction object to the arrylist is called tranactions up top
        //
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                Transaction myT = new Transaction(date, time, description, vendor, amount);
                transactions.add(myT);
            }
            br.close();
        } catch (Exception e) {
            System.err.println();
        }
    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.

        /*
        1- Ask the user for the info
        2- Parse the date, time add amount
        3- create new transaction
        4- add the new transaction in the list
        5- create a bufferedwriter
        6- write the transaction to the file
         */
        //Deposit Date and Time
        boolean check = false;
        LocalDate date = null;
        LocalTime time = null;

        while (!check)
            try {
                System.out.println("Enter the date and time in this format: yyyy-MM-dd HH:mm:ss");
                String userDateTime = scanner.nextLine().trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(userDateTime, formatter);
                date = dateTime.toLocalDate();
                time = dateTime.toLocalTime();
                check = true;


            } catch (Exception e) {
                System.err.println("Incorrect Format\n");

            }

        String description = "";
        //Deposit Description
        while (description.isEmpty()) {
            System.out.println("Enter the description of the deposit");
            description = scanner.nextLine().trim();

        }

        String vendor = "";
        //Deposit Vendor
        while (vendor.isEmpty()) {
            System.out.println("Enter the Vendor for the deposit");
            vendor = scanner.nextLine().trim();
        }

        check = false;

        double amount = 0;

        while (!check) {
            System.out.println("Enter the amount you would like to deposit");
            amount = scanner.nextDouble();
            scanner.nextLine();
            if (amount >= 0) {
                System.out.println("Valid Deposit\n");
                check = true;
            } else {
                System.err.println("Incorrect\nMust be a positive Deposit\n");
            }
        }
        Transaction transaction = new Transaction(date, time, description, vendor, amount);

        transactions.add(transaction);

        try(BufferedWriter writeInfo = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writeInfo.write(transaction.toString());
            writeInfo.newLine();
        }catch (Exception e) {
            System.err.println("Error during transfer to file");
        }
    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
        boolean check = false;
        LocalDate date = null;
        LocalTime time = null;

        while (!check)
            try {
                System.out.println("Enter the date and time in this format: yyyy-MM-dd HH:mm:ss");
                String userDateTime = scanner.nextLine().trim();
                String[] parts = userDateTime.split(" ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(userDateTime, formatter);
                date = dateTime.toLocalDate();
                time = dateTime.toLocalTime();
                check = true;


            } catch (Exception e) {
                System.err.println("Incorrect Format\n");

            }

        String description = "";
        //Deposit Description
        while (description.isEmpty()) {
            System.out.println("Enter the description of the payment");
            description = scanner.nextLine().trim();

        }

        String vendor = "";
        //Deposit Vendor
        while (vendor.isEmpty()) {
            System.out.println("Enter the Vendor for the payment");
            vendor = scanner.nextLine().trim();
        }

        check = false;

        double amount = 0;

        while (!check) {
            System.out.println("Enter the amount that was paid");
            amount = scanner.nextDouble();
            scanner.nextLine();
            if (amount >= 0) {
                System.out.println("Valid Payment");
                check = true;
            } else {
                System.err.println("Incorrect\nMust be a positive Amount\n");
            }
        }
        amount   *= -1;

        Transaction transaction = new Transaction(date, time, description, vendor, amount);

        transactions.add(transaction);

        try(BufferedWriter writeInfo = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            writeInfo.write(transaction.toString());
            writeInfo.newLine();
        }catch (Exception e) {
            System.err.println("Error during transfer to file");
        }
    }


    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.

        System.out.println("----All Transactions----");

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }




    }





    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("----Deposits----");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("----Payment----");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }
    }
// LOOK UP LOCAL date and year to see methods getyear month and day
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }

}
