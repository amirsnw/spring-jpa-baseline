package com.hibernate.entity.one.to.one;

import javax.persistence.*;

@Entity
@Table(name="team")
public class Team {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="name")
	private String teamName;

	public Team() {
	}

	public Team(String teamName) {
		this.teamName = teamName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	@Override
	public String toString() {
		return "Class [id=" + id + ", teamName=" + teamName + "]";
	}
		
}







