package com.hibernate.dao.one.to.one.bi;

import com.hibernate.entity.Manager;
import com.hibernate.entity.ManagerInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CreateManager {

	public static void main(String[] args) {

		// create session factory
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml")
								.addAnnotatedClass(Manager.class)
								.addAnnotatedClass(ManagerInfo.class)
								.buildSessionFactory();
		
		// create session
		Session session = factory.getCurrentSession();
		
		try {			
			
			// create the objects
			Manager tempManager =
					new Manager("Amir", "Khalighi", "amirsnw@gmail.com");
			
			ManagerInfo tempManagerInfo =
					new ManagerInfo(
							"https://www.linkedin.com/in/amirsnw",
							"Software Engineer");
			
			// associate the objects
			tempManager.setManagerInfo(tempManagerInfo);
			
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





