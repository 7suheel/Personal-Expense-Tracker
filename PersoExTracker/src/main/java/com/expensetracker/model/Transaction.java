package com.expensetracker.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final int id;
    private final TransactionType type;
    private final double amount;
    private final Category category;
    private final LocalDate date;
    private final String description;

    public Transaction(int id, TransactionType type, double amount, Category category, LocalDate date, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.category = category != null ? category : new Category(CategoryType.OTHER);
        this.date = date != null ? date : LocalDate.now();
        this.description = description != null ? description : "";
    }

    public int getId() { return id; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public Category getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        String desc = description.isEmpty() ? "-" : description;
        return String.format("[%d] %s | %.2f | %s | %s | %s",
                id, type, amount, category.getName(),
                date.format(DateTimeFormatter.ISO_LOCAL_DATE), desc);
    }
}
