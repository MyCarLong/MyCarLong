package com.mycarlong.repository;

import com.mycarlong.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

	// 팀 이름으로 조회하는 메서드 (선택적)
	List<Team> findByName(String name);
}
