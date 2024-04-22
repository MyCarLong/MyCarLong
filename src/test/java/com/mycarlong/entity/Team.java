package com.mycarlong.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//매핑한 팀 엔티티
@Entity
public class Team {

	@Id
	@Column(name = "Team_id")
	private String id;

	private String name;

	@Getter
	@OneToMany(mappedBy = "team")
	private List<TeamMember> members = new ArrayList<>();

	public Team(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Team() {

	}

	//...
}