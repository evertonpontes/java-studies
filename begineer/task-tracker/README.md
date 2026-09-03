# Task CLI

A simple command-line task management application built with **Java**. The project allows users to create, update, delete, and organize tasks directly from the terminal.

Tasks are persisted locally in a `tasks.json` file, so the data remains available between executions.

The project was built without external libraries, using Java's standard APIs for file handling, JSON serialization/deserialization, and command-line interaction.

## Features

- Create new tasks
- Update task descriptions
- Delete tasks
- Update task status
- List all tasks
- Filter tasks by status
- Find a task by ID
- Automatic task ID generation
- Automatic creation of `tasks.json`
- Persistent local storage
- Formatted table output in the terminal
- Long descriptions are automatically truncated to preserve the table layout

### Task Statuses

Tasks can have one of three statuses:

- `todo`
- `in-progress`
- `done`

## Tech Stack

- Java
- Java Collections
- Java NIO
- `LocalDateTime`
- JSON
- Command-line interface (CLI)

No external dependencies are required.

## Project Structure

```text
src/
└── taskcli/
    ├── Main.java
    ├── Task.java
    ├── TaskJsonMapper.java
    ├── Database.java
    ├── TaskRepository.java
    ├── TaskRepositoryImpl.java
    ├── TaskService.java
    └── TaskStatus.java
```

After building the application, the distributable files can be organized as:

```text
task-tracker/
├── taskcli.jar
└── taskcli.bat
```

## Requirements

Before installing the application, make sure you have the **Java Development Kit (JDK)** installed.

You can verify your Java installation with:

```bash
java -version
```

The project uses modern Java syntax, so a recent JDK version is recommended.

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/evertonpontes/task-cli.git
```

Navigate to the project directory:

```bash
cd task-cli
```

### 2. Compile the project

On Windows:

```cmd
javac -d out src\taskcli\*.java
```

### 3. Create the JAR file

```cmd
jar cfe taskcli.jar taskcli.Main -C out .
```

This creates:

```text
taskcli.jar
```

### 4. Create the Windows launcher

Create a file named:

```text
taskcli.bat
```

with the following content:

```bat
@echo off
java -jar "%~dp0taskcli.jar" %*
```

Place the `.bat` file in the same directory as the JAR:

```text
task-tracker/
├── taskcli.jar
└── taskcli.bat
```

You can now run the application with:

```cmd
taskcli.bat list
```

If the directory containing `taskcli.bat` is added to the Windows `PATH`, the command can be used directly:

```cmd
taskcli list
```

## Usage

The general command format is:

```text
taskcli <command> [arguments]
```

### Add a task

Create a new task:

```cmd
taskcli add "Buy milk"
```

Example output:

```text
Task created successfully: 1
```

### Update a task

Update the description of an existing task:

```cmd
taskcli update 1 "Buy milk and bread"
```

### Delete a task

Delete a task using its ID:

```cmd
taskcli delete 1
```

### Update task status

Change the status of a task:

```cmd
taskcli status 1 in-progress
```

Available statuses:

```text
todo
in-progress
done
```

For example:

```cmd
taskcli status 1 done
```

### List all tasks

```cmd
taskcli list
```

Example:

```text
+-----+--------------------------------+-----------------+---------------------+---------------------+
| ID  | DESCRIPTION                    | STATUS          | CREATED AT          | UPDATED AT          |
+-----+--------------------------------+-----------------+---------------------+---------------------+
| 1   | Buy milk                       | todo            | 2026-09-02 18:10:00 | 2026-09-02 18:10:00 |
| 2   | Study Java                     | in-progress     | 2026-09-02 18:15:00 | 2026-09-02 18:20:00 |
| 3   | Finish CLI project             | done            | 2026-09-02 18:30:00 | 2026-09-02 19:10:00 |
+-----+--------------------------------+-----------------+---------------------+---------------------+
```

### List tasks by status

You can filter tasks by status:

```cmd
taskcli list todo
```

```cmd
taskcli list in-progress
```

```cmd
taskcli list done
```

### Find a task by ID

```cmd
taskcli get 1
```

### Display available commands

Running the application without arguments displays the available commands:

```cmd
taskcli
```

## Data Persistence

Tasks are stored in a local `tasks.json` file.

The file is automatically created when the application runs for the first time.

Example:

```json
{
  "tasks": [
    {
      "id": 1,
      "description": "Buy milk",
      "status": "todo",
      "createdAt": "2026-09-02T18:10:00",
      "updatedAt": "2026-09-02T18:10:00"
    }
  ]
}
```

The application uses a **read-modify-write** approach for persistence:

1. Read the existing JSON file.
2. Deserialize it into Java objects.
3. Perform the requested operation.
4. Serialize the updated task list.
5. Write the complete JSON back to the file.

This approach keeps the implementation simple and avoids the need for an external database.

## Architecture

The application follows a layered architecture:

```text
Command Line
     │
     ▼
CommandHandler
     │
     ▼
TaskService
     │
     ▼
TaskRepository
     │
     ▼
TaskJsonMapper
     │
     ▼
Database
     │
     ▼
tasks.json
```

### CommandHandler

Responsible for:

- Parsing command-line arguments
- Validating command input
- Calling the appropriate service methods
- Displaying results and errors

### TaskService

Contains the application's business logic, including:

- Creating tasks
- Generating IDs
- Updating tasks
- Deleting tasks
- Updating task statuses
- Searching and filtering tasks

### TaskRepository

Responsible for data persistence:

- Reading tasks from `tasks.json`
- Saving tasks
- Updating the JSON file

### TaskJsonMapper

Responsible for converting between:

```text
Java objects ↔ JSON
```

The project intentionally implements JSON mapping without third-party libraries as a learning exercise.

## License

This project is available for educational and personal use.
