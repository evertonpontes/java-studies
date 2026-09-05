package main.java.com.expense.tracker.cli.services;

import main.java.com.expense.tracker.cli.dtos.AddExpenseDTO;
import main.java.com.expense.tracker.cli.dtos.UpdateExpenseDTO;
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

    public int create(AddExpenseDTO dto) {
        try {
            List<Expense> expenseList = repository.findAll();

            int nextId = Expense.genNextId(expenseList);

            repository.save(
                    new Expense(
                            nextId,
                            dto.date(),
                            dto.description(),
                            dto.amount()
                    )
            );

            return nextId;
        }  catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int update(UpdateExpenseDTO dto) {
        try {
            List<Expense> expenseList = repository.findAll();

            Expense expense = expenseList.stream()
                    .filter(e -> e.getId() == dto.id())
                    .findFirst()
                    .orElseThrow(() -> new Exception("Expense not found."));

            if (dto.date().isOk()) {
                expense.setDate(dto.date().getInstance());

            }

            if (dto.description().isOk()) {
                expense.setDescription(dto.description().getInstance());

            }

            if (dto.amount().isOk()) {
                expense.setAmount(dto.amount().getInstance());
            }

            repository.save(expense);

            return dto.id();
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
                    .filter(e -> e.getDate().getMonth().getValue() == month)
                    .toList();

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
                    .filter(e -> e.getDate().getMonth().getValue() == month)
                    .mapToDouble(Expense::getAmount)
                    .sum();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

}
