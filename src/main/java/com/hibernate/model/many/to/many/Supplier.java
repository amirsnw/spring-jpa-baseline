package com.hibernate.model.many.to.many;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="supplier")
public class Supplier {

	// define our fields
	
	// define constructors
	
	// define getter setters
	
	// define tostring
	
	// annotate fields
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "supplier_sequence")
	@SequenceGenerator(name = "supplier_sequence", sequenceName = "sup_seq", allocationSize = 1)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;

	@Column(name="address")
	private String address;

	@Column(name="country")
	private String country;

	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE,
			 CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="supplier_product",
			joinColumns = @JoinColumn(name="supplier_id"),
			inverseJoinColumns = @JoinColumn(name="product_id"))
	private List<Product> products;

	public Supplier() {
	}

	public Supplier(String name, String address, String country) {
		this.name = name;
		this.address = address;
		this.country = country;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	// add a convenience method

	public void addProduct(Product theProduct) {

		if (products == null) {
			products = new ArrayList<>();
		}

		products.add(theProduct);
	}

	@Override
	public String toString() {
		return "Supplier [" +
				"id=" + id +
				", name=" + name +
				", address=" + address +
				", country=" + country + "]";
	}
}



