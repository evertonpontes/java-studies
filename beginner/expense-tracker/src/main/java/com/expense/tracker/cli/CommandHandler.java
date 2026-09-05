package main.java.com.expense.tracker.cli;

import main.java.com.expense.tracker.cli.dtos.AddExpenseDTO;
import main.java.com.expense.tracker.cli.dtos.UpdateExpenseDTO;
import main.java.com.expense.tracker.cli.services.ExpenseService;
import main.java.com.expense.tracker.cli.utils.Result;

import java.time.LocalDate;

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

    }

    private void handleList(String[] args) {

    }

    private void handleSummary(String[] args) {

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
}
