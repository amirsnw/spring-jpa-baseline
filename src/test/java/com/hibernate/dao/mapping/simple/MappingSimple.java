package com.hibernate.dao.mapping.simple;

import com.hibernate.model.many.to.many.Product;
import com.hibernate.model.many.to.many.Supplier;
import com.hibernate.model.one.to.many.sharedPK.bi.Student;
import com.hibernate.model.simple.SimpleStudent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Order(5)
@DisplayName("Test Simple Mapping")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MappingSimple {

    SessionFactory factory;
    Session session;
    int studentId;

    @BeforeAll
    void setup() {
        // create session factory
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(SimpleStudent.class)
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
    void createStudent_ByGivenParameters_ShouldNotThrowException() {

        // create a student
        SimpleStudent tempStudent = new SimpleStudent("Amy", "Winehouse", "amy@gmail.com");

        // Save student and assert that no exception happens
        assertDoesNotThrow(() -> studentId = (int) session.save(tempStudent),
                "Saving student should not throw any exception");
    }

    @Order(2)
    @Test
    void getStudent_PredSavedStudent_ReturnNotNullStudent() {

        // Arrange
        SimpleStudent tempStudent;

        // Act: get the student from db
        tempStudent = session.get(SimpleStudent.class, studentId);

        // Assert
        assertNotNull(tempStudent, "Student should be found");
    }

    @Order(3)
    @Test
    void queryHQLStudent_PredSavedStudent_ReturnNotNullStudent() {

        // Arrange
        Query query = session.createQuery("from SimpleStudent s where s.firstName=:firstName");
        query.setParameter("firstName", "Amy");

        // Act And Assert
        assertDoesNotThrow(() -> {
                    assertTrue(query.getSingleResult() instanceof SimpleStudent);
                },
                () -> "student with first-name 'Amy' should be found");;
    }

    /*@Order(4)
    @Test
    void updateStudent_PredSavedStudent_ShouldNotThrowException() {

        // Arrange
        SimpleStudent tempStudent;

        // Act: get the student from db
        tempStudent = session.get(SimpleStudent.class, studentId);

        // Act And Assert
        assertDoesNotThrow(() -> {
                    assertTrue(query.getSingleResult() instanceof SimpleStudent);
                },
                () -> "student with first-name 'Amy' should be found");;
    }

    @Order(4)
    @Test
    void deleteSupplierAndProducts_PredSavedSupplierAndProducts_ShouldNotThrowException() {

        // Arrange
        final Supplier tempSupplier;

        // Act
        tempSupplier = session.get(Supplier.class, supplierId);

        // Act And Assert
        assertDoesNotThrow(() -> {
                    tempSupplier.getProducts().forEach(item -> {
                        session.delete(item);
                    });
                    session.delete(tempSupplier);
                },
                () -> "Deleting supplier with id=" + supplierId + "should not throw any exception");

        assertNull(session.get(Supplier.class, supplierId), "Should not return any supplierId");
    }*/
}



