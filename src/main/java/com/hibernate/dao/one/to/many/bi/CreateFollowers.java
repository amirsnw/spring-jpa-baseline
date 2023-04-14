package com.hibernate.dao.one.to.many.bi;

import com.hibernate.entity.one.to.many.Follower;
import com.hibernate.entity.one.to.many.Influencer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CreateFollowers {

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
			
			// get the influencer from db
			int theId = 67;
			Influencer tempInfluencer = session.get(Influencer.class, theId);		
			
			// create some followers
			Follower tempFollower1 = new Follower("amirsnw");
			Follower tempFollower2 = new Follower("amisnw14");
			
			// add followers to influencer
			tempInfluencer.addFollower(tempFollower1);
			tempInfluencer.addFollower(tempFollower2);
			
			// save the followers
			session.save(tempFollower1);
			session.save(tempFollower2);
			
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





