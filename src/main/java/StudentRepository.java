import java.util.HashMap;
import java.util.Map;

public class StudentRepository {
    private Map<Integer, Student> storage = new HashMap<>();

    public void save(Student student) {
        storage.put(student.getId(), student);
    }

    public Student findById(int id) {
        return storage.get(id);
    }

    public boolean existsById(int id) {
        return storage.containsKey(id);
    }
}