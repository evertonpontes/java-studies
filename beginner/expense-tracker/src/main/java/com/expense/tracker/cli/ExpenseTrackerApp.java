package main.java.com.expense.tracker.cli;

public class ExpenseTrackerApp {

    static void main(String[] args) {
        CommandHandler commandHandler = new CommandHandler();

        commandHandler.handler(args);
    }
}
