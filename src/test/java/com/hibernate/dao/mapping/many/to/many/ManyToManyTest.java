package com.hibernate.dao.mapping.many.to.many;

import com.hibernate.model.many.to.many.Product;
import com.hibernate.model.many.to.many.Supplier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Order(4)
@DisplayName("Test Many To Many Mapping")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManyToManyTest {

    SessionFactory factory;
    Session session;
    int supplierId;

    @BeforeAll
    void setup() {
        // create session factory
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Supplier.class)
                .addAnnotatedClass(Product.class)
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
    void createSupplier_ByGivenParameters_ShouldNotThrowException() {

        // Arrange
        // create a supplier
        Supplier tempSupplier = new Supplier("Snowman", "Forest", "US");

        // Act And Assert
        // Save supplier and assert that no exception happens
        assertDoesNotThrow(() -> supplierId = (int) session.save(tempSupplier),
                "Saving supplier should not throw any exception");
    }

    @Order(2)
    @Test
    // Test the same as createSupplierForProduct_UsingPreSavedProduct_ShouldNotThrowException
    void createProductForSupplier_UsingPreSavedSupplier_ShouldNotThrowException() {

        // Arrange
        Supplier tempSupplier;
        Product tempProduct1 = new Product("Banana", "Good For Heath", "10$");
        Product tempProduct2 = new Product("Cucumber", "Good For Skin", "8$");

        // Act
        // get supplier from db
        tempSupplier = session.get(Supplier.class, supplierId);
        // connect Product to supplier
        tempSupplier.addProduct(tempProduct1);
        tempSupplier.addProduct(tempProduct2);

        // Assert
        // Save products and assert that no exception happens
        assertDoesNotThrow(() -> session.save(tempProduct1),
                "Saving product1 should not throw any exception");

        assertDoesNotThrow(() -> session.save(tempProduct2),
                "Saving product2 should not throw any exception");
    }

    @Order(3)
    @Test
    void getProductsForSupplier_PredSavedSupplier_ReturnNotNullSupplierAndTwoProducts() {

        // Arrange
        int expectedProductListSize = 2;
        Supplier tempSupplier;

        // Act: get the supplier from db
        tempSupplier = session.get(Supplier.class, supplierId);

        // Assert
        assertNotNull(tempSupplier, "Supplier should be found");
        assertEquals(expectedProductListSize, tempSupplier.getProducts().size(), "Should return two products");
    }

    @Order(4)
    @Test
    void deleteSupplierAndProducts_PredSavedSupplierAndProducts_ShouldNotThrowException() {

        // Arrange
        Supplier tempSupplier;

        // Act
        tempSupplier = session.get(Supplier.class, supplierId);

        // Act And Assert
        assertDoesNotThrow(() -> {
                    tempSupplier.getProducts().forEach(item -> {
                        session.delete(item);
                    });
                    session.delete(tempSupplier);
                },
                () -> "Deleting supplier with id=" + supplierId + " should not throw any exception");

        assertNull(session.get(Supplier.class, supplierId), "Should not return any supplier");
    }
}



