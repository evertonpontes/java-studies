package taskcli;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
	
	// mutations
	
	void saveAll(List<Task> tasks);
	void save(Task task);
	void delete(int id) throws Exception;
	
	// queries
	
	List<Task> findAll();
	Optional<Task> findById(int id);
}
