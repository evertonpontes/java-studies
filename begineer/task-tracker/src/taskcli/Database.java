package taskcli;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Database {
	private final Path filePath = Path.of("tasks.json");
	
	public Database() {
		try {
			if (Files.notExists(filePath)) {
				Files.writeString(
						filePath, 
						"{ \"tasks\": [] }"
				);
			}
		} catch (IOException e) {
			System.out.println("An error occurred whike creating file.");
			e.printStackTrace();
		}
	}
	
	public void writeText(String text) {
		try {
			Files.writeString(filePath, text);
		} catch (IOException e) {
			System.out.println("An error occurred while writing on file.");
			e.printStackTrace();
		}
	}
	
	public String readText() {
		try {
			String text = Files.readString(filePath);
			return text;
		} catch (IOException e) {
			System.out.println("An error occurred while reading file.");
			return null;
		}
	}
	
	public void deleteDatabase() {
		try {
			Files.delete(filePath);
		} catch (IOException e) {
			System.out.println("An error occurred while deleting file.");
			e.printStackTrace();
		}
	}
}
