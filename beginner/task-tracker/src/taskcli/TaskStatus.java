package taskcli;

public enum TaskStatus {
	
	TODO("todo"),
	IN_PROGRESS("in-progress"),
	DONE("done");
	
	private final String value;
	
	TaskStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static TaskStatus fromValue(String value) {
		
		return switch (value) {
		case "todo" -> TODO;
		case "in-progress" -> IN_PROGRESS;
		case "done" -> DONE;
		
		default -> throw new IllegalArgumentException("Invalid status: "+value);
		};
	}
}
