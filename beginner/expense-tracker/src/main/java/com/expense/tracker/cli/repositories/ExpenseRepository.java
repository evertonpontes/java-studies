package main.java.com.expense.tracker.cli.repositories;

import main.java.com.expense.tracker.cli.models.Expense;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    void saveAll(List<Expense> expenseList) throws IOException;
    void save(Expense expense) throws Exception;
    void deleteAll() throws IOException;
    void delete(Expense expense) throws Exception;

    List<Expense> findAll() throws Exception;
    Optional<Expense> findById(int id) throws Exception;
}
