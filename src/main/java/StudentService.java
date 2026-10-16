public class StudentService {
    private StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student addStudent(int id, String name, int grade) {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Оценка вне диапазона");
        }
        Student student = new Student(id, name, grade);
        repository.save(student);
        return student;
    }

    public Student getStudent(int id) {
        Student s = repository.findById(id);
        if (s == null) {
            throw new RuntimeException("Студент не найден");
        }
        return s;
    }

    public void updateGrade(int id, int newGrade) {
        Student s = getStudent(id);
        if (newGrade < 0 || newGrade > 100) {
            throw new IllegalArgumentException("Оценка вне диапазона");
        }
        s.setGrade(newGrade);
    }
}