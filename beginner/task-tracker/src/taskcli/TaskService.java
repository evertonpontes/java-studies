package taskcli;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TaskService {
	private TaskRepository repository;
	
	public TaskService () {
		repository = new TaskRepositoryImpl();
	}
	
	public void create(String description) {
	
		List<Task> tasks = repository.findAll();
			
		int nextId = Task.generateNextId(tasks);
			
		Task newTask = new Task(
					nextId,
					description.trim(),
					TaskStatus.TODO,
					LocalDateTime.now(),
					LocalDateTime.now()
				);
			
		repository.save(newTask);
	}
	
	public void update(int id, String description) {	
		
		Optional<Task> task = repository.findById(id);
			
		if (task.isEmpty()) {
			System.out.println("Task not found!");
			return;
		} else {
			Task taskToUpdate = task.get();
				
			taskToUpdate.setDescription(description);
			taskToUpdate.setUpdatedAt(LocalDateTime.now());
				
			repository.save(taskToUpdate);
		}
	}
	
	public void updateStatus(int id, String statusValue) {	
		
		Optional<Task> task = repository.findById(id);
			
		if (task.isEmpty()) {
			System.out.println("Task not found!");
			return;
		} else {
			Task taskToUpdate = task.get();
				
			TaskStatus status = TaskStatus.fromValue(statusValue);
				
			taskToUpdate.setStatus(status);
			taskToUpdate.setUpdatedAt(LocalDateTime.now());
				
			repository.save(taskToUpdate);
		}
	}
	
	public void delete(int id) {
		
		try {			
			repository.delete(id);
			
			System.out.println("Task deleted successfully: "+id);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public List<Task> findAll() {	
		
		return repository.findAll();
	}
	
	public List<Task> findAllByStatus(String statusValue) {	
		
		List<Task> tasks = repository.findAll();
			
		TaskStatus status = TaskStatus.fromValue(statusValue);
			
		List<Task> tasksFromStatus = tasks.stream()
				.filter(t -> t.getStatus() == status)
				.toList();
			
		return tasksFromStatus;
	}
	
	public Optional<Task> findById(int id) {
		
		Optional<Task> task = repository.findById(id);
			
		return task;
	}
}
