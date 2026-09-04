package main.java.com.expense.tracker.cli.services;

import main.java.com.expense.tracker.cli.models.Expense;
import main.java.com.expense.tracker.cli.repositories.ExpenseRepository;
import main.java.com.expense.tracker.cli.repositories.ExpenseRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ExpenseService {

    private ExpenseRepository repository;

    public ExpenseService() {
        repository = ExpenseRepositoryImpl.getInstance();
    }

    public int create(LocalDate date, String description, double amount) {
        try {
            List<Expense> expenseList = repository.findAll();

            int nextId = Expense.genNextId(expenseList);

            repository.save(
                    new Expense(
                            nextId,
                            date,
                            description.trim(),
                            amount
                    )
            );

            return nextId;
        }  catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int update(int id, LocalDate date, String description, double amount) {
        try {
            List<Expense> expenseList = repository.findAll();

            Expense expense = expenseList.stream()
                    .filter(e -> e.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new Exception("Expense not found."));

            expense.setDate(date);
            expense.setDescription(description);
            expense.setAmount(amount);

            repository.save(expense);

            return id;
        }  catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int delete(int id) {
        try {
            if (id < 0) {
                repository.deleteAll();
            } else {
                Expense expense = repository.findById(id).orElseThrow(() -> new Exception("Expense not found."));

                repository.delete(expense);
            }

            return id;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public List<Expense> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Expense> findAllByMonth(int month) {
        try {
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Invalid month value");
            }

            List<Expense> allExpenses = repository.findAll();

            return allExpenses.stream()
                    .filter(e -> {
                        e.getDate().getMonth();
                        return false;
                    }).toList();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Optional<Expense> findById(int id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public double summary() {
        try {
            List<Expense> allExpenses = repository.findAll();

            return allExpenses.stream()
                    .mapToDouble(Expense::getAmount)
                    .sum();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public double summaryByMonth(int month) {
        try {
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Invalid month value");
            }

            List<Expense> allExpenses = repository.findAll();

            return allExpenses.stream()
                    .filter(e -> {
                        e.getDate().getMonth();
                        return false;
                    })
                    .mapToDouble(Expense::getAmount)
                    .sum();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

}
