# Personal Expense Tracker

A simple console app in Java to track your income and expenses, add categories, and see monthly summaries.

## What it does

- Add income and expenses (with category and optional date/description)
- List all transactions
- Filter by category (Food, Rent, Transport, Health, Education, Other or custom)
- Monthly summary: total income, total expense, remaining balance
- Delete a transaction by ID

Rules: expense amount can't be negative; if you don't enter a date, today is used; category is required.

## How to run

You need **Java 17+** (and optionally Maven).

**Option 1 – with Maven**

```bash
# clone the repo then:
cd personal-expense-tracker   # or your folder name

mvn compile
mvn exec:java
```

**Option 2 – run compiled classes**

```bash
mvn compile
java -cp target/classes com.expensetracker.ui.ExpenseTrackerApp
```

**Option 3 – run from JAR**

```bash
mvn package
java -jar target/personal-expense-tracker-1.0-SNAPSHOT.jar
```

## Example

```
========================================
   Personal Expense Tracker
========================================

1. Add income
2. Add expense
3. List all transactions
4. Filter by category
5. Monthly summary
6. Delete transaction (by ID)
7. Exit
Choice: 1
Amount: 1500
Categories: 1=FOOD, 2=RENT, 3=TRANSPORT, 4=HEALTH, 5=EDUCATION, 6=OTHER
Category (1-6 or name): 6
Date (YYYY-MM-DD, empty = today): 
Description (optional): Salary
Added: [1] INCOME | 1500.00 | OTHER | 2026-02-07 | Salary

Choice: 2
Amount: 250
Category (1-6 or name): 1
Date (YYYY-MM-DD, empty = today): 2026-02-07
Description (optional): Groceries
Added: [2] EXPENSE | 250.00 | FOOD | 2026-02-07 | Groceries

Choice: 3
--- All transactions ---
[1] INCOME | 1500.00 | OTHER | 2026-02-07 | Salary
[2] EXPENSE | 250.00 | FOOD | 2026-02-07 | Groceries

Choice: 5
Year (e.g. 2026): 2026
Month (1-12): 2
--- Summary 2026-02 ---
Total income:  1500.00
Total expense: 250.00
Balance:       1250.00
```

## Project structure

```
src/main/java/com/expensetracker/
├── model/       Transaction, Category, TransactionType, CategoryType
├── service/     TransactionService (add, list, filter, summary, delete)
├── storage/     Storage interface (for future file/CSV)
└── ui/          ConsoleMenu, ExpenseTrackerApp (main)
```

Data is kept in memory for now. Later: file storage (e.g. CSV), then stats, GUI or DB.

## License

MIT or use it for learning – no warranty.
