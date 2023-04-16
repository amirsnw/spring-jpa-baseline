package com.hibernate.dao.many.to.many;

import com.hibernate.model.many.to.many.Product;
import com.hibernate.model.many.to.many.Supplier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CreateSupplierAndProducts {

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
						
			// create a supplier
			Supplier tempSupplier = new Supplier("Snowman", "Forest", "Iran");
						
			// save the supplier
			System.out.println("\nSaving the supplier ...");
			session.save(tempSupplier);
			System.out.println("Saved the supplier: " + tempSupplier);
			
			// create the products
			Product tempProduct1 = new Product("Banana", "Good For Heath", "10$");
			Product tempProduct2 = new Product("Cucumber", "Good For Skin", "8$");
						
			// add products to the supplier
			tempSupplier.addProduct(tempProduct1);
			tempSupplier.addProduct(tempProduct2);
			
			// save the products
			System.out.println("\nSaving products ...");
			session.save(tempProduct1);
			session.save(tempProduct2);
			System.out.println("Saved products: " + tempSupplier.getProducts());
			
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





