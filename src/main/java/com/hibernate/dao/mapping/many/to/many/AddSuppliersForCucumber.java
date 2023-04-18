package com.hibernate.dao.mapping.many.to.many;

import com.hibernate.model.many.to.many.Product;
import com.hibernate.model.many.to.many.Supplier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AddSuppliersForCucumber {

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
				
			// get the product cucumber from database
			int productId = 2;
			Product tempProduct = session.get(Product.class, productId);
			
			System.out.println("\nLoaded product: " + tempProduct);
			System.out.println("Suppliers: " + tempProduct.getSuppliers());
			
			// create more suppliers
			Supplier tempSupplier1 = new Supplier("Snowman", "Forest", "Iran");
			Supplier tempSupplier2 = new Supplier("Maggy", "Hills", "England");
						
			// add product to suppliers
			tempSupplier1.addProduct(tempProduct);
			tempSupplier2.addProduct(tempProduct);
						
			// save the suppliers
			System.out.println("\nSaving the suppliers ...");
			
			session.save(tempSupplier1);
			session.save(tempSupplier2);
						
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





