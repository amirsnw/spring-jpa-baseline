package com.hibernate.dao.mapping.cache.firstLevel;

import com.hibernate.model.simple.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveFromCache {

    private static Logger logger = LoggerFactory.getLogger(RemoveFromCache.class);

    public static void main(String[] args) throws InterruptedException {

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();

        try {
            Transaction tx = session.beginTransaction();


            // START: evict example to remove specific object from hibernate first level cache

            // Get student with id = 1
            Student student = session.load(Student.class, 1);
            logger.trace("1 :: " + student);

            // Get student with id = 2
            Student student2 = session.load(Student.class, 2);
            logger.trace("2 :: " + student2);

            // evict the student object with id = 1
            // Note: The method evict() removes a single object from Session cache
            session.evict(student);
            logger.trace("\n-- After evict()");
            logger.trace("Session Contains Student with id = 1? " + session.contains(student)); // False
            logger.trace("\n");

            // since object is removed from first level cache, you will see query in logs
            Student student3 = session.load(Student.class, 1);
            logger.trace("3 :: " + student3);

            // this object is still present, so you won't see query in logs
            Student student4 = session.load(Student.class, 2);
            logger.trace("4 :: " + student4);
            //END: evict example

            // START: clear example to remove everything from first level cache
            session.clear();
            logger.trace("\n-- After clearing cache");
            logger.trace("Session Contains Student with id = 1? " + session.contains(student3));
            logger.trace("Session Contains Student with id = 2? " + session.contains(student4));
            logger.trace("\n");

            Student student5 = session.load(Student.class, 1);
            logger.trace("5 :: " + student5);

            Student student6 = session.load(Student.class, 2);
            logger.trace("6 :: " + student6);

            logger.trace("\n");
            logger.trace("Session Contains Student with id = 1? " + session.contains(student5));
            logger.trace("Session Contains Student with id = 2? " + session.contains(student6));

            tx.commit();
        } finally {
            // add clean up code
            session.close();
            factory.close();
        }
    }
}
