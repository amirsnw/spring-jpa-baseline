package com.baseline.propagation;

import com.baseline.common.dto.SearchDto;
import com.baseline.common.model.SearchResultModel;
import com.baseline.entity.StudentEntity;
import com.baseline.repository.StudentRepository;
import com.baseline.service.StudentService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test Propagation")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class PropagationTest {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    void startTransaction() {
        resetEntityTable();
    }

    @AfterEach
    void closeTransaction() {
        resetEntityTable();
    }

    @Test
    @SneakyThrows
    void testPropagationREQUIRED() {

        // Arrange
        StudentEntity student = StudentEntity.builder().firstName("Amir").lastName("Khalighi").build();

        // Act
        studentService.saveWithREQUIRED(student);

        // Assert
        assertEquals(0, studentRepository.findAll().size());
    }

    @Test
    @SneakyThrows
    void testPropagationREQUIRED_NEW() {

        // Arrange
        StudentEntity student = StudentEntity.builder().firstName("Amir").lastName("Khalighi").build();

        // Act
        studentService.saveWithREQUIRES_NEW(student);

        // Assert
        assertEquals(1, studentRepository.findAll().size());
    }

    /*@Transactional(propagation=Propagation.REQUIRED)
    public Set<StudentEntity> testRequired(StudentEntity student) {
        Set<StudentEntity> students = new HashSet<>();
        students.add(studentService.save(student));
        students.add(studentService.saveWithREQUIRED());
        return students;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public Set<StudentEntity> testRequiresNew(StudentEntity student) {
        Set<StudentEntity> students = new HashSet<>();
        students.add(studentService.save(student));
        students.add(studentService.saveWithREQUIRES_NEW());
        return students;
    }*/

    private void resetEntityTable() {
        SearchResultModel<StudentEntity> entityList = studentService
                .search(SearchDto.builder().size(10).skipCount(true).build());
        entityList.getResult().forEach(item -> studentService.delete(item.getId(), true));
    }
}



