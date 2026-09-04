package main.java.com.expense.tracker.cli.models;

import java.time.LocalDate;
import java.util.List;

public class Expense {

    // Attributes

    private int id;
    private LocalDate date;
    private String description;
    private double amount;

    public Expense(
            int id,
            LocalDate date,
            String description,
            double amount
    ) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    // getters and setters

    public int getId() {
        return this.id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static  int genNextId(List<Expense> expenseList) {

        return expenseList.stream()
                .mapToInt(Expense::getId)
                .max()
                .orElse(0) + 1;
    }
}
