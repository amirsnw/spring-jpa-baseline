package com.baseline.entity.many.to.many;

import com.baseline.config.AppConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AppConstants.TABLE_PREFIX + "supplier")
public class SupplierEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
			name=AppConstants.TABLE_PREFIX + "supplier_product",
			joinColumns = @JoinColumn(name="supplier_id"),
			inverseJoinColumns = @JoinColumn(name="product_id"))
	private List<ProductEntity> products;
}



