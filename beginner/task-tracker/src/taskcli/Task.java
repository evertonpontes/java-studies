package taskcli;
import java.time.LocalDateTime;
import java.util.List;

public class Task {
	
	// attributes
	
	private int id;
	private String description;
	private TaskStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	// constructor
	
	public Task (int id, String description, TaskStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	// getters methods
	
	public int getId() {
		return this.id;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public TaskStatus getStatus() {
		return this.status;
	}
	
	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}
	
	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}
	
	// setters methods
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
	public void setUpdatedAt(LocalDateTime localdatetime) {
		this.updatedAt = localdatetime;
	}
	
	public static int generateNextId(List<Task> tasks) {
		
		return tasks.stream()
					.mapToInt(Task::getId)
					.max()
					.orElse(0) + 1;
	}
}
