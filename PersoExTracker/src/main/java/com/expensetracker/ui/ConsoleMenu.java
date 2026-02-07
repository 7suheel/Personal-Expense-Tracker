package com.expensetracker.ui;

import com.expensetracker.model.Category;
import com.expensetracker.model.CategoryType;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.TransactionType;
import com.expensetracker.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {
    private final TransactionService service = new TransactionService();
    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    public void run() {
        printWelcome();
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    addIncome();
                    break;
                case "2":
                    addExpense();
                    break;
                case "3":
                    listAll();
                    break;
                case "4":
                    filterByCategory();
                    break;
                case "5":
                    monthlySummary();
                    break;
                case "6":
                    deleteById();
                    break;
                case "7":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please choose 1-7.");
            }
        }
    }

    private void printWelcome() {
        System.out.println("========================================");
        System.out.println("   Personal Expense Tracker");
        System.out.println("========================================");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Add income");
        System.out.println("2. Add expense");
        System.out.println("3. List all transactions");
        System.out.println("4. Filter by category");
        System.out.println("5. Monthly summary");
        System.out.println("6. Delete transaction (by ID)");
        System.out.println("7. Exit");
        System.out.print("Choice: ");
    }

    private void addIncome() {
        addTransaction(TransactionType.INCOME);
    }

    private void addExpense() {
        addTransaction(TransactionType.EXPENSE);
    }

    private void addTransaction(TransactionType type) {
        try {
            System.out.print("Amount: ");
            String amountStr = scanner.nextLine().trim();
            double amount = Double.parseDouble(amountStr);
            if (type == TransactionType.EXPENSE && amount < 0) {
                System.out.println("Expense amount cannot be negative.");
                return;
            }
            if (amount < 0) amount = -amount;

            Category category = chooseCategory();
            LocalDate date = readOptionalDate();
            System.out.print("Description (optional): ");
            String description = scanner.nextLine().trim();

            Transaction t = service.addTransaction(type, amount, category, date, description);
            System.out.println("Added: " + t);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Category chooseCategory() {
        System.out.println("Categories: 1=FOOD, 2=RENT, 3=TRANSPORT, 4=HEALTH, 5=EDUCATION, 6=OTHER");
        System.out.print("Category (1-6 or name): ");
        String input = scanner.nextLine().trim();
        CategoryType[] types = CategoryType.values();
        if (input.matches("\\d+")) {
            int idx = Integer.parseInt(input);
            if (idx >= 1 && idx <= types.length)
                return new Category(types[idx - 1]);
        }
        for (CategoryType ct : types) {
            if (ct.name().equalsIgnoreCase(input))
                return new Category(ct);
        }
        return new Category(input.isEmpty() ? "OTHER" : input);
    }

    private LocalDate readOptionalDate() {
        System.out.print("Date (YYYY-MM-DD, empty = today): ");
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) return LocalDate.now();
        try {
            return LocalDate.parse(line, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Using today.");
            return LocalDate.now();
        }
    }

    private void listAll() {
        List<Transaction> list = service.listAll();
        if (list.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        System.out.println("--- All transactions ---");
        for (Transaction t : list) {
            System.out.println(t);
        }
    }

    private void filterByCategory() {
        System.out.print("Category name (or number 1-6): ");
        String input = scanner.nextLine().trim();
        CategoryType[] types = CategoryType.values();
        if (input.matches("\\d+")) {
            int idx = Integer.parseInt(input);
            if (idx >= 1 && idx <= types.length)
                input = types[idx - 1].name();
        }
        List<Transaction> list = service.filterByCategory(input);
        if (list.isEmpty()) {
            System.out.println("No transactions for category: " + input);
            return;
        }
        System.out.println("--- Transactions for " + input + " ---");
        for (Transaction t : list) {
            System.out.println(t);
        }
    }

    private void monthlySummary() {
        int year = readInt("Year (e.g. 2026): ", 2000, 2100);
        int month = readInt("Month (1-12): ", 1, 12);
        TransactionService.MonthlySummary summary = service.monthlySummary(year, month);
        System.out.printf("--- Summary %d-%02d ---%n", year, month);
        System.out.printf("Total income:  %.2f%n", summary.getTotalIncome());
        System.out.printf("Total expense: %.2f%n", summary.getTotalExpense());
        System.out.printf("Balance:       %.2f%n", summary.getBalance());
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int n = Integer.parseInt(scanner.nextLine().trim());
                if (n >= min && n <= max) return n;
            } catch (NumberFormatException ignored) { }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    private void deleteById() {
        System.out.print("Transaction ID to delete: ");
        String line = scanner.nextLine().trim();
        try {
            int id = Integer.parseInt(line);
            Optional<Transaction> removed = service.deleteById(id);
            if (removed.isPresent()) {
                System.out.println("Deleted: " + removed.get());
            } else {
                System.out.println("No transaction with ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }
}
