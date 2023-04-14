package com.hibernate.entity.one.to.one;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="manager_info")
public class ManagerInfo {

	// annotate the class as an entity and map to db table
	
	// define the fields
	
	// annotate the fields with db column names
	
	// create constructors
	
	// generate getter/setter methods
	
	// generate toString() method
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="linkedin_account")
	private String linkedinAccount;
	
	@Column(name="title")
	private String title;
	
	// add new field for manager (also add getter/setters)
	
	// add @OneToOne annotation

	// Reference back to Manager
	@OneToOne(mappedBy="managerInfo",
			cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
						CascadeType.REFRESH}) // OneToOne default : FetchType.EAGER
	private Manager manager;

	public ManagerInfo() {
	}

	public ManagerInfo(String linkedinAccount, String title) {
		this.linkedinAccount = linkedinAccount;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLinkedinAccount() {
		return linkedinAccount;
	}

	public void setLinkedinAccount(String linkedinAccount) {
		this.linkedinAccount = linkedinAccount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@Override
	public String toString() {
		return "ManagerInfo [id=" + id + ", linkedin=" + linkedinAccount + ", hobby=" + title + "]";
	}
		
}







