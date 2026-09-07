# Expense Tracker CLI

A simple command-line expense tracker built with **Java**, created as part of my Java studies and based on the [Expense Tracker challenge](https://roadmap.sh/projects/expense-tracker) from [roadmap.sh](https://roadmap.sh/).

The application allows users to manage their expenses directly from the command line, including adding, updating, deleting, listing, and summarizing expenses.

## Challenge

This project was developed based on the following roadmap.sh challenge:

- [Expense Tracker — roadmap.sh](https://roadmap.sh/projects/expense-tracker)

The challenge focuses on building a command-line application while practicing **logic building, command parsing, filesystem operations, data management, and error handling**.

## Features

- Add expenses with a description and amount
- Update existing expenses
- Delete expenses
- List all expenses
- Display a summary of all expenses
- Display a summary for a specific month
- Store expenses locally using a CSV file
- Command-line argument parsing
- Validation and error handling for invalid commands and arguments

## Technologies

- **Java**
- **Java Collections**
- **Java I/O**
- **CSV file persistence**
- **Command-line arguments**
- **Object-Oriented Programming**

## Project Structure

```text
expense-tracker/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── expense/
│                   └── tracker/
│                       └── cli/
│                           ├── dtos/
│                           │   ├── AddExpenseDTO.java
│                           │   └── UpdateExpenseDTO.java
│                           │
│                           ├── mappers/
│                           │   └── CSVMapper.java
│                           │
│                           ├── models/
│                           │   └── Expense.java
│                           │
│                           ├── repositories/
│                           │   ├── ExpenseRepository.java
│                           │   └── ExpenseRepositoryImpl.java
│                           │
│                           ├── services/
│                           │   └── ExpenseService.java
│                           │
│                           ├── utils/
│                           │   └── Result.java
│                           │
│                           ├── CommandHandler.java
│                           └── ExpenseTrackerApp.java
│
├── build.bat
├── expense-tracker.bat
├── .gitignore
└── expense-tracker.iml
```

The application follows a simple layered structure:

```text
CLI
 │
 ▼
CommandHandler
 │
 ▼
Service
 │
 ▼
Repository
 │
 ▼
CSV file
```

This separation keeps command parsing, business logic, and data persistence independent from each other.

## Running the Application

### Requirements

- Java JDK installed
- Java 17 or later recommended
- Windows, Linux, or macOS

You can verify your Java installation with:

```bash
java -version
```

and:

```bash
javac -version
```

## Build

There are several ways to compile the project depending on your environment.

### Windows PowerShell

PowerShell can recursively find all `.java` files, store them temporarily in `sources.txt`, and pass that file to `javac`.

From the project root:

```powershell
Get-ChildItem -Recurse -Filter *.java src\main\java | ForEach-Object { $_.FullName } > sources.txt
javac -d out @sources.txt
Remove-Item sources.txt
```

The process:

1. Finds every `.java` file under `src/main/java`.
2. Stores their paths in `sources.txt`.
3. Compiles all source files into the `out` directory.
4. Removes the temporary `sources.txt` file.

### Windows Batch

The project includes a `build.bat` script that performs the same process automatically.

Simply run:

```bat
build.bat
```

The script:

1. Creates the `out` directory if necessary.
2. Finds all Java source files recursively.
3. Generates a temporary `sources.txt`.
4. Compiles the source files.
5. Creates the application JAR.
6. Removes the temporary `sources.txt`.

### Bash / Linux / macOS / Git Bash

On Bash-compatible environments, the source files can be passed directly to `javac` without creating a temporary file:

```bash
javac -d out $(find src/main/java -name "*.java")
```

The `find` command recursively locates every `.java` file under `src/main/java`.

## Creating the JAR

After compiling the project, create the executable JAR with:

```bash
jar cfe expense-tracker.jar main.java.com.expense.tracker.cli.ExpenseTrackerApp -C out .
```

The command specifies:

- `expense-tracker.jar` — the output JAR file
- `com.expense.tracker.cli.ExpenseTrackerApp` — the main class
- `-C out .` — includes the compiled classes from the `out` directory

The resulting structure can look like:

```text
expense-tracker/
├── expense-tracker.jar
├── expense-tracker.bat
└── src/
```

## Running the JAR

The application can be executed directly with:

```bash
java -jar expense-tracker.jar
```

Commands can be passed directly to the JAR:

```bash
java -jar expense-tracker.jar add --description "Lunch" --amount 20
```

## Windows BAT Launcher

The project also includes an expense-tracker.bat launcher to simplify execution on Windows.

`expense-tracker.bat`

```bat
@echo off

java -jar expense-tracker.jar %*
```

The `%*` variable forwards all arguments passed to the batch file to the Java application.

For example:

```bat
expense-tracker.bat add --description "Lunch" --amount 20
```

The launcher forwards all command-line arguments to the application.

## Commands

### Add an expense

```bash
expense-tracker add --description "Lunch" --amount 20
```

An expense can also be created using the amount as a positional argument:

```bash
expense-tracker add "Lunch" 20
```

Optional date:

```bash
expense-tracker add --description "Lunch" --amount 20 --date 2026-09-07
```

The options can be provided in different orders.

### Update an expense

```bash
expense-tracker update --id 1 --description "Dinner" --amount 30
```

### Delete an expense

```bash
expense-tracker delete --id 1
```

### List expenses

```bash
expense-tracker list
```

### Summary

```bash
expense-tracker summary
```

### Monthly summary

```bash
expense-tracker summary --month 8
```

## Expense Model

Each expense contains the following information:

| Field         | Description                       |
| ------------- | --------------------------------- |
| `id`          | Unique identifier                 |
| `date`        | Date when the expense was created |
| `description` | Description of the expense        |
| `amount`      | Expense amount                    |

## Example

Adding two expenses:

```bash
expense-tracker.bat add --description "Lunch" --amount 20
expense-tracker.bat add --description "Dinner" --amount 10
```

Listing the expenses:

```bash
expense-tracker.bat list
```

Example output:

```text
+-----+---------------------+--------------------------------+--------------+
| ID  | DATE                | DESCRIPTION                    | AMOUNT       |
+-----+---------------------+--------------------------------+--------------+
| 1   | 2026-09-07          | lunch                          | 20.0         |
| 2   | 2026-09-07          | Dinner                         | 10.0         |
+-----+---------------------+--------------------------------+--------------+
```

Displaying the summary:

```bash
expense-tracker.bat summary
```

Example:

```text
Total expenses: $30.00
```

## Command Parsing

The `CommandHandler` is responsible for interpreting the arguments received from the command line and routing each command to the appropriate application logic.

For example:

```text
expense-tracker add --description "Lunch" --amount 20
```

is interpreted as:

```text
Command:
    add

Options:
    --description → Lunch
    --amount      → 20
```

Options are not dependent on their position, allowing commands such as:

```bash
expense-tracker add --description "Lunch" --amount 20
```

and:

```bash
expense-tracker add --amount 20 --description "Lunch"
```

to represent the same operation.

The command handler also validates invalid or incomplete arguments before passing the data to the service layer.

## Data Persistence

Expenses are persisted locally using a CSV file rather than an external database.

This keeps the application simple and follows the filesystem-oriented nature of the roadmap.sh challenge.

The CSV mapping logic is isolated in:

```text
mappers/CSVMapper.java
```

while persistence operations are handled through:

```text
repositories/ExpenseRepository.java
repositories/ExpenseRepositoryImpl.java
```

## What I Practiced

This project was built to practice several fundamental Java concepts:

- Object-Oriented Programming
- Separation of responsibilities
- Command-line argument parsing
- Input validation
- Exception handling
- Java Collections
- File I/O
- CSV data persistence
- DTOs
- Mappers
- Repository pattern
- Service layer
- Basic application architecture

## Roadmap.sh

This project is part of my implementation of projects from the [roadmap.sh](https://roadmap.sh/) learning paths.

Challenge:

[Expense Tracker](https://roadmap.sh/projects/expense-tracker)

Repository:

[Java Studies](https://github.com/evertonpontes/java-studies)
