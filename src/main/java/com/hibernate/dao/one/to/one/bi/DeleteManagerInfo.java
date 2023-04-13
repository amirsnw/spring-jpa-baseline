package com.hibernate.dao.one.to.one.bi;


import com.hibernate.entity.Manager;
import com.hibernate.entity.ManagerInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DeleteManagerInfo {

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

			// get the manager info object
			int theId = 3;
			ManagerInfo tempManagerInfo =
					session.get(ManagerInfo.class, theId);
			
			// print the manager info
			System.out.println("tempManagerInfo: " + tempManagerInfo);
						
			// print  the associated manager
			System.out.println("the associated manager: " +
								tempManagerInfo.getManager());
			
			// now let's delete the manager info
			System.out.println("Deleting tempManagerInfo: "
											+ tempManagerInfo);

			// remove the associated object reference
			// break bi-directional link
			
			tempManagerInfo.getManager().setManagerInfo(null);
			
			session.delete(tempManagerInfo);
			
			// commit transaction
			session.getTransaction().commit();
			
			System.out.println("Done!");
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			// handle connection leak issue
			session.close();
			
			factory.close();
		}
	}

}





