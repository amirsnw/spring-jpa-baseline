package com.hibernate.dao.one.to.one.bi;


import com.hibernate.entity.one.to.one.Manager;
import com.hibernate.entity.one.to.one.ManagerInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DeleteManager {

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
			
			// start a transaction
			session.beginTransaction();

			// get manager by primary key / id
			int theId = 1;
			Manager tempManager =
					session.get(Manager.class, theId);
			
			System.out.println("Found manager: " + tempManager);
			
			// delete the managers
			if (tempManager != null) {
			
				System.out.println("Deleting: " + tempManager);
				
				// Note: will ALSO delete associated "infos" object
				// because of CascadeType.ALL
				//
				session.delete(tempManager);
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





