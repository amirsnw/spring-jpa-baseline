package com.hibernate.dao.mapping.one.to.many.uni;

import com.hibernate.model.one.to.many.Follower;
import com.hibernate.model.one.to.many.Story;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DeleteFollowerAndStories {

	public static void main(String[] args) {

		// create session factory
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml")
								.addAnnotatedClass(Follower.class)
								.addAnnotatedClass(Story.class)
								.buildSessionFactory();
		
		// create session
		Session session = factory.getCurrentSession();
		
		try {			
			
			// start a transaction
			session.beginTransaction();

			// get the follower
			int theId = 10;
			Follower tempFollower = session.get(Follower.class, theId);
			
			// print the follower
			System.out.println("Deleting the follower ... ");
			System.out.println(tempFollower);
			
			// print the follower stories
			System.out.println(tempFollower.getStories());
			
			// delete the follower
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





