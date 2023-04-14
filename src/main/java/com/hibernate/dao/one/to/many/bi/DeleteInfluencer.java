package com.hibernate.dao.one.to.many.bi;


import com.hibernate.entity.one.to.many.Influencer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DeleteInfluencer {

	public static void main(String[] args) {

		// create session factory
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml")
								.addAnnotatedClass(Influencer.class)
								.buildSessionFactory();
		
		// create session
		Session session = factory.getCurrentSession();
		
		try {			
			
			// start a transaction
			session.beginTransaction();

			// get influencer by primary key / id
			int theId = 1;
			Influencer tempInfluencer = 
					session.get(Influencer.class, theId);
			
			System.out.println("Found influencer: " + tempInfluencer);
			
			// delete the influencer
			if (tempInfluencer != null) {
			
				System.out.println("Deleting: " + tempInfluencer);
				
				session.delete(tempInfluencer);
			}
			
			// commit transaction
			session.getTransaction().commit();
			
			System.out.println("Done!");
		}
		finally {
			factory.close();
		}
	}

}





