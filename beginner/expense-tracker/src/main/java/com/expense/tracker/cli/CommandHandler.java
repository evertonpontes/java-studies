package main.java.com.expense.tracker.cli;

import main.java.com.expense.tracker.cli.dtos.AddExpenseDTO;
import main.java.com.expense.tracker.cli.dtos.UpdateExpenseDTO;
import main.java.com.expense.tracker.cli.models.Expense;
import main.java.com.expense.tracker.cli.services.ExpenseService;
import main.java.com.expense.tracker.cli.utils.Result;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommandHandler {

    private final ExpenseService service;

    public CommandHandler() {
        service = new ExpenseService();
    }

    public void handler(String[] args) {

        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];

        switch (command) {
            case "add":
                handleAdd(args);
                break;

            case "update":
                handleUpdate(args);
                break;

            case "delete":
                handleDelete(args);
                break;

            case "list":
                handleList(args);
                break;

            case "summary":
                handleSummary(args);
                break;

            default:
                printHelp();
        }
    }

    private void handleAdd(String[] args) {
        if (args.length < 3) {
            printHelpAdd();
            return;
        }

        String dateValue = null;
        String description = null;
        String amountValue = null;

        if (args.length == 3) {
            if (!args[1].startsWith("--") && !args[2].startsWith("--")) {
                description = args[1];
                amountValue = args[2];
            }

        }

        String option = null;

        for (int i = 1; i < args.length; i++) {
                String arg = args[i];

            if (option == null && arg.startsWith("--")) {
                option = arg;
                continue;
            }

            if (option == null && !arg.startsWith("--") && args.length > 3) {
                System.out.println("Unexpected argument: "+arg);
                printHelpAdd();
                return;
            }

            if (option != null) {
                if (arg.startsWith("--")) {
                    System.out.println("Unexpected argument: "+arg);
                    printHelpAdd();
                    return;
                }

                switch (option) {
                    case "--date":
                        dateValue = arg;
                        option = null;
                        break;
                    case "--description":
                        description = arg;
                        option = null;
                        break;
                     case "--amount":
                        amountValue = arg;
                        option = null;
                        break;
                    default:
                        System.out.println("Unknown argument: "+option);
                        printHelpAdd();
                        return;
                }
            }
        }

        Result<LocalDate> dateResult = Result.ok(LocalDate.now());

        if (dateValue != null) {
            dateResult = Result.tryParse(dateValue, LocalDate::parse);
        }

        if (amountValue == null || description == null) {
            printHelpAdd();
            return;
        }

        Result<Double> doubleResult = Result.tryParse(amountValue, Double::parseDouble);

        if (doubleResult.isFailure()) {
            System.out.println("Invalid argument: "+amountValue);
            printHelpAdd();
            return;
        }

        if (dateResult.isFailure()) {
            System.out.println("Invalid argument: "+dateValue);
            printHelpAdd();
            return;
        }

        AddExpenseDTO dto = new AddExpenseDTO(
                dateResult.getInstance(),
                description,
                doubleResult.getInstance()
        );

        int result = service.create(dto);

        System.out.printf("Expense added successfully: (ID: %d)\n", result);
    }

    private void handleUpdate(String[] args) {
        if (args.length < 4) {
            printHelpUpdate();
            return;
        }

        Result<Integer> resultId = Result.tryParse(args[1], Integer::parseInt);

        if (resultId.isFailure()) {
            System.out.println("Invalid argument: "+args[1]);
            printHelpUpdate();
            return;
        }

        String dateValue = null;
        String description = null;
        String amountValue = null;

        if (args.length == 4) {
            if (!args[1].startsWith("--") && !args[2].startsWith("--")) {
                description = args[2];
                amountValue = args[3];
            }

        }

        String option = null;

        for (int i = 2; i < args.length; i++) {
            String arg = args[i];

            if (option == null && arg.startsWith("--")) {
                option = arg;
                continue;
            }

            if (option == null && !arg.startsWith("--") && args.length > 4) {
                System.out.println("Unexpected argument: "+arg);
                printHelpUpdate();
                return;
            }

            if (option != null) {
                if (arg.startsWith("--")) {
                    System.out.println("Unexpected argument: "+arg);
                    printHelpUpdate();
                    return;
                }

                switch (option) {
                    case "--date":
                        dateValue = arg;
                        option = null;
                        break;
                    case "--description":
                        description = arg;
                        option = null;
                        break;
                    case "--amount":
                        amountValue = arg;
                        option = null;
                        break;
                    default:
                        System.out.println("Unknown argument: "+option);
                        printHelpUpdate();
                        return;
                }
            }
        }

        Result<LocalDate> dateResult = Result.ok(null);
        Result<Double> doubleResult = Result.ok(null);
        Result<String> descritpionResult = Result.ok(null);

        if (dateValue != null) {
            dateResult = Result.tryParse(dateValue, LocalDate::parse);
        }

        if (amountValue != null) {
            doubleResult = Result.tryParse(amountValue, Double::parseDouble);
        }

        if (description != null) {
            descritpionResult = Result.ok(description);
        }

        if (doubleResult.isFailure()) {
            System.out.println("Invalid argument: "+amountValue);
            printHelpUpdate();
            return;
        }

        if (dateResult.isFailure()) {
            System.out.println("Invalid argument: "+dateValue);
            printHelpUpdate();
            return;
        }

        UpdateExpenseDTO dto = new UpdateExpenseDTO(
                resultId.getInstance(),
                dateResult,
                descritpionResult,
                doubleResult
        );

        int result = service.update(dto);

        System.out.printf("Expense updated successfully: (ID: %d)\n", result);
    }

    private void handleDelete(String[] args) {
        if (args.length < 2) {
            printHelpDelete();
            return;
        }

        Result<Integer> resultId = Result.ok(null);

        if (args.length == 2) {
            resultId = Result.tryParse(args[1], Integer::parseInt);
        }

        String option = null;

        for (int i = 1; i < args.length; i++) {
            String arg = args[i];

            if (option == null && arg.startsWith("--")) {
                option = arg;
                continue;
            }

            if (option == null && !arg.startsWith("--") && args.length > 2) {
                System.out.println("Unexpected argument: "+arg);
                printHelpDelete();
                return;
            }

            if (option != null) {
                if (arg.startsWith("--")) {
                    System.out.println("Unexpected argument: "+arg);
                    printHelpDelete();
                    return;
                }

                if (option.equals("--id")) {
                    resultId = Result.tryParse(arg, Integer::parseInt);
                } else {
                    System.out.println("Unknown argument: "+option);
                    printHelpDelete();
                    return;
                }
            }
        }

        if (resultId.isFailure() || resultId.isNull()) {
            printHelpDelete();
            return;
        }

        service.delete(resultId.getInstance());

        System.out.println("Expense deleted successfully");
    }

    private void handleList(String[] args) {
        List<Expense> expenseList = new ArrayList<>();

        Result<Integer> monthResult = Result.ok(null);

        if (args.length >= 1) {
            expenseList = service.findAll();
        }

        if (args.length > 1) {
            String option = null;

            for (int i = 1; i < args.length; i++) {
                String arg = args[i];

                if (option == null && arg.startsWith("--")) {
                    option = arg;
                    continue;
                }

                if (option == null && !arg.startsWith("--")) {
                    System.out.println("Unexpected argument: "+arg);
                    printHelpList();
                    return;
                }

                if (option != null) {
                    if (arg.startsWith("--")) {
                        System.out.println("Unexpected argument: "+arg);
                        printHelpList();
                        return;
                    }

                    if (option.equals("--month")) {
                        monthResult = Result.tryParse(arg, Integer::parseInt);

                        if (monthResult.isFailure()) {
                            System.out.println("Invalid argument: "+arg);
                            printHelpList();
                            return;
                        }

                        expenseList = service.findAllByMonth(monthResult.getInstance());

                    } else {
                        System.out.println("Unknown argument: "+option);
                        printHelpList();
                        return;
                    }
                }
            }
        }

        printExpenses(expenseList);
    }

    private void handleSummary(String[] args) {
        double totalExpenses = 0;

        Result<Integer> monthResult = Result.ok(null);

        if (args.length >= 1) {
            totalExpenses = service.summary();
        }

        if (args.length > 1) {
            String option = null;

            for (int i = 1; i < args.length; i++) {
                String arg = args[i];

                if (option == null && arg.startsWith("--")) {
                    option = arg;
                    continue;
                }

                if (option == null && !arg.startsWith("--")) {
                    System.out.println("Unexpected argument: "+arg);
                    printHelpSummary();
                    return;
                }

                if (option != null) {
                    if (arg.startsWith("--")) {
                        System.out.println("Unexpected argument: "+arg);
                        printHelpSummary();
                        return;
                    }

                    if (option.equals("--month")) {
                        monthResult = Result.tryParse(arg, Integer::parseInt);

                        if (monthResult.isFailure()) {
                            System.out.println("Invalid argument: "+arg);
                            printHelpSummary();
                            return;
                        }

                        totalExpenses = service.summaryByMonth(monthResult.getInstance());

                    } else {
                        System.out.println("Unknown argument: "+option);
                        printHelpSummary();
                        return;
                    }
                }
            }
        }

        System.out.printf("Total expenses: $%.2f",totalExpenses);
    }

    private void printHelp() {
        System.out.println("Hello World");
    }

    private void printHelpAdd() {
        System.out.println("USAGE: expense-tracker add [OPTIONS]\n");
        System.out.println(
                """
                ALIAS:
                    expense-tracker add <description> <amount>
                """
        );
        System.out.println("""
                    OPTIONS:
                        --date              Set the date of the expense (Optional), by default use current date of system
                        --description       Set the description of the expense
                        --amount            Set the amount of the expense
                    """);
    }

    private void printHelpUpdate() {
        System.out.println("USAGE: expense-tracker update ID [OPTIONS]\n");
        System.out.println(
                """
                ALIAS:
                    expense-tracker update ID <description> <amount>
                """
        );
        System.out.println("""
                    OPTIONS:
                        --date              Set the date of the expense (Optional), by default use current date of system
                        --description       Set the description of the expense
                        --amount            Set the amount of the expense
                    """);
    }

    private void printHelpDelete() {
        System.out.println("USAGE: expense-tracker delete [OPTIONS]\n");
        System.out.println(
                """
                ALIAS:
                    expense-tracker delete ID
                """
        );
        System.out.println("""
                    OPTIONS:
                        --id              Provide the id of expense to delete
                    """);
    }

    private void printHelpList() {
        System.out.println("USAGE: expense-tracker list [OPTIONS]\n");
        System.out.println(
                """
                ALIAS:
                    expense-tracker list
                """
        );
        System.out.println("""
                    OPTIONS:
                        --month           List all expenses from that specific month
                    """);
    }

    private void printHelpSummary() {
        System.out.println("USAGE: expense-tracker summary [OPTIONS]\n");
        System.out.println(
                """
                ALIAS:
                    expense-tracker summary
                """
        );
        System.out.println("""
                    OPTIONS:
                        --month           Summary all expenses from that specific month
                    """);
    }

    private void printExpenses(List<Expense> expenses) {

        if (expenses.isEmpty()) {
            System.out.println("Expenses not found.");
            return;
        }

        int DESCRIPTION_WIDTH = 30;
        int DATE_WIDTH = 19;
        int AMOUNT_WIDTH = 12;

        String separator =
                "+" + "-".repeat(5) + "+"
                        + "-".repeat(DATE_WIDTH + 2) + "+"
                        + "-".repeat(DESCRIPTION_WIDTH + 2) + "+"
                        + "-".repeat(AMOUNT_WIDTH + 2) + "+";

        System.out.println(separator);

        System.out.printf(
                "| %-3s | %-" + DATE_WIDTH + "s | %-" + DESCRIPTION_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |%n",
                "ID",
                "DATE",
                "DESCRIPTION",
                "AMOUNT"
        );

        System.out.println(separator);

        for(Expense expense : expenses) {
            String description = truncate(
                    expense.getDescription(),
                    DESCRIPTION_WIDTH
            );

            String date = expense.getDate().toString();

            System.out.printf(
                    "| %-3s | %-" + DATE_WIDTH + "s | %-" + DESCRIPTION_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |%n",
                    expense.getId(),
                    date,
                    description,
                    expense.getAmount()
            );
        }

        System.out.println(separator);

    }

    private String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength - 3) + "...";
    }
}
