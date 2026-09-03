package taskcli;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskJsonMapper {
	
	// convert list of tasks to json format
	public String toJson(List<Task> tasks) {
		StringBuilder json = new StringBuilder();
		
		json.append("{\n");
		json.append(" \"tasks\": [\n");
		
		for (int i = 0; i < tasks.size(); i++) {
			
			Task task = tasks.get(i);
			
			json.append("   {\n");
			json.append("    \"id\": ")
				.append(task.getId())
				.append(",\n");
			
			json.append("    \"description\": \"")
				.append(this.escapeJson(task.getDescription()))
				.append("\",\n");
			
			json.append("    \"status\": \"")
				.append(task.getStatus().getValue())
				.append("\",\n");

			json.append("    \"createdAt\": \"")
				.append(task.getCreatedAt())
				.append("\",\n");

			json.append("    \"updatedAt\": \"")
				.append(task.getUpdatedAt())
				.append("\"\n");
			
			json.append("   }");
			
			if (i < tasks.size() - 1) {
				json.append(",");
			}
			
			json.append("\n");
			
		}
		
		json.append("  ]\n");
		json.append("}");
		
		return json.toString();
	}
	
	// convert json format to task list
	public List<Task> fromJson(String json) {
		
		String tasksJson = extractTaskArray(json);
		
		List<String> objects = extractObjects(tasksJson);
		
		List<Task> tasks = new ArrayList<Task>();
		
		for (String object : objects) {
			
			Task task = parseTask(object);
			
			tasks.add(task);
		}
		
		return tasks;
	}
	
	public String escapeJson(String value) {
		return value
				.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\n", "\\n")
				.replace("\r", "\\r")
				.replace("\t", "\\t");
	}
	
	public String unescapeJson(String value) {
		return value
				.replace("\\\\", "\\")
				.replace("\\\"", "\"")
				.replace("\\n", "\n")
				.replace("\\r", "\r")
				.replace("\\t", "\t");
	}
	
	// convert json to task
	private Task parseTask(String json) {
		
		int id = extractId(json);
		
		String description = extractString(json, "description");
		
		String statusValue = extractString(json, "status");
		
		TaskStatus status = TaskStatus.fromValue(statusValue);
		
		String createdAtValue = extractString(json, "createdAt");
		
		LocalDateTime createdAt = LocalDateTime.parse(createdAtValue);
		
		String updatedAtValue = extractString(json, "updatedAt");
		
		LocalDateTime updatedAt = LocalDateTime.parse(updatedAtValue);
		
		return new Task(
			id,
			description,
			status,
			createdAt,
			updatedAt
		);
	}
	
	private String extractTaskArray(String json) {
		
		int start = json.indexOf("[");
		int end = json.indexOf("]");
		
		if (start == -1 || end == -1) {
			throw new IllegalArgumentException("Invalid JSON");
		}
		
		return json.substring(start + 1, end);
	}
	
	private List<String> extractObjects(String json) {
		
		List<String> objects = new ArrayList<>();
		
		int depth = 0;
		int start = -1;
		
		boolean insideString = false;
		boolean escaped = false;
		
		for (int i = 0; i < json.length(); i++) {
			
			char c = json.charAt(i);
			
			if (escaped) {
				escaped = false;
				continue;
			}
			
			if (c == '\\' && insideString) {
				escaped = true;
				continue;
			}
			
			if (c == '"') {
				insideString = !insideString;
			}
			
			if (insideString) {
				continue;
			}
			
			if (c == '{') {
				
				if (depth == 0) {
					start = i;
				}
				
				depth++;
			}
			
			else if (c == '}') {
				
				depth--;
				
				if (depth == 0) {
					objects.add(
						json.substring(start, i + 1)
					);
				}
			}
		}
		
		return objects;
	}
	
	private int extractId(String json) {
		String key = "\"id\"";
		
		int keyPosition = json.indexOf(key);
		
		if (keyPosition == -1) {
			throw new IllegalArgumentException("Missing id");
		}
		
		int colonPosition = json.indexOf(
			":",
			keyPosition
		);
		
		int commaPosition = json.indexOf(
			",",
			colonPosition
		);
		
		String value = json.substring(
			colonPosition + 1,
			commaPosition
		).trim();
	
		return Integer.parseInt(value);
	}
	
	private String extractString(String json, String field) {
		
		String key = "\"" + field + "\"";
		
		int keyPosition = json.indexOf(key);
		
		if (keyPosition == -1) {
			throw new IllegalArgumentException("Missing field: "+field);
		}
		
		int colonPosition = json.indexOf(
			":",
			keyPosition
		);
		
		int startQuote = json.indexOf(
			"\"",
			colonPosition
		);
		
		int endQuote = findClosingQuote(
			json,
			startQuote + 1
		);
		
		String value = json.substring(startQuote + 1, endQuote);
		
		return unescapeJson(value);
	}
	
	private int findClosingQuote(String json, int start) {
		boolean escaped = false;
		
		for (int i = start; i < json.length(); i++) {
			char c = json.charAt(i);
			
			if (escaped) {
				escaped = false;
				continue;
			}
			
			if (c == '\\') {
				escaped = true;
				continue;
			}
			
			if (c == '"') {
				return i;
			}
		}
		
		throw new IllegalArgumentException("Unterminated JSON string");
	}
}
