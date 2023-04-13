package com.hibernate.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Influencer")
public class Influencer {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;

	@Column(name="channel")
	private String channel;
	
	@OneToMany(mappedBy="Influencer",
			   cascade= {CascadeType.PERSIST, CascadeType.MERGE,
						 CascadeType.DETACH, CascadeType.REFRESH})
	private List<Follower> followers;
	
	
	public Influencer() {
		
	}

	public Influencer(String firstName, String lastName, String channel) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.channel = channel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Follower> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Follower> followers) {
		this.followers = followers;
	}

	@Override
	public String toString() {
		return "Influencer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", channel=" + channel
				+ "]";
	}

	// add convenience methods for bi-directional relationship
	
	public void add(Follower follower) {
		
		if (follower == null) {
			followers = new ArrayList<>();
		}

		followers.add(follower);

		follower.setInstructor(this);
	}
	
}











