import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    private StudentRepository repository;
    private StudentService service;
    private StudentController controller;

    @BeforeEach
    void setUp() {
        repository = new StudentRepository();
        service = new StudentService(repository);
        controller = new StudentController(service);
    }
    @Test
    void testAddStudent_EndToEnd_Positive() {
        String result = controller.handleAdd(1, "Иван", 85);
        assertTrue(result.startsWith("OK:"));
        assertNotNull(repository.findById(1));
        assertEquals("Иван", repository.findById(1).getName());
        assertEquals(85, repository.findById(1).getGrade());
    }
    @Test
    void testGetStudent_EndToEnd_Positive() {
        controller.handleAdd(2, "Мария", 90);
        String result = controller.handleGet(2);
        assertTrue(result.contains("Мария"));
        assertTrue(result.contains("90"));
    }

    @Test
    void testUpdateGrade_EndToEnd_Positive() {
        controller.handleAdd(3, "Пётр", 70);
        String result = controller.handleUpdate(3, 95);
        assertEquals("OK: оценка обновлена", result);
        assertEquals(95, repository.findById(3).getGrade());
    }

    @Test
    void testAddStudent_GradeTooLow_Negative() {
        String result = controller.handleAdd(4, "Олег", -5);
        assertTrue(result.startsWith("ERROR:"));
        assertFalse(repository.existsById(4));
    }

    @Test
    void testAddStudent_GradeTooHigh_Negative() {
        String result = controller.handleAdd(5, "Анна", 150);
        assertTrue(result.startsWith("ERROR:"));
        assertFalse(repository.existsById(5));
    }

    @Test
    void testGetStudent_NotFound_Negative() {
        String result = controller.handleGet(999);
        assertTrue(result.startsWith("ERROR:"));
        assertTrue(result.contains("не найден"));
    }

    @Test
    void testUpdateGrade_StudentNotFound_Negative() {
        String result = controller.handleUpdate(777, 50);
        assertTrue(result.startsWith("ERROR:"));
    }

    @Test
    void testUpdateGrade_InvalidGrade_Negative() {
        controller.handleAdd(6, "Сергей", 60);
        String result = controller.handleUpdate(6, 200);
        assertTrue(result.startsWith("ERROR:"));
        assertEquals(60, repository.findById(6).getGrade());
    }

    @Test
    void testController_UsesServiceMock() {
        StudentRepository realRepo = new StudentRepository();
        AtomicBoolean called = new AtomicBoolean(false);

        StudentService spyService = new StudentService(realRepo) {
            @Override
            public Student addStudent(int id, String name, int grade) {
                called.set(true);
                return super.addStudent(id, name, grade);
            }
        };

        StudentController c = new StudentController(spyService);
        c.handleAdd(10, "Тест", 50);

        assertTrue(called.get());
    }

    @Test
    void testMultipleOperations_SharedRepository() {
        controller.handleAdd(20, "A", 10);
        controller.handleAdd(21, "B", 20);
        controller.handleUpdate(20, 30);
        assertTrue(controller.handleGet(20).contains("30"));
        assertTrue(controller.handleGet(21).contains("20"));
    }

    @Test
    void testGradeBoundary_0_and_100() {
        assertTrue(controller.handleAdd(30, "Low", 0).startsWith("OK:"));
        assertTrue(controller.handleAdd(31, "High", 100).startsWith("OK:"));
    }
}