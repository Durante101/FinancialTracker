package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static com.sun.imageio.plugins.jpeg.JPEG.vendor;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<>();
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

    // Load transactions from file
    public static void loadTransactions(String fileName) {

        String line;

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
            System.err.println("Error");
        }
    }
    // Add deposit transaction
    private static void addDeposit(Scanner scanner) {

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
    // Add payment transaction
    private static void addPayment(Scanner scanner) {

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
                System.out.println("Valid Payment\n");
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

    // Ledger menu options
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
    // Display all transactions
    private static void displayLedger() {


        System.out.println("----All Transactions----");

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }




    }




    // Display deposit transaction
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
    // Display payment transactions
    private static void displayPayments() {

        System.out.println("----Payment----");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }
    }
    // Reports menu options
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

            LocalDate endDate;
            LocalDate startDate;

           switch (input) {
                case "1":

                    endDate = LocalDate.now();
                    startDate = endDate.withDayOfMonth(1);

                    filterTransactionsByDate(startDate, endDate);
                    break;
                case "2":

                    startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

                    filterTransactionsByDate(startDate, endDate);
                    break;
                case "3":

                    startDate = LocalDate.now().withDayOfYear(1);
                    endDate = LocalDate.now();

                    filterTransactionsByDate(startDate, endDate);
                    break;
                case "4":

                    startDate = LocalDate.now().minusYears(1).withDayOfYear(1);
                    endDate = startDate.withMonth(12).withDayOfMonth(31);

                    filterTransactionsByDate(startDate, endDate);
                    break;
                case "5":

                    System.out.println("Enter which vendor report you want displayed");
                    String vendor = scanner.nextLine().trim();

                    filterTransactionsByVendor(vendor, scanner);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    // Filter by date
    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {



        while (!startDate.isAfter(endDate)) {
            for (Transaction transaction : transactions) {
                if (transaction.getDate().isEqual(startDate)) {
                    System.out.println(transaction);
                }
            }

            startDate = startDate.plusDays(1);
        }
    }
    // Filter by vendor
    private static void filterTransactionsByVendor(String l, Scanner scanner) {

        boolean check = false;

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(transaction);
                check = true;
            }
        }

        if (!check) {
            System.err.println("No results found for vendor");
        }
    }

}
