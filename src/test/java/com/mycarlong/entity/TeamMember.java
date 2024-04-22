package com.mycarlong.entity;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

//매핑한 회원 엔티티
@Entity
@RequiredArgsConstructor
public class TeamMember {

	@Id
	@Column(name = "TeamMember_id")
	private String id;

	public String username;

	@ManyToOne
	@JoinColumn(name = "Team_id")
	private Team team;

	public TeamMember(String id, String username) {
		this.id = id;
		this.username = username;
	}

	//...
}