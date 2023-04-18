package com.hibernate.dao.mapping.one.to.one.sharedPK.bi;


import com.hibernate.model.one.to.one.sharedPK.bi.Book;
import com.hibernate.model.one.to.one.sharedPK.bi.Manuscript;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class GetBook {

	public static void main(String[] args) {

		// create session factory
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml")
								.addAnnotatedClass(Book.class)
								.addAnnotatedClass(Manuscript.class)
								.buildSessionFactory();
		
		// create session
		Session session = factory.getCurrentSession();
		
		try {
			// start a transaction
			session.beginTransaction();

			// get the manager info object
			int theId = 2;
			Book tempBook =	session.get(Book.class, theId);
			
			// print the manager info
			System.out.println("Car: " + tempBook);
						
			// print the associated manager
			System.out.println("the associated manuscript: " +
					tempBook.getManuscript());
			
			// commit transaction
			session.getTransaction().commit();
			
			System.out.println("Done!");
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			// handle connection leak issue
			session.close();
			
			factory.close();
		}
	}

}





