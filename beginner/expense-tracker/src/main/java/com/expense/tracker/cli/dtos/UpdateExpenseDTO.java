package main.java.com.expense.tracker.cli.dtos;

import main.java.com.expense.tracker.cli.utils.Result;

import java.time.LocalDate;

public record UpdateExpenseDTO(
        int id,
        Result<LocalDate> date,
        Result<String> description,
        Result<Double> amount
) {
}
