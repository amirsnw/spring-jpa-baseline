package com.baseline.hibernateCache;

import com.baseline.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test First Level Cache")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FirstLevelCacheTest {

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
        StudentEntity tempStudentEntity = new StudentEntity("Tianna", "Bass", "tianna@gmail.com");

        // Save student and assert that no exception happens
        assertDoesNotThrow(() -> studentId = (int) session.save(tempStudentEntity),
                "Saving student should not throw any exception");
    }

    @Order(2)
    @Test
    void testFetchFromFirstLevelCache_ByPreSavedSimpleStudentRecord_ShouldMeetAllAssertions() {

        // Arrange
        StudentEntity studentEntity;
        StudentEntity studentEntity2;
        StudentEntity studentEntity3;
        Session newSession;

        // Act
        studentEntity = session.get(StudentEntity.class, studentId);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail("Unexpected Exception!");
        }

        // Fetch same data again, check logs that no query fired (show cached values)
        studentEntity2 = session.get(StudentEntity.class, studentId);

        // Create new session
        newSession = factory.openSession();

        // Get student with studentId, notice the logs for query (show updated values)
        studentEntity3 = newSession.get(StudentEntity.class, studentId);

        // Assert
        assertNotNull(studentEntity, "SimpleStudent should be found");
        assertEquals(studentEntity2, studentEntity);
        assertNotEquals(studentEntity3, studentEntity);
        assertNotEquals(studentEntity3, studentEntity2);
    }

    @Order(3)
    @Test
        // evict example to remove specific object from hibernate first level cache
    void testRemoveFromFirstLevelCacheByEvict_ByPreSavedSimpleStudentRecord_ShouldBeRemovedFromCache() {

        // Arrange
        StudentEntity studentEntity;

        // Act
        // Get student
        studentEntity = session.get(StudentEntity.class, studentId);

        // evict the student object with studentId
        // Note: The method evict() removes a single object from Session cache
        session.evict(studentEntity);

        // Assert
        assertFalse(session.contains(studentEntity), "Student should be removed from cache");

        // Console Assertion
        // since object is removed from first level cache, you will see query in logs
        session.get(StudentEntity.class, studentId);
    }

    @Order(4)
    @Test
        // evict example to remove specific object from hibernate first level cache
    void testRemoveAllFromFirstLevelCacheByClearingCache_ByPreSavedSimpleStudentRecord_ShouldMeetAllAssertions() {

        // Arrange
        StudentEntity studentEntity;

        // Act
        // Get student
        studentEntity = session.get(StudentEntity.class, studentId);

        // START: clear example to remove everything from first level cache
        session.clear();


        // Assert
        assertFalse(session.contains(studentEntity), "Student should be removed from cache");

        // Console Assertion
        // Shows query in console again
        session.get(StudentEntity.class, studentId);
    }

    @Order(5)
    @Test
    void deleteSimpleStudent_PredSavedSimpleStudent_ShouldNotThrowException() {

        // Arrange
        StudentEntity tempStudentEntity;

        // Act
        tempStudentEntity = session.get(StudentEntity.class, studentId);

        // Act And Assert
        assertDoesNotThrow(() -> session.delete(tempStudentEntity),
                () -> "Deleting student with id=" + studentId + " should not throw any exception");

        assertNull(session.get(StudentEntity.class, studentId), "Should not return any student");
    }
}



