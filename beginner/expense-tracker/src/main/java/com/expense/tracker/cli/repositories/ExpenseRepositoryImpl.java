package main.java.com.expense.tracker.cli.repositories;

import main.java.com.expense.tracker.cli.mappers.CSVMapper;
import main.java.com.expense.tracker.cli.models.Expense;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseRepositoryImpl implements ExpenseRepository{
    private static ExpenseRepository instance;
    private final Path filePath;
    private final CSVMapper mapper;


    private ExpenseRepositoryImpl(Path filePath, CSVMapper mapper) {
        this.filePath = filePath;
        this.mapper = mapper;
    }

    public static ExpenseRepository getInstance() {
        try {
            Path filePath = Path.of("expenses.csv");
            if (Files.notExists(filePath)) {
                Files.writeString(
                        filePath,
                        "id,date,description,amount"
                );
            }

            if (instance == null) {
                return new ExpenseRepositoryImpl(
                        filePath,
                        new CSVMapper()
                );
            }

            return instance;
        } catch (Exception e) {
            System.out.println("An error occurred: "+e.getMessage());
            return null;
        }
    }

    @Override
    public void saveAll(List<Expense> expenseList) throws IOException {
        String content = mapper.toCSV(expenseList);
        Files.writeString(filePath, content);
    }

    @Override
    public void save(Expense expense) throws Exception {
        List<Expense> expenseList = findAll();
        int expenseIndex = -1;
        for (int i = 0; i < expenseList.size(); i++) {
            Expense e = expenseList.get(i);

            if (e.getId() == expense.getId()) {
                expenseIndex = i;
            }
        }
        if (expenseIndex >= 0) {
            expenseList.set(expenseIndex, expense);
        } else {
            expenseList.add(expense);
        }
        saveAll(expenseList);
    }

    @Override
    public void deleteAll() throws IOException {
        List<Expense> enptyExpenseList = new ArrayList<>();

        String content = mapper.toCSV(enptyExpenseList);

        Files.writeString(filePath, content);
    }

    @Override
    public void delete(Expense expense) throws Exception {
        List<Expense> expenseList = findAll();
        int expenseIndex = -1;
        for (int i = 0; i < expenseList.size(); i++) {
            Expense e = expenseList.get(i);

            if (e.getId() == expense.getId()) {
                expenseIndex = i;
            }
        }
        if (expenseIndex >= 0) {
            expenseList.remove(expenseIndex);
        } else {
            throw new Exception("Expense not found.");
        }

        saveAll(expenseList);
    }

    @Override
    public List<Expense> findAll() throws Exception {
        String text = Files.readString(filePath);
        return mapper.fromCSV(text);
    }

    @Override
    public Optional<Expense> findById(int id) throws Exception {
        List<Expense> expenseList = findAll();

        return expenseList
                .stream()
                .filter(e -> e.getId() == id)
                .findFirst();
    }
}
