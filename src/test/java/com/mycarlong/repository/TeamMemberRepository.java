package com.mycarlong.repository;

import com.mycarlong.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {

	// 추가적인 필요에 따라 JPQL 쿼리를 사용하는 메서드를 정의할 수 있습니다.
}
