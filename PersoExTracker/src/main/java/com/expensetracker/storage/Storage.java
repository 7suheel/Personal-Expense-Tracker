package com.expensetracker.storage;

import com.expensetracker.model.Transaction;

import java.util.List;

// for later when we add file save/load
public interface Storage {
    void save(List<Transaction> transactions);
    List<Transaction> load();
}
