package com.expensetracker.service;

import com.expensetracker.model.Category;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.TransactionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionService {
    private final List<Transaction> transactions = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public Transaction addTransaction(TransactionType type, double amount, Category category, LocalDate date, String description) {
        if (type == TransactionType.EXPENSE && amount < 0)
            throw new IllegalArgumentException("Expense amount cannot be negative.");
        if (category == null)
            throw new IllegalArgumentException("Category is mandatory.");
        int id = nextId.getAndIncrement();
        Transaction t = new Transaction(id, type, amount, category, date, description);
        transactions.add(t);
        return t;
    }

    public List<Transaction> listAll() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> filterByCategory(Category category) {
        if (category == null) return listAll();
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getCategory().getName().equalsIgnoreCase(category.getName()))
                result.add(t);
        }
        return result;
    }

    public List<Transaction> filterByCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) return listAll();
        List<Transaction> result = new ArrayList<>();
        String name = categoryName.trim();
        for (Transaction t : transactions) {
            if (t.getCategory().getName().equalsIgnoreCase(name))
                result.add(t);
        }
        return result;
    }

    public MonthlySummary monthlySummary(int year, int month) {
        double income = 0;
        double expense = 0;
        for (Transaction t : transactions) {
            if (t.getDate().getYear() != year || t.getDate().getMonthValue() != month)
                continue;
            if (t.getType() == TransactionType.INCOME)
                income += t.getAmount();
            else
                expense += t.getAmount();
        }
        return new MonthlySummary(year, month, income, expense, income - expense);
    }

    public Optional<Transaction> deleteById(int id) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == id) {
                return Optional.of(transactions.remove(i));
            }
        }
        return Optional.empty();
    }

    public Optional<Transaction> findById(int id) {
        for (Transaction t : transactions) {
            if (t.getId() == id) return Optional.of(t);
        }
        return Optional.empty();
    }

    public static final class MonthlySummary {
        private final int year;
        private final int month;
        private final double totalIncome;
        private final double totalExpense;
        private final double balance;

        public MonthlySummary(int year, int month, double totalIncome, double totalExpense, double balance) {
            this.year = year;
            this.month = month;
            this.totalIncome = totalIncome;
            this.totalExpense = totalExpense;
            this.balance = balance;
        }

        public int getYear() { return year; }
        public int getMonth() { return month; }
        public double getTotalIncome() { return totalIncome; }
        public double getTotalExpense() { return totalExpense; }
        public double getBalance() { return balance; }
    }
}
