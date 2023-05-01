package com.hibernate.dao.mapping.one.to.many.sharedPK.bi;

import com.hibernate.model.one.to.many.sharedPK.bi.Student;
import com.hibernate.model.one.to.many.sharedPK.bi.Teacher;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Order(3)
@DisplayName("Test One To Many Bidirectional Mapping by Shared Primary Key")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MappingOneToManySharedPkBi {

    SessionFactory factory;
    Session session;
    int teacherId;

    // TODO: There are plenty of other sad ending scenarios ...

    @BeforeAll
    void setup() {
        // create session factory
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Teacher.class)
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();
    }

    @BeforeEach
    void startTransaction() {
        // create session
        session = factory.getCurrentSession();

        // start a transaction
        session.beginTransaction();
    }

    @AfterEach
    void closeTransaction() {
        // Close transaction
        session.getTransaction().commit();
        // Close session
        session.close();
    }

    @AfterAll
    void finished() {
        // clean it up
        factory.close();
    }

    @Order(1)
    @Test
    void createTeacher_ByGivenParameters_ShouldNotThrowException() {

        // create the objects
        Teacher tempTeacher =
                new Teacher("Rudy Lowe");

        // Save teacher and assert that no exception happens
        assertDoesNotThrow(() -> teacherId = (int) session.save(tempTeacher),
                "Saving teacher should not throw any exception");
    }

    @Order(2)
    @Test
    void addStudentsToTeacher_UsingPreSavedTeacher_ShouldNotThrowException() {

        // Arrange
        Teacher tempTeacher;
        Student tempStudent1 = new Student("Nataly Vaughn", 44);
        Student tempStudent2 = new Student("Marc Boyd", 30);

        // Act
        // get teacher from db
        tempTeacher = session.get(Teacher.class, teacherId);
        // add students to teacher
        tempTeacher.addStudent(tempStudent1);
        tempTeacher.addStudent(tempStudent2);

        // Save student and assert that no exception happens
        assertDoesNotThrow(() -> session.save(tempStudent1),
                "Saving student1 should not throw any exception");

        assertDoesNotThrow(() -> session.save(tempStudent2),
                "Saving student2 should not throw any exception");
    }

    @Order(3)
    @Test
    void getTeacherAndStudents_PredSavedRecords_ReturnNotNullTeacherAndTwoStudents() {

        // Arrange
        int expectedStudentListSize = 2;
        Teacher tempTeacher;

        // Act: get the teacher from db
        tempTeacher = session.get(Teacher.class, teacherId);

        // Assert
        assertNotNull(tempTeacher, "Teacher should be found");
        assertEquals(expectedStudentListSize, tempTeacher.getStudents().size(), "Should return two student");
    }

    @Order(4)
    @Test
    void deleteTeacher_WhenHasStudentsHQL_ShowThrowConstraintViolationException() {

        // Arrange
        Query query = session.createQuery("delete Teacher where id > :teacherId");
        query.setParameter("teacherId", teacherId);
        int result;

        // Act
        result = query.executeUpdate();

        if (result > 0) {
            System.out.println("Expensive products was removed");
        }

        assertFalse(result > 0,
                "Teacher can not be removed while there are students connected to it");
    }

    /*@Order(5)
    @Test
    void deleteTeacher_WhenHasStudents_ShowThrowConstraintViolationException() {

        // Arrange
        final Teacher tempTeacher = new Teacher(teacherId);
        tempTeacher.setTeacherId(teacherId);

        // Arrange and Act: delete the teacher (Cascade.All = true)
        assertThrows(Error.class, () -> session.delete(tempTeacher),
                () -> "Teacher can not be removed while there are students connected to it");
    }*/

    @Order(5)
    @Test
    void deleteTeacher_AndDeleteStudents_ShouldThrowsNoException() {

        // Arrange
        final Teacher tempTeacher;

        // Act
        tempTeacher = session.get(Teacher.class, teacherId);

        // Act And Assert
        assertDoesNotThrow(() -> {
                    tempTeacher.getStudents().forEach(item -> {
                        session.delete(item);
                    });
                    session.delete(tempTeacher);
                },
                () -> "Deleting teacher with id=" + teacherId + "should not throw any exception");

        assertNull(session.get(Teacher.class, teacherId), "Should not return any teacher");
    }
}



