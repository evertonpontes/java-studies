package taskcli;
public class Main {
	
	public static void main(String[] args) {
		
		CommandHandler handler = new CommandHandler();
		
		handler.handle(args);
	}
}
