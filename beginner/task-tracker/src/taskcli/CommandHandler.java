package taskcli;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandHandler {
	private final int DESCRIPTION_WIDTH = 30;
	private final int STATUS_WIDTH = 15;
	private final int DATE_WIDTH = 19;
	
	private final TaskService service;
	
	public CommandHandler() {
		service = new TaskService();
	}
	
	public void handle(String[] args) {
		
		if (args.length == 0 ) {
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
		case "mark-todo":
			handleMarkTodo(args);
			break;
		case "mark-in-progress":
			handleMarkInProgress(args);
			break;
		case "mark-done":
			handleMarkDone(args);
			break;
		case "list":
			handleList(args);
			break;
		case "get":
			handleGet(args);
			break;
		default:
			System.out.println(
				"Unknowm command " + command 
			);
			
			printHelp();
		}
	}
	
	private void handleAdd(String[] args) {
		List<Task> result = new ArrayList<Task>();
		
		if (args.length != 2) {
			System.out.println(
				"Usage: taskcli add <description>"
			);
			return;
		}
		
		String description = args[1];
		
		service.create(description);
		
		List<Task> tasks = service.findAll();
		
		Task task = tasks.getLast();
		
		result.add(task);
		
		System.out.println(
			"Task created successfully: " + task.getId()
		);
		
		printTasks(result);
	}
	
	private void handleUpdate(String[] args) {
		List<Task> result = new ArrayList<Task>();
		
		if (args.length != 3) {
			System.out.println(
				"Usage: taskcli update <id> <description>"
			);
			return;
		}
		
		String idValue = args[1];
		
		if (isNumeric(idValue)) {
			
			int id = Integer.parseInt(idValue);
			
			String description = args[2];
			
			Optional<Task> task = service.findById(id);
			
			if (task.isEmpty()) {
				System.out.println("Task not found.");
			} else {
				Task taskToUpdate = task.get();
				taskToUpdate.setDescription(description);
				result.add(taskToUpdate);
				service.update(id, description);
				System.out.println(
						"Task updated successfully: " + taskToUpdate.getId()
						);
				
				printTasks(result);
			}
		} else {
			System.out.println(
					"Unknowm command " + idValue 
			);
			System.out.println(
					"Usage: taskcli update <id> <description>"
			);
		}
		
				
	}
	
	private void handleDelete(String[] args) {
		if (args.length != 2) {
			System.out.println(
				"Usage: taskcli delete <id>"
			);
			return;
		}
		
		String idValue = args[1];
		
		if (isNumeric(idValue)) {			
			int id = Integer.parseInt(idValue);
			
			service.delete(id);
		} else {
			System.out.println(
					"Unknowm command " + idValue 
			);
			System.out.println(
					"Usage: taskcli delete <id>"
			);
		}
		
	}
	
	private void handleMarkTodo(String[] args) {
		List<Task> result = new ArrayList<Task>();
		
		if (args.length != 2) {
			System.out.println(
				"Usage: taskcli mark-todo <id>"
			);
			return;
		}
		
		String idValue = args[1];
		
		if (isNumeric(idValue)) {			
			int id = Integer.parseInt(idValue);
			
			Optional<Task> task = service.findById(id);
			
			if (task.isEmpty()) {
				System.out.println("Task not found.");
			} else {
				Task taskToUpdate = task.get();
				TaskStatus status = TaskStatus.fromValue("todo");
				taskToUpdate.setStatus(status);
				result.add(taskToUpdate);
				service.updateStatus(id, "todo");
				System.out.println(
						"Task updated successfully: " + taskToUpdate.getId()
						);
				
				printTasks(result);
			}
		} else {
			System.out.println(
					"Unknowm command " + idValue 
			);
			System.out.println(
					"Usage: taskcli mark-todo <id>"
				);
		}
		
	}
	
	private void handleMarkInProgress(String[] args) {
		List<Task> result = new ArrayList<Task>();
		
		if (args.length != 2) {
			System.out.println(
				"Usage: taskcli mark-in-progress <id>"
			);
			return;
		}
		
		String idValue = args[1];
		
		if (isNumeric(idValue)) {			
			int id = Integer.parseInt(idValue);
			
			Optional<Task> task = service.findById(id);
			
			if (task.isEmpty()) {
				System.out.println("Task not found.");
			} else {
				Task taskToUpdate = task.get();
				TaskStatus status = TaskStatus.fromValue("in-progress");
				taskToUpdate.setStatus(status);
				result.add(taskToUpdate);
				service.updateStatus(id, "in-progress");
				System.out.println(
						"Task updated successfully: " + taskToUpdate.getId()
						);
				
				printTasks(result);
			}
		} else {
			System.out.println(
					"Unknowm command " + idValue 
			);
			System.out.println(
					"Usage: taskcli mark-in-progress <id>"
				);
		}
		
	}
	
	private void handleMarkDone(String[] args) {
		List<Task> result = new ArrayList<Task>();
		
		if (args.length != 2) {
			System.out.println(
				"Usage: taskcli mark-done <id>"
			);
			return;
		}
		
		String idValue = args[1];
		
		if (isNumeric(idValue)) {
			
			int id = Integer.parseInt(idValue);
			
			Optional<Task> task = service.findById(id);
			
			if (task.isEmpty()) {
				System.out.println("Task not found.");
			} else {
				Task taskToUpdate = task.get();
				TaskStatus status = TaskStatus.fromValue("done");
				taskToUpdate.setStatus(status);
				result.add(taskToUpdate);
				service.updateStatus(id, "done");
				System.out.println(
						"Task updated successfully: " + taskToUpdate.getId()
						);
				
				printTasks(result);
			}
		} else {
			System.out.println(
					"Unknowm command " + idValue 
			);
			System.out.println(
					"Usage: taskcli mark-done <id>"
				);
		}
		
	}
	
	private void handleList(String[] args) {
		
		if (args.length == 2) {
			String statusValue = args[1];
			
			if (args[1].equals("todo") | args[1].equals("in-progress") | args[1].equals("done")) {
				List<Task> tasksByStatus = service.findAllByStatus(statusValue);
				
				printTasks(tasksByStatus);
				
			} else {
				System.out.println(
						"Unknowm command " + statusValue 
					);
				
				printStatusHelp();
			}
		} else if (args.length == 1) {
			List<Task> tasks = service.findAll();
			
			printTasks(tasks);
		} else {
			System.out.println(
					"Unknowm command" 
			);
			printStatusHelp();
		}
		
	}
	
	private void handleGet(String[] args) {
		List<Task> result = new ArrayList<Task>();
		
		if (args.length != 2) {
			System.out.println(
				"Usage: taskcli get <id>"
			);
			return;
		}
		
		String idValue = args[1];
		
		if (isNumeric(idValue)) {			
			int id = Integer.parseInt(idValue);
			
			Optional<Task> taskOptional = service.findById(id);
			
			if (!taskOptional.isEmpty()) {
				Task task = taskOptional.get();
				result.add(task);
			}
			
			printTasks(result);
		} else {
			System.out.println(
					"Unknowm command " +  idValue
			);
			System.out.println(
					"Usage: taskcli get <id>"
			);
		}
		
	}
	
	private void printHelp() {
		System.out.println("""
		        Usage: taskcli <command> [arguments]

		        Commands:

		          add <description>
		              Create a new task.

		          update <id> <description>
		              Update an existing task.

		          delete <id>
		              Delete a task.

		          mark-todo <id>
		              Mark task as todo.
		              
		          mark-in-progress <id>
		              Mark task as in-progress.
		              
		          mark-done <id>
		              Mark task as done.

		          list
		              List all tasks.

		          list <status>
		              List tasks by status.

		          get <id>
		              Find a task by ID.

		        Status:
		          todo
		          in-progress
		          done
		        """);
	}
	
	private void printStatusHelp() {
		System.out.println("""
		Usage: taskcli list [arguments]
			
		Status:
		  todo
		      List all tasks marked as todo
		  in-progress
		      List all tasks marked as in progress
		  done
		      List all tasks marked ad done
		""");
	}
	
	private void printTasks(List<Task> tasks) {
		
		if (tasks.isEmpty()) {
			System.out.println("Task not found.");
			return;
		}
		
		String separator = 
				"+" + "-".repeat(5) + "+"
				+ "-".repeat(DESCRIPTION_WIDTH + 2) + "+"
				+ "-".repeat(STATUS_WIDTH + 2) + "+"
				+ "-".repeat(DATE_WIDTH + 2) + "+"
				+ "-".repeat(DATE_WIDTH + 2) + "+";
		
		System.out.println(separator);
		
		System.out.printf(
				"| %-3s | %-" + DESCRIPTION_WIDTH + "s | %-" + STATUS_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + DATE_WIDTH + "s |%n",
				"ID",
				"DESCRIPTION",
				"STATUS",
				"CREATED AT",
				"UPDATED AT"
		);
		
		System.out.println(separator);
		
		for(Task task : tasks) {
			String description = truncate(
					task.getDescription(),
					DESCRIPTION_WIDTH
			);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			String createdAtFormatted = task.getCreatedAt().format(formatter);
			
			String updatedAtFormatted = task.getUpdatedAt().format(formatter);
			
			System.out.printf(
				"| %-3s | %-" + DESCRIPTION_WIDTH + "s | %-" + STATUS_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + DATE_WIDTH + "s |%n",
				task.getId(),
				description,
				task.getStatus().getValue(),
				createdAtFormatted,
				updatedAtFormatted
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
	
	private boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
}
