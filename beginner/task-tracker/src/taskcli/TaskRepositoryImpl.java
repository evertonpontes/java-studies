package taskcli;
import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {
	
	private Database db;
	private TaskJsonMapper mapper;
	
	public TaskRepositoryImpl() {
		db = new Database();
		mapper = new TaskJsonMapper();
	}

	@Override
	public void saveAll(List<Task> tasks) {
		String json = mapper.toJson(tasks);
		db.writeText(json);
	}

	@Override
	public void save(Task task) {
		List<Task> tasks = findAll();
		
		int index = -1;
		
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == task.getId()) {
				index = i;
				break;
			}
		}
		
		if (index >= 0) {
			tasks.set(index, task);
		} else {
			tasks.add(task);
		}
		
		saveAll(tasks);
	}

	@Override
	public void delete(int id) throws Exception {
		List<Task> tasks = findAll();
		
		int index = -1;
		
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == id) {
				index = i;
				break;
			}
		}
		
		if (index >= 0) {
			tasks.remove(index);
		} else {
			throw new Exception("Task not fount!");
		}
		
		saveAll(tasks);
	}

	@Override
	public List<Task> findAll() {
		String json = db.readText();
		
		return mapper.fromJson(json);
	}

	@Override
	public Optional<Task> findById(int id) {
		List<Task> tasks = findAll();
		
		Optional<Task> task = tasks.stream()
				.filter(t -> t.getId() == id)
				.findFirst();
		
		return task;
	}


}
