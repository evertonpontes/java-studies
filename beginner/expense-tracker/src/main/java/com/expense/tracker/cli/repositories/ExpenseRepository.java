package main.java.com.expense.tracker.cli.repositories;

import main.java.com.expense.tracker.cli.models.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    public void save(Expense expense);
    public void deleteAll(List<Expense> expenseList);
    public void delete(Expense expense);

    public List<Expense> findAll();
    public Optional<Expense> findById();
}
