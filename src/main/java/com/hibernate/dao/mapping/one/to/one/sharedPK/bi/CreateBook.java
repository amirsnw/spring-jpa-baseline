package com.hibernate.dao.mapping.one.to.one.sharedPK.bi;

import com.hibernate.model.one.to.one.sharedPK.bi.Book;
import com.hibernate.model.one.to.one.sharedPK.bi.Manuscript;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CreateBook {

	public static void main(String[] args) {

		// create session factory
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml") // This is the default file that hibernate search for.
								.addAnnotatedClass(Book.class)
								.addAnnotatedClass(Manuscript.class)
								.buildSessionFactory();
		
		// create session
		Session session = factory.getCurrentSession();
		
		try {			
			// create a book object
			System.out.println("Creating new book object...");
			Book tempBook = new Book("Success");
			
			// start a transaction
			session.beginTransaction();
			
			// save the book object
			System.out.println("Saving the book...");
			session.save(tempBook);
			
			// commit transaction
			session.getTransaction().commit();
			
			System.out.println("Done!");
		}
		finally {
			factory.close();
		}
	}

}





