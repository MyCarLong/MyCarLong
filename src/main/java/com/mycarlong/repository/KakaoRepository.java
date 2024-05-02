package com.mycarlong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycarlong.entity.UserEntity;

@Repository
public interface KakaoRepository extends JpaRepository<UserEntity, Long> {

}