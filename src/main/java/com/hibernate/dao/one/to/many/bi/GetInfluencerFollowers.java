package com.hibernate.dao.one.to.many.bi;

import com.hibernate.entity.one.to.many.Follower;
import com.hibernate.entity.one.to.many.Influencer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class GetInfluencerFollowers {

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
			
			System.out.println("Influencer: " + tempInfluencer);
			
			// get followers for the influencer
			System.out.println("Followers: " + tempInfluencer.getFollowers());
			
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





