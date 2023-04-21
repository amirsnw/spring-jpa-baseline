package com.hibernate.dao.mapping.cache.firstLevel;

import com.hibernate.model.simple.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class FetchFromCache {

    public static void main(String[] args) throws InterruptedException {

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();
        Session newSession = null;

        try {
            Transaction tx = session.beginTransaction();

            // Get student with id
            // https://www.digitalocean.com/community/tutorials/hibernate-session-get-vs-load-difference-with-examples
            Student student = session.load(Student.class, 1);
            System.out.println("1 :: " + student);

            // waiting for you to change the data in database
            Thread.sleep(10000);

            // Fetch same data again, check logs that no query fired
            Student student2 = session.load(Student.class, 1);
            System.out.println("2 :: " + student2);

            // Create new session
            newSession = factory.openSession();
            // Get student with id = 1, notice the logs for query
            Student student3 = newSession.load(Student.class, 1);
            System.out.println("3 :: " + student3);

            tx.commit();
        } finally {
            // add clean up code
            session.close();
            newSession.close();
            factory.close();
        }
    }
}