package com.hibernate.dao.mapping.many.to.many;

import com.hibernate.model.many.to.many.Product;
import com.hibernate.model.many.to.many.Supplier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DeleteSnowmanSupplier {

	public static void main(String[] args) {

		// create session factory
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml")
								.addAnnotatedClass(Supplier.class)
								.addAnnotatedClass(Product.class)
								.buildSessionFactory();
		
		// create session
		Session session = factory.getCurrentSession();
		
		try {			
			
			// start a transaction
			session.beginTransaction();

			// get the snowman supplier from db
			int supplierId = 10;
			Supplier tempSupplier = session.get(Supplier.class, supplierId);
			
			// delete the supplier
			System.out.println("Deleting supplier: " + tempSupplier);
			
			session.delete(tempSupplier);

			// Products still exist
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





