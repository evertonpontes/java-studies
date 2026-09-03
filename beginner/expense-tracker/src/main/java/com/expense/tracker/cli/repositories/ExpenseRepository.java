package main.java.com.expense.tracker.cli.repositories;

import java.util.List;
import java.util.Optional;

import main.java.com.expense.tracker.cli.models.Expense;

public interface ExpenseRepository {
	public void save(Expense expense);
	public void deleteAll(List<Expense> expenses);
	public void delete(Expense expense);
	
	public List<Expense> findAll();
	public Optional<Expense> findById();
}
