package main.java.com.expense.tracker.cli.dtos;

import java.time.LocalDate;

public record AddExpenseDTO(
        LocalDate date,
        String description,
        double amount
) {}
