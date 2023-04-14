package com.hibernate.dao.one.to.many;

import com.hibernate.entity.one.to.many.Follower;
import com.hibernate.entity.one.to.many.Influencer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


public class EagerLazyHQL {

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
			
			// option 2: Hibernate query with HQL
			
			// get the influencer from db
			int theId = 1;
			Query<Influencer> query =
					session.createQuery("select i from Influencer i "
									+ "JOIN FETCH i.followers "
									+ "where i.id=:theInfluencerId",
							Influencer.class);

			// set parameter on query
			query.setParameter("theInfluencerId", theId);
			
			// execute query and get influencer
			Influencer tempInfluencer = query.getSingleResult();
			
			System.out.println("Influencer: " + tempInfluencer);
			
			// commit transaction
			session.getTransaction().commit();
			
			// close the session
			session.close();
			
			System.out.println("\nThe session is now closed!\n");
			
			// get followers for the influencer
			System.out.println("Followers: " + tempInfluencer.getFollowers());
			
			System.out.println("Done!");
		}
		finally {
			
			// add clean up code
			session.close();
			
			factory.close();
		}
	}

}





