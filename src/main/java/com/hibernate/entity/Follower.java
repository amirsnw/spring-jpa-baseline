package com.hibernate.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
						 CascadeType.DETACH, CascadeType.REFRESH}) // ManyToOne default : FetchType.EAGER
	@JoinColumn(name="influencer_id")
	private Influencer influencer;

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL) // OneToMany default : FetchType.LAZY
	@JoinColumn(name="follower_id")
	private List<Story> stories;
	
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

	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}

	@Override
	public String toString() {
		return "Follower [id=" + id + ", username=" + username + "]";
	}

	// add a convenience method

	public void addStory(Story theStory) {

		if (stories == null) {
			stories = new ArrayList<>();
		}

		stories.add(theStory);
	}
}
