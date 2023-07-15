package com.hibernate.model.one.to.one.uni;

import com.hibernate.config.AppConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AppConstants.TABLE_PREFIX + "team")
public class Address {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="street")
	private String street;

	@Column(name="number")
	private String number;
}







