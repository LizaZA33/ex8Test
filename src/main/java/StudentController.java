public class StudentController {
    private StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    public String handleAdd(int id, String name, int grade) {
        try {
            Student s = service.addStudent(id, name, grade);
            return "OK: " + s;
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    public String handleGet(int id) {
        try {
            Student s = service.getStudent(id);
            return "OK: " + s;
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    public String handleUpdate(int id, int grade) {
        try {
            service.updateGrade(id, grade);
            return "OK: оценка обновлена";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}