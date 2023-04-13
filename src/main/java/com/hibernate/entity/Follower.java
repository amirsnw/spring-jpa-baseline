package com.hibernate.entity;

import javax.persistence.*;

@Entity
@Table(name="follower")
public class Follower {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "follower_sequence")
	@SequenceGenerator(name = "follower_sequence", sequenceName = "FLW_SEQ", allocationSize = 1)
	@Column(name="id")
	private int id;
	
	@Column(name="username")
	private String username;
	
	@ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
						 CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name="influencer_id")
	private Influencer influencer;
	
	public Follower() {
	}

	public Follower(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Influencer getInfluencer() {
		return influencer;
	}

	public void setInfluencer(Influencer influencer) {
		this.influencer = influencer;
	}

	@Override
	public String toString() {
		return "Follower [id=" + id + ", username=" + username + "]";
	}
	
	
}
