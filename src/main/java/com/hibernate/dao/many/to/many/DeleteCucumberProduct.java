package com.hibernate.dao.many.to.many;

import com.hibernate.entity.many.to.many.Product;
import com.hibernate.entity.many.to.many.Supplier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DeleteCucumberProduct {

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
				
			// get the product from database
			int productId = 2;
			Product tempProduct = session.get(Product.class, productId);
			
			System.out.println("\nLoaded product: " + tempProduct);
			System.out.println("Suppliers: " + tempProduct.getSuppliers());
		
			// delete product
			System.out.println("\nDeleting product: " + tempProduct);
			session.delete(tempProduct);

			// Suppliers still exist
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





