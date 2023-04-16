package com.hibernate.dao.one.to.one.uni;

import com.hibernate.entity.one.to.one.Manager;
import com.hibernate.entity.one.to.one.Team;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CreateManagerAndTeam {

	public static void main(String[] args) {

		// create session factory
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml")
								.addAnnotatedClass(Manager.class)
								.addAnnotatedClass(Team.class)
								.buildSessionFactory();
		
		// create session
		Session session = factory.getCurrentSession();
		
		try {			
			Manager tempManager =
					new Manager("Amir", "Khalighi", "amirsnw@gmail.com");
			
			Team tempTeam =
					new Team("Modern Systems");
			
			// associate the objects
			tempManager.setTeam(tempTeam);
			
			// start a transaction
			session.beginTransaction();
			
			// save the manager
			//
			// Note: this will ALSO save the infos object
			// because of CascadeType.ALL
			//
			System.out.println("Saving manager: " + tempManager);
			session.save(tempManager);					
			
			// commit transaction
			session.getTransaction().commit();
			
			System.out.println("Done!");
		}
		finally {
			factory.close();
		}
	}

}





