package com.hibernate.dao.one.to.many.bi;

import com.hibernate.entity.one.to.many.Follower;
import com.hibernate.entity.one.to.many.Influencer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DeleteFollower {

	public static void main(String[] args) {

		// create session factory
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml")
								.addAnnotatedClass(Influencer.class)
								.addAnnotatedClass(Follower.class)
								.buildSessionFactory();
		
		// create session
		Session session = factory.getCurrentSession();
		
		try {			
			
			// start a transaction
			session.beginTransaction();
			
			// get a follower
			int theId = 10;
			Follower tempFollower = session.get(Follower.class, theId);
			
			// delete follower
			System.out.println("Deleting follower: " + tempFollower);
			
			session.delete(tempFollower);

			// commit transaction
			session.getTransaction().commit();
			
			System.out.println("Done!");
		}
		finally {
			
			// add clean up code
			session.close();
			
			factory.close();
		}
	}

}





