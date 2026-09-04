package main.java.com.expense.tracker.cli.mappers;

import main.java.com.expense.tracker.cli.models.Expense;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CSVMapper {

    public List<Expense> fromCSV(String text) throws Exception {
        List<Expense> expenseList = new ArrayList<>();

        String textWithoutHeader = extractHeader(text);

        List<String> rows = extractRows(textWithoutHeader);

        for (String row : rows) {
            expenseList.add(
                    parseExpense(row)
            );
        }

        return  expenseList;
    }

    public String toCSV(List<Expense> expenseList) {

        StringBuilder body = new StringBuilder();

        body.append(
                """
                id,date,description,amount
                """
        );

        for (Expense expense : expenseList) {
         body.append(
                 String.format(Locale.US,"%d,%s,%s,%.2f\n",
                         expense.getId(),
                         expense.getDate(),
                         escapeCode(expense.getDescription()),
                         expense.getAmount())
         );

        }
        return body.toString();
    }

    private String escapeCode(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String unescapeCode(String value) {
        return value
                .replace("\\\\", "\\")
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }

    private String extractHeader(String text) throws IllegalArgumentException {
        String header = "id,date,description,amount\n";

        if (!text.contains(header)) {
            throw new IllegalArgumentException("Invalid .csv file format.");
        }

        return text.replace(header, "");
    }

    private List<String> extractRows(String text) {
        List<String> rowList = new ArrayList<>();

        boolean insideString = false;
        boolean escaped = false;

        int startIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (c == '"') {
                insideString = !insideString;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                continue;
            }

            if (c == '\n') {
                String row = text.substring(startIndex, i);
                rowList.add(row);
                startIndex = i + 1;
            }
        }

        return rowList;
    }

    private List<String> extractValues(String text) {
        List<String> valueList = new ArrayList<>();

        boolean insideString = false;
        boolean escaped = false;

        int startIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (c == '"') {
                insideString = !insideString;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                continue;
            }

            if (c == ',') {
                String value = text.substring(startIndex, i);
                valueList.add(value);
                startIndex = i + 1;
            }
        }

        String value = text.substring(startIndex);
        valueList.add(value);

        return valueList;
    }

    private Expense parseExpense(String row) throws Exception {
        List<String> values = extractValues(row);

        String idValue = values.getFirst();
        int id = extractId(idValue);

        if (id <= 0) {
            throw new Exception("Id invalid");
        }

        String dateValue = values.get(1);
        LocalDate date = extractDate(dateValue);

        String descriptionValue = values.get(2);
        String description = unescapeCode(descriptionValue).trim();

        String amountValue = values.get(3);
        double amount = extractAmount(amountValue);

        return new Expense(
                id,
                date,
                description,
                amount
        );
    }

    private int extractId(String value) throws NumberFormatException {
        return Integer.parseInt(value);
    }

    private LocalDate extractDate(String value) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
                .withResolverStyle(ResolverStyle.STRICT);

        return LocalDate.parse(value, formatter);
    }

    private double extractAmount(String value) throws NumberFormatException, NullPointerException {
        return Double.parseDouble(value);
    }

}
