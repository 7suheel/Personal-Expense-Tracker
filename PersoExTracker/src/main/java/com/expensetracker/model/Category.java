package com.expensetracker.model;

public class Category {
    private final String name;

    public Category(String name) {
        if (name == null || name.isBlank())
            this.name = CategoryType.OTHER.name();
        else
            this.name = name.trim();
    }

    public Category(CategoryType type) {
        this.name = type.name();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
