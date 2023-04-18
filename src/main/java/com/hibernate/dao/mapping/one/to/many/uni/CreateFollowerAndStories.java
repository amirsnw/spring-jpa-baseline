package com.hibernate.dao.mapping.one.to.many.uni;

import com.hibernate.model.one.to.many.Follower;
import com.hibernate.model.one.to.many.Story;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Date;

public class CreateFollowerAndStories {

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

			// create a follower
			Follower tempFollower = new Follower("amirsnw");
			
			// add some stories
			tempFollower.addStory(new Story(new Date()));
			tempFollower.addStory(new Story(new Date()));
			tempFollower.addStory(new Story(new Date()));
						
			// save the follower ... and leverage the cascade all
			System.out.println("Saving the follower");
			System.out.println(tempFollower);
			System.out.println(tempFollower.getStories());
			
			session.save(tempFollower);
			
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





