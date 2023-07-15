package com.baseline.cache.first.level;

import com.baseline.model.StudentEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Order(5)
@DisplayName("Test Many To Many Mapping")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FirstCacheTestTest {

    SessionFactory factory;
    Session session;
    int studentId;

    @BeforeAll
    void setup() {
        // create session factory
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(StudentEntity.class)
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
    void createSimpleStudent_ByGivenParameters_ShouldNotThrowException() {

        // Arrange
        // create a student
        StudentEntity tempStudent = new StudentEntity("Tianna", "Bass", "tianna@gmail.com");

        // Save student and assert that no exception happens
        assertDoesNotThrow(() -> studentId = (int) session.save(tempStudent),
                "Saving student should not throw any exception");
    }

    @Order(2)
    @Test
    void testFetchFromFirstLevelCache_ByPreSavedSimpleStudentRecord_ShouldMeetAllAssertions() {

        // Arrange
        StudentEntity student;
        StudentEntity student2;
        StudentEntity student3;
        Session newSession;

        // Act
        student = session.get(StudentEntity.class, studentId);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail("Unexpected Exception!");
        }

        // Fetch same data again, check logs that no query fired (show cached values)
        student2 = session.get(StudentEntity.class, studentId);

        // Create new session
        newSession = factory.openSession();

        // Get student with studentId, notice the logs for query (show updated values)
        student3 = newSession.get(StudentEntity.class, studentId);

        // Assert
        assertNotNull(student, "SimpleStudent should be found");
        assertEquals(student2, student);
        assertNotEquals(student3, student);
        assertNotEquals(student3, student2);
    }

    @Order(3)
    @Test
    // evict example to remove specific object from hibernate first level cache
    void testRemoveFromFirstLevelCacheByEvict_ByPreSavedSimpleStudentRecord_ShouldBeRemovedFromCache() {

        // Arrange
        StudentEntity student;

        // Act
        // Get student
        student = session.get(StudentEntity.class, studentId);

        // evict the student object with studentId
        // Note: The method evict() removes a single object from Session cache
        session.evict(student);

        // Assert
        assertFalse(session.contains(student), "Student should be removed from cache");

        // Console Assertion
        // since object is removed from first level cache, you will see query in logs
        session.get(StudentEntity.class, studentId);
    }

    @Order(4)
    @Test
        // evict example to remove specific object from hibernate first level cache
    void testRemoveAllFromFirstLevelCacheByClearingCache_ByPreSavedSimpleStudentRecord_ShouldMeetAllAssertions() {

        // Arrange
        StudentEntity student;

        // Act
        // Get student
        student = session.get(StudentEntity.class, studentId);

        // START: clear example to remove everything from first level cache
        session.clear();


        // Assert
        assertFalse(session.contains(student), "Student should be removed from cache");

        // Console Assertion
        // Shows query in console again
        session.get(StudentEntity.class, studentId);
    }

    @Order(5)
    @Test
    void deleteSimpleStudent_PredSavedSimpleStudent_ShouldNotThrowException() {

        // Arrange
        StudentEntity tempStudent;

        // Act
        tempStudent = session.get(StudentEntity.class, studentId);

        // Act And Assert
        assertDoesNotThrow(() -> session.delete(tempStudent),
                () -> "Deleting student with id=" + studentId + " should not throw any exception");

        assertNull(session.get(StudentEntity.class, studentId), "Should not return any student");
    }
}



